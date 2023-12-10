package supernotes.management;

import supernotes.fileHandling.FileHandler;
import supernotes.notes.*;
import supernotes.notionAPI.NotionAPI;
import supernotes.notionAPI.NotionApiManager;
import supernotes.notionAPI.NotionManager;
import supernotes.notionAPI.NotionAPI;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineInterface {
    private final NoteFactory textNoteFactory;
    private final NoteFactory imageNoteFactory;
    private final FileHandler fileHandler;
    private final NoteManager noteManager;
    private final NotionApiManager notionApiManager;
    private final NotionManager notionManager;

    public CommandLineInterface(NoteFactory textNoteFactory, NoteFactory imageNoteFactory, FileHandler fileHandler, NotionManager notionManager, NotionApiManager notionApiManager) {
        this.textNoteFactory = textNoteFactory;
        this.imageNoteFactory = imageNoteFactory;
        this.fileHandler = fileHandler;
        this.noteManager = new NoteManagerDataBase();
        this.notionApiManager = notionApiManager;
        this.notionManager = notionManager;
    }

    public void parseCommand(String command) {
        Pattern addNotePattern = Pattern.compile("sn add \"([^\"]*)\"(?: --tag \"([^\"]*)\")?");
        Matcher addNoteMatcher = addNotePattern.matcher(command);

        Pattern exportPDFPattern = Pattern.compile("^sn export --all \"(.*)\"$");
        Matcher exportPDFMatcher = exportPDFPattern.matcher(command);

        Pattern deleteNotesPattern = Pattern.compile("^sn delete --tag \"(.*)\"$");
        Matcher deleteNotesMatcher = deleteNotesPattern.matcher(command);

        Pattern exportPDFUsingTagPattern = Pattern.compile("^sn export --tag \"(.*)\" \"(.*)\"$");
        Matcher exportPDFUsingTagMatcher = exportPDFUsingTagPattern.matcher(command);

        Pattern exportPDFFilterTagPattern = Pattern.compile("sn export --word \"([^\"]*)\" \"([^\"]*)\"?");
        Matcher exportPDFFilterTagMatcher = exportPDFFilterTagPattern.matcher(command);
        
        Pattern getNotionPageContentPattern = Pattern.compile("sn notion get --page \"(.*)\"$");
        Matcher getNotionPageContentMatcher = getNotionPageContentPattern.matcher(command);
        
        Pattern updateNotionPageContentPattern = Pattern.compile("sn notion update \"(.*)\" --note \"(.*)\"$");
        Matcher updateNotionPageContentnMatcher = updateNotionPageContentPattern.matcher(command);

        Pattern createNotionPagePattern = Pattern.compile("sn notion create \"(.*)\"$");
        Matcher createNotionPageMatcher = createNotionPagePattern.matcher(command);

        Pattern helpPattern = Pattern.compile("sn --help");
        Matcher helpMatcher = helpPattern.matcher(command);

        
        if (addNoteMatcher.matches()) {
            String noteContent = addNoteMatcher.group(1);
            String noteTag = addNoteMatcher.group(2);

            NoteFactory noteFactory = isImage(noteContent) ? imageNoteFactory : textNoteFactory;
            Note note = noteFactory.createNote(null, noteContent, noteTag, null, null);

            noteManager.addNote(note);
            System.out.println("Note ajoutée avec succès !");
        }

        else if (deleteNotesMatcher.matches()) {
            String noteTag = deleteNotesMatcher.group(1);

            noteManager.deleteByTag(noteTag);
            System.out.println("notes exporter avec succès !");

        }

        else if (exportPDFUsingTagMatcher.matches()) {
            String filePath = exportPDFUsingTagMatcher.group(2);
            String noteTag = exportPDFUsingTagMatcher.group(1);

            fileHandler.exportPdfFile(filePath, noteTag);
            System.out.println("notes exporter avec succès !");

        }

        else if (exportPDFFilterTagMatcher.matches()) {
            String filePath = exportPDFFilterTagMatcher.group(2);
            String filter = exportPDFFilterTagMatcher.group(1);

            fileHandler.exportPdfFileUsingFilter(filePath, filter);
            System.out.println("notes exporter avec succès !");

        }

        else if (exportPDFMatcher.matches()) {
            String filePath = exportPDFMatcher.group(1);

            fileHandler.exportPdfFile(filePath, null);
            System.out.println("notes exporter avec succès !");

        }

        else if (getNotionPageContentMatcher.matches()) {
            String pageId = getNotionPageContentMatcher.group(1);

            String notionPage = notionApiManager.retrievePageContent(pageId); 
            String parentId = notionManager.extractParentPageId(notionPage);
            String title = notionManager.extractPageTitle(notionPage); 

            // Vérifiez si la page existe déjà dans la base de données
            if (!noteManager.doesNoteExist(pageId)) {
                    NoteFactory noteFactory = isImage(title) ? imageNoteFactory : textNoteFactory;
                    Note note = noteFactory.createNote(null, title, "notion", parentId, pageId);
                    noteManager.addNote(note);
                    System.out.println("Page ajoutée à la base de données : ");
            }

        }
        
        else if (updateNotionPageContentnMatcher.matches()) {
            String newContent = updateNotionPageContentnMatcher.group(1);
            String oldContent = updateNotionPageContentnMatcher.group(2);

            String pageId = noteManager.getPageId(oldContent);
            if (pageId != null){
                String propertiesJson = "{ \"properties\": { \"title\": [{\"text\": {\"content\": \"" + newContent + "\"}}] } }";

                String updatedPage = notionApiManager.updatePageProperties(pageId, propertiesJson);
                noteManager.updateNoteContentInDB(pageId, newContent);
            } else {
                System.out.println("Page introuvable");
            }

        }

        else if (createNotionPageMatcher.matches()) {
            String content = createNotionPageMatcher.group(1);

            // Vérifier si l'ID de page parent est déjà enregistré dans la base de données
            String parentPageId = noteManager.getParentPageId(); // Récupérer l'ID de page parent enregistré

            if (parentPageId == null) {
                // Demander à l'utilisateur d'entrer l'ID de la page parent pour la première utilisation
                System.out.println("Veuillez entrer l'ID de la page : ");
                Scanner scanner = new Scanner(System.in);
                parentPageId = scanner.nextLine();
            }

            String propertiesJson = "{ " +
                    "\"parent\": { \"page_id\": \"" + parentPageId + "\"}, " +
                    "\"properties\": { " +
                        "\"title\": [{ \"text\": { \"content\": \"" + content + "\"} }], " +
                        "\"Content\": [{ \"text\": { \"content\": \"Contenu de la note\" } }], " +
                        "\"Tag\": [{ \"text\": { \"content\": \"Tag de la note\" } }] " +
                    "} " +
                "}";     

            NoteFactory noteFactory = isImage(content) ? imageNoteFactory : textNoteFactory;
            String newPage = notionApiManager.createNotionPage(parentPageId, propertiesJson);

            if (newPage != null && !newPage.isEmpty()) {
                String newPageId = notionManager.extractNewPageId(newPage);
                Note note = noteFactory.createNote(null, content, null, parentPageId, newPageId);
                System.out.println("Note ajoutée avec succès !");
                noteManager.addNote(note);
        }
    }

    else if (helpMatcher.matches()){
        displayHelp();
    }

    else if(!command.equals("exit")) {
            System.out.println("Commande invalide. Tapez 'sn --help' pour afficher l'aide.");
        }
    }

    private boolean isImage(String path) {
        return path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif");
    }

    public void displayHelp() {
        System.out.println("Bienvenue dans SuperNotes !");
        System.out.println("Voici les commandes disponibles :");
        System.out.println("- Pour ajouter une note : sn add \"Contenu de la note\" --tag \"Tag de la note\" (le tag est facultatif)");
        System.out.println("- Pour exporter toutes les notes en PDF : sn export --all \"Chemin du fichier PDF\"");
        System.out.println("- Pour supprimer des notes par tag : sn delete --tag \"Tag de la note\"");
        System.out.println("- Pour exporter des notes par tag en PDF : sn export --tag \"Tag de la note\" \"Chemin du fichier PDF\"");
        System.out.println("- Pour exporter des notes filtrées par mot clé en PDF : sn export --word \"Mot clé\" \"Chemin du fichier PDF\" (le mot clé est facultatif)");
        System.out.println("- Pour obtenir le contenu d'une page Notion : sn notion get --page \"ID de la page\"");
        System.out.println("- Pour mettre à jour le contenu d'une page Notion : sn notion update \"Nouveau contenu\" --note \"Ancien contenu\"");
        System.out.println("- Pour créer une nouvelle page Notion : sn notion create \"Contenu de la nouvelle page\"");
        System.out.println("Pour quitter l'application : exit");
    }

}

