package supernotes.management;

import com.google.api.client.util.DateTime;

import supernotes.constants.MessagesContants;
import supernotes.file_handling.FileHandler;
import supernotes.helpers.InputScanner;
import supernotes.helpers.MyLogger;
import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.NoteFactory;
import supernotes.notionAPI.NotionApiManager;
import supernotes.notionAPI.NotionManager;
import supernotes.translationAPI.*;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class CommandLineInterface {
    private final NoteFactory textNoteFactory;
    private final NoteFactory imageNoteFactory;
    private final FileHandler fileHandler;
    private final NoteManager noteManager;
    private final NotionApiManager notionApiManager;
    private final NotionManager notionManager;
    private final TranslationManager translationManager;
    private static final MyLogger LOGGER = MyLogger.getInstance();


    public CommandLineInterface(NoteFactory textNoteFactory, NoteFactory imageNoteFactory, FileHandler fileHandler, NotionManager notionManager, NotionApiManager notionApiManager, TranslationManager translationManager) {
        this.textNoteFactory = textNoteFactory;
        this.imageNoteFactory = imageNoteFactory;
        this.fileHandler = fileHandler;
        this.noteManager = new NoteManagerDataBase();
        this.notionApiManager = notionApiManager;
        this.notionManager = notionManager;
        this.translationManager = translationManager;
    }

    public void parseCommand(String command) throws SQLException {        
        if (parseAddNoteCommand(command)) {
            return;
        }
        if (parseDeleteNotesByTagCommand(command)) {
            return;
        }
        if (parseDeleteNotesByIdCommand(command)) {
            return;
        }
        if (parseExportPDFPatternCommand(command)) {
            return;
        }
        if (parseExportPDFUsingTagCommand(command)) {
            return;
        }
        if (parseExportPDFFilterTagCommand(command)) {
            return;
        }
        if (parseGetNotionPageContentCommand(command)) {
            return;
        }
        if (parseUpdateNotionPageContentCommand(command)) {
            return;
        }
        if (parseCreateNotionPageCommand(command)) {
            return;
        }
        if (parseHelpCommand(command)) {
            return;
        }
        if (parseShowAllCommand(command)) {
            return;
        }
        if (parseAddNoteWithReminderCommand(command)) {
            return;
        }
        if (parseGetNoteWithReminderCommand(command)) {
            return;
        }
        if (parseDeleteReminderForNoteCommand(command)) {
            return;
        }
        if (parseExportTXTCommand(command)) {
            return;
        }
        if (parseLinkNotesWithORCommand(command)) {
            return;
        }
        if (parseLinkNotesWithANDCommand(command)) {
            return;
        }
        if (parseLinkNotesWithANDAndAtCommand(command)) {
            return;
        }
        if (parseLinkNotesWithANDAndBeforeCommand(command)) {
            return;
        }
        if (parseLinkNotesWithANDAndAfterCommand(command)) {
            return;
        }
        if (parseShowAllLinksByNameCommand(command)) {
            return;
        }
        if (parseTranslateNoteCommand(command)) {
            return;
        }
        if (parseTranslateAllNotesCommand(command)) {
            return;
        }

        handleInvalidCommand();
    }

    public boolean parseAddNoteCommand(String command) {
        Pattern addNotePattern = Pattern.compile("sn add \"([^\"]+)\"(?: --tag \"([^\"]+)\")?(?: \"([^\"]+)\")*");
        Matcher addNoteMatcher = addNotePattern.matcher(command);

        if (addNoteMatcher.matches()) {
            String noteContent = addNoteMatcher.group(1);
            String noteTag = addNoteMatcher.group(2);

            NoteFactory noteFactory = isImage(noteContent) ? imageNoteFactory : textNoteFactory;
            Note note = noteFactory.createNote(noteContent, noteTag, null, null);

            int id = noteManager.addNote(note);
            LOGGER.logInfo(MessagesContants.NoteAddedSuccessufullyWithId + id);

            return true;
        }

        return false;
    }

    public boolean parseDeleteNotesByTagCommand(String command) {
        Pattern deleteNotesPattern = Pattern.compile("^sn delete --tag \"(.*)\"$");
        Matcher deleteNotesMatcher = deleteNotesPattern.matcher(command);

        if (deleteNotesMatcher.matches()) {
            String noteTag = deleteNotesMatcher.group(1);

            noteManager.deleteByTag(noteTag);
            LOGGER.logInfo(MessagesContants.NoteDeletedSuccessfully);

            return true;
        }

        return false;
    }
    
    private boolean parseDeleteNotesByIdCommand(String command) {
        Pattern deleteNoteByIdPattern = Pattern.compile("^sn delete (\\d+)$");
        Matcher deleteNoteByIdMatcher = deleteNoteByIdPattern.matcher(command); 
        
        if (deleteNoteByIdMatcher.matches()) {
            try {
                String noteIdString = deleteNoteByIdMatcher.group(1);
                int noteId = Integer.parseInt(noteIdString);
        
                noteManager.deleteNoteByNoteId(noteId);
                LOGGER.logInfo(MessagesContants.NoteDeletedSuccessfully);

                return true;

            } catch (NumberFormatException e) {
                LOGGER.logError(MessagesContants.ErrorNoteIdInvalid);
            }
        }

        return false;
    }

    private boolean parseExportPDFPatternCommand(String command) {
        Pattern exportPDFPattern = Pattern.compile("^sn export --all \"(.*)\"$");
        Matcher exportPDFMatcher = exportPDFPattern.matcher(command);

        if (exportPDFMatcher.matches()) {
            String filePath = exportPDFMatcher.group(1);
    
            fileHandler.exportPdfFile(filePath, null);
            LOGGER.logInfo(MessagesContants.NoteExportedSuccessfully);

            return true;
        }

        return false;
    }

    private boolean parseExportPDFUsingTagCommand(String command) {
        Pattern exportPDFUsingTagPattern = Pattern.compile("^sn export --tag \"(.*)\" \"(.*)\"$");
        Matcher exportPDFUsingTagMatcher = exportPDFUsingTagPattern.matcher(command);

        if (exportPDFUsingTagMatcher.matches()) {
            String filePath = exportPDFUsingTagMatcher.group(2);
            String noteTag = exportPDFUsingTagMatcher.group(1);
    
            fileHandler.exportPdfFile(filePath, noteTag);
            LOGGER.logInfo(MessagesContants.NoteExportedSuccessfully);
            return true;
        }

        return false;
    }

    private boolean parseExportPDFFilterTagCommand(String command) {
        Pattern exportPDFFilterTagPattern = Pattern.compile("sn export --word \"([^\"]*)\" \"([^\"]*)\"?");
        Matcher exportPDFFilterTagMatcher = exportPDFFilterTagPattern.matcher(command);

        if (exportPDFFilterTagMatcher.matches()) {
            String filePath = exportPDFFilterTagMatcher.group(2);
            String filter = exportPDFFilterTagMatcher.group(1);
    
            fileHandler.exportPdfFileUsingFilter(filePath, filter);
            LOGGER.logInfo(MessagesContants.NoteExportedSuccessfully);
            return true;
        }

        return false;
    }


    private boolean parseGetNotionPageContentCommand(String command) {
        Pattern getNotionPageContentPattern = Pattern.compile("sn notion get --page \"(.*)\"$");
        Matcher getNotionPageContentMatcher = getNotionPageContentPattern.matcher(command);

        if (getNotionPageContentMatcher.matches()) {
            String pageId = getNotionPageContentMatcher.group(1);
    
            String notionPage = notionApiManager.retrievePageContent(pageId);
            String parentId = notionManager.extractParentPageId(notionPage);
            String content = notionManager.extractPageTitle(notionPage);
    
            // Vérifiez si la page existe déjà dans la base de données
            if (!noteManager.doesNoteExist(pageId)) {
                NoteFactory noteFactory = isImage(content) ? imageNoteFactory : textNoteFactory;
                Note note = noteFactory.createNote(content, "notion", parentId, pageId);
                noteManager.addNote(note);
                LOGGER.logInfo(MessagesContants.NotionPageAddedSuccessfully);

            }
            return true;
        }

        return false;
    }

    private boolean parseUpdateNotionPageContentCommand(String command) {
        Pattern updateNotionPageContentPattern = Pattern.compile("sn notion update \"(.*)\" --note \"(.*)\"$");
        Matcher updateNotionPageContentnMatcher = updateNotionPageContentPattern.matcher(command);

        if (updateNotionPageContentnMatcher.matches()) {
            String newContent = updateNotionPageContentnMatcher.group(1);
            String oldContent = updateNotionPageContentnMatcher.group(2);
    
            String pageId = noteManager.getPageId(oldContent);
            if (pageId != null) {
                noteManager.updateNoteContentInDB(pageId, newContent);
            } else {
                LOGGER.logError(MessagesContants.ErrorNotionPageNotFound);
            }
            return true;
        }

        return false;
    }

    private boolean parseCreateNotionPageCommand(String command) {
        Pattern createNotionPagePattern = Pattern.compile("sn notion create \"(.*)\"$");
        Matcher createNotionPageMatcher = createNotionPagePattern.matcher(command);

        if (createNotionPageMatcher.matches()) {
            String content = createNotionPageMatcher.group(1);
    
            // Vérifier si l'ID de page parent est déjà enregistré dans la base de données
            String parentPageId = noteManager.getParentPageId(); // Récupérer l'ID de page parent enregistré
    
            if (parentPageId == null) {
                // Demander à l'utilisateur d'entrer l'ID de la page parent pour la première utilisation
                LOGGER.logInfo("Veuillez entrer l'ID de la page :");
                Scanner scanner = InputScanner.getInstance();
                parentPageId = scanner.nextLine();
                
            }
    
            String propertiesJson = "{ " + "\"parent\": { \"page_id\": \"" + parentPageId + "\"}, " + "\"properties\": { " + "\"title\": [{ \"text\": { \"content\": \"" + content + "\"} }], " + "\"Content\": [{ \"text\": { \"content\": \"Contenu de la note\" } }], " + "\"Tag\": [{ \"text\": { \"content\": \"Tag de la note\" } }] " + "} " + "}";
    
            NoteFactory noteFactory = isImage(content) ? imageNoteFactory : textNoteFactory;
            String newPage = notionApiManager.createNotionPage(parentPageId, propertiesJson);
    
            if (newPage != null && !newPage.isEmpty()) {
                String newPageId = notionManager.extractNewPageId(newPage);
                Note note = noteFactory.createNote(content, "notion", parentPageId, newPageId);
                LOGGER.logInfo(MessagesContants.NoteAddedSuccessufully);

                noteManager.addNote(note);
    
            }
            return true;
        }

        return false;
    }

    public boolean parseTranslateNoteCommand(String command) {
        Pattern translateNotePattern = Pattern.compile("sn translate \"([^\"]*)\" --to \"([^\"]+)\"(?: --tag \"([^\"]*)\")?");
        Matcher translateNoteMatcher = translateNotePattern.matcher(command);

        if (translateNoteMatcher.matches()) {
            try {
                String text = translateNoteMatcher.group(1);
                String targetLanguage = translateNoteMatcher.group(2);
                String tag = translateNoteMatcher.group(3);

            String translatedText = translationManager.translateText(text, targetLanguage, tag);
                if (translatedText != null) {

                    LOGGER.logInfo("Note translated successfully: " + translatedText);
                }
                return true;
            } catch (Exception e) {
                LOGGER.logError("Error translating note: " + e.getMessage());
            }
        }

        return false;
    }

    public boolean parseTranslateAllNotesCommand(String command) {
        Pattern translateExistingNotePattern = Pattern.compile("sn translate --note (\\d+) --to \"([^\"]*)\"(?: --tag \"([^\"]*)\")?");
        Matcher translateExistingNoteMatcher = translateExistingNotePattern.matcher(command);

        if (translateExistingNoteMatcher.matches()) {
            try {
                int noteId = Integer.parseInt(translateExistingNoteMatcher.group(1));
                String targetLanguage = translateExistingNoteMatcher.group(2);
                String tag = translateExistingNoteMatcher.group(3); 

                String translatedText = translationManager.translateNote(noteId, targetLanguage, tag);
                
                if (translatedText != null) {
                    LOGGER.logInfo("Translated text: " + translatedText);
                } else {
                    LOGGER.logInfo("Failed to translate the note.");
                }
                return true;
            } catch (Exception e) {
                LOGGER.logError("Error translating notes: " + e.getMessage());
            }
        }

        return false;
    }



    private boolean parseHelpCommand(String command) {
        Pattern helpPattern = Pattern.compile("sn --help");
        Matcher helpMatcher = helpPattern.matcher(command);

        if (helpMatcher.matches()) {
            displayHelp();
            return true;
        }

        return false;
    }

    private boolean parseShowAllCommand(String command) {
        Pattern showAllPattern = Pattern.compile("sn show notes");
        Matcher showAllMatcher = showAllPattern.matcher(command);

        if (showAllMatcher.matches()) {
            List<Note> showAllNotes = new ArrayList<>();
            showAllNotes = noteManager.showAllNotes();
            LOGGER.logInfo("Notes :- \\n" + //
                                "\\n" + //
                                "\\n" + //
                                "");

            showAllNotesDesigner(showAllNotes);
            return true;
        }

        return false;
    }

    private boolean parseAddNoteWithReminderCommand(String command) {
        Pattern addNoteWithReminderPattern = Pattern.compile("sn add \"([^\"]*)\"(?: --tag \"([^\"]*)\")? --reminder \"([^\"]*)\"");
        Matcher addNoteWithReminderMatcher = addNoteWithReminderPattern.matcher(command);

        if (addNoteWithReminderMatcher.matches()) {
            try {
                String noteContent = addNoteWithReminderMatcher.group(1);
                String noteTag = addNoteWithReminderMatcher.group(2);
                String reminder = addNoteWithReminderMatcher.group(3);
    
                // Création d'une nouvelle note
                NoteFactory noteFactory = isImage(noteContent) ? imageNoteFactory : textNoteFactory;
                Note note = noteFactory.createNote(noteContent, noteTag, null, null);
                int noteId = noteManager.addNote(note);
    
                DateTimeFormatter userDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                LocalDateTime reminderDateTime = LocalDateTime.parse(reminder, userDateTimeFormatter);
    
                noteManager.addReminder(noteId, reminderDateTime);
    
                // Formatter pour le format attendu par Google Calendar
                DateTimeFormatter googleCalendarDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
    
                // Conversion de la date saisie par l'utilisateur au format LocalDateTime
                LocalDateTime localDateTime = LocalDateTime.parse(reminder, userDateTimeFormatter);
                String formattedDate = localDateTime.atOffset(ZoneOffset.UTC).format(googleCalendarDateTimeFormatter);
    
                DateTime startDateTime = new DateTime(formattedDate);
    
                noteManager.addNoteWithReminderToCalendar(noteContent, startDateTime.toString());
    
                LOGGER.logInfo(MessagesContants.NoteWithReminderAddedSuccessfully);

            } catch (DateTimeParseException e) {
                LOGGER.logInfo(MessagesContants.ErrorDateFormatInvalid);
            }
            return true;
        }

        return false;
    }

    private boolean parseGetNoteWithReminderCommand(String command) {
        Pattern getNoteWithReminderPattern = Pattern.compile("sn get --reminder --tag \"(.*)\"$");
        Matcher getNoteWithReminderMatcher = getNoteWithReminderPattern.matcher(command);

        if (getNoteWithReminderMatcher.matches()) {
            String tag = getNoteWithReminderMatcher.group(1);
    
            List<Note> allNotesByTag = noteManager.getByTag(tag);
    
            if (!allNotesByTag.isEmpty()) {
                for (Note note : allNotesByTag) {
                    int noteId = note.getId(); // Récupération de l'ID de la note
    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    List<LocalDateTime> reminders = noteManager.getReminders(noteId);
    
                    if (!reminders.isEmpty()) {
                        // Affiche les rappels associés à la note avec le tag spécifique
                        LOGGER.logInfo("Rappels pour la note avec l'ID" + noteId + " " + note.getContent());

                        for (LocalDateTime reminder : reminders) {
                            String formattedReminder = reminder.format(formatter);
                            LOGGER.logInfo("- " + formattedReminder);
                        }
                    } else {
                        // Aucun rappel trouvé pour cette note
                        LOGGER.logInfo("Aucun rappel trouvé pour la note avec l'ID" + noteId + " " + note.getContent());
                        
                    }
                }
            } else {
                LOGGER.logError(MessagesContants.ErrorNoNoteFoundWithTag);
            }
            return true;
        }

        return false;
    }

    private boolean parseDeleteReminderForNoteCommand(String command) {
        Pattern deleteReminderForNotePattern = Pattern.compile("sn delete --reminder --tag \"(.*)\"$");
        Matcher deleteReminderForNoteMatcher = deleteReminderForNotePattern.matcher(command);

        if (deleteReminderForNoteMatcher.matches()) {
            String tag = deleteReminderForNoteMatcher.group(1);
    
            List<Note> allNotesByTag = noteManager.getByTag(tag);
    
            if (!allNotesByTag.isEmpty()) {
                boolean anyReminderDeleted = false;
                for (Note note : allNotesByTag) {
                    int noteId = note.getId(); // Récupération de l'ID de la note
                    boolean reminderDeleted = noteManager.deleteRemindersByNoteId(noteId);
                    if (!reminderDeleted) {
                        anyReminderDeleted = true;
                    } else {
                        // Aucun rappel trouvé pour cette note
                        LOGGER.logInfo("Aucun rappel trouvé pour la note avec le tag " + tag + " " + note.getContent());
                    }
                }
                if (anyReminderDeleted) {
                    LOGGER.logInfo(MessagesContants.ReminderDeletedSuccessfully);
                }
            } else {
                LOGGER.logError(MessagesContants.ErrorNoNoteFoundWithTag);
            }
            return true;
        }

        return false;
    }

    private boolean parseExportTXTCommand(String command) {
        Pattern exportTXTPattern = Pattern.compile("sn export --text \"(.*)\"$");
        Matcher exportTXTMatcher = exportTXTPattern.matcher(command);

        if (exportTXTMatcher.matches()) {
            String filePath = exportTXTMatcher.group(1);
    
            fileHandler.exportToText(filePath);
            LOGGER.logInfo(MessagesContants.NoteExportedSuccessfully);
            return true;
        }

        return false;
    }

    private boolean parseLinkNotesWithORCommand(String command) throws SQLException {
        Pattern linkNotesPatternWithOR = Pattern.compile("sn link --id \"([^\"]+)\" --tag \"([^\"]+)\"(?: +or +\"([^\"]+)\")?(?:\\s+--name \"([^\"]+)\")?");
        Matcher linkNotesMatcherWithOR = linkNotesPatternWithOR.matcher(command);

        if (linkNotesMatcherWithOR.matches()) {
            int noteId = Integer.parseInt(linkNotesMatcherWithOR.group(1));
            String[] tags;
    
            if (linkNotesMatcherWithOR.group(3) != null) {
                tags = new String[]{linkNotesMatcherWithOR.group(2), linkNotesMatcherWithOR.group(3)};
            } else {
                tags = new String[]{linkNotesMatcherWithOR.group(2)};
            }
    
            String linkName = linkNotesMatcherWithOR.group(4);
    
            // Assuming noteManager.linkNotes returns an int
            int linkId = noteManager.linkNotesWithOR(noteId, tags, linkName);
            if (linkId == -1) {
                LOGGER.logError(MessagesContants.ErrorLinkNotesFailed);
            } else {
                LOGGER.logInfo(MessagesContants.LinkingNotesSuccessfullyWithId + linkId);
            }
            return true;
        }

        return false;
    }

    private boolean parseLinkNotesWithANDCommand(String command) throws SQLException {
        Pattern linkNotesPatternWithAND = Pattern.compile("sn link --id \"([^\"]+)\" --tag \"([^\"]+)\" and \"([^\"]+)\" --name \"([^\"]+)\"");
        Matcher linkNotesMatcherWithAND = linkNotesPatternWithAND.matcher(command);

        if (linkNotesMatcherWithAND.matches()) {
            int noteId = Integer.parseInt(linkNotesMatcherWithAND.group(1));
            String[] tags;
    
            if (linkNotesMatcherWithAND.group(3) != null) {
                tags = new String[]{linkNotesMatcherWithAND.group(2), linkNotesMatcherWithAND.group(3)};
            } else {
                tags = new String[]{linkNotesMatcherWithAND.group(2)};
            }
    
            String linkName = linkNotesMatcherWithAND.group(4);
    
            // Assuming noteManager.linkNotes returns an int
            int linkId = noteManager.linkNotesWithAND(noteId, tags, linkName);
            if (linkId == -1) {
                LOGGER.logError(MessagesContants.ErrorLinkNotesFailed);
            } else {
                LOGGER.logInfo(MessagesContants.LinkingNotesSuccessfullyWithId + linkId);
            }
            return true;
        }

        return false;
    }

    private boolean parseLinkNotesWithANDAndAtCommand(String command) throws SQLException {
        Pattern linkNotesPatternWithANDAndAt = Pattern.compile("sn link --id \"([^\"]+)\" --tag \"([^\"]+)\" and \"([^\"]+)\" --name \"([^\"]+)\" --at \"([^\"]+)\"");
        Matcher linkNotesMatcherWithANDAndAt = linkNotesPatternWithANDAndAt.matcher(command);

        if (linkNotesMatcherWithANDAndAt.find()) {
            int noteId = Integer.parseInt(linkNotesMatcherWithANDAndAt.group(1));
            String[] tags;
    
            if (linkNotesMatcherWithANDAndAt.group(3) != null) {
                tags = new String[]{linkNotesMatcherWithANDAndAt.group(2), linkNotesMatcherWithANDAndAt.group(3)};
            } else {
                tags = new String[]{linkNotesMatcherWithANDAndAt.group(2)};
            }
    
            String linkName = linkNotesMatcherWithANDAndAt.group(4);
            String date = linkNotesMatcherWithANDAndAt.group(5);
            // Assuming noteManager.linkNotes returns an int
            int linkId = noteManager.linkNotesWithANDAtDate(noteId, tags, linkName, date);
            if (linkId == -1) {
                LOGGER.logError(MessagesContants.ErrorLinkNotesFailed);
            } else {
                LOGGER.logInfo(MessagesContants.LinkingNotesSuccessfully);
            }
            return true;
        }

        return false;
    }

    private boolean parseLinkNotesWithANDAndBeforeCommand(String command) throws SQLException {
        Pattern linkNotesPatternWithANDAndBefore = Pattern.compile("sn link --id \"([^\"]+)\" --tag \"([^\"]+)\" and \"([^\"]+)\" --name \"([^\"]+)\" --before \"([^\"]+)\"");
        Matcher linkNotesMatcherWithANDAndBefore = linkNotesPatternWithANDAndBefore.matcher(command);

        if (linkNotesMatcherWithANDAndBefore.find()) {
            int noteId = Integer.parseInt(linkNotesMatcherWithANDAndBefore.group(1));
            String[] tags;
    
            if (linkNotesMatcherWithANDAndBefore.group(3) != null) {
                tags = new String[]{linkNotesMatcherWithANDAndBefore.group(2), linkNotesMatcherWithANDAndBefore.group(3)};
            } else {
                tags = new String[]{linkNotesMatcherWithANDAndBefore.group(2)};
            }
    
            String linkName = linkNotesMatcherWithANDAndBefore.group(4);
            String date = linkNotesMatcherWithANDAndBefore.group(5);
            // Assuming noteManager.linkNotes returns an int
            int linkId = noteManager.linkNotesWithANDBeforeDate(noteId, tags, linkName, date);
            if (linkId == -1) {
                LOGGER.logError(MessagesContants.ErrorLinkNotesFailed);
            } else {
                LOGGER.logInfo(MessagesContants.LinkingNotesSuccessfully);
            }
            return true;
        }

        return false;
    }

    private boolean parseLinkNotesWithANDAndAfterCommand(String command) {
        Pattern linkNotesPatternWithANDAndAfter = Pattern.compile("sn link --id \"([^\"]+)\" --tag \"([^\"]+)\" and \"([^\"]+)\" --name \"([^\"]+)\" --after \"([^\"]+)\"");
        Matcher linkNotesMatcherWithANDAndAfter = linkNotesPatternWithANDAndAfter.matcher(command);

        if (linkNotesMatcherWithANDAndAfter.find()) {
            int noteId = Integer.parseInt(linkNotesMatcherWithANDAndAfter.group(1));
            String[] tags;
    
            if (linkNotesMatcherWithANDAndAfter.group(3) != null) {
                tags = new String[]{linkNotesMatcherWithANDAndAfter.group(2), linkNotesMatcherWithANDAndAfter.group(3)};
            } else {
                tags = new String[]{linkNotesMatcherWithANDAndAfter.group(2)};
            }
    
            String linkName = linkNotesMatcherWithANDAndAfter.group(4);
            String date = linkNotesMatcherWithANDAndAfter.group(5);
            // Assuming noteManager.linkNotes returns an int
            int linkId = noteManager.linkNotesWithANDAfterDate(noteId, tags, linkName, date);
            if (linkId == -1) {
                LOGGER.logError(MessagesContants.ErrorLinkNotesFailed);
            } else {
                LOGGER.logInfo(MessagesContants.LinkingNotesSuccessfully);
            }
            return true;
        }

        return false;
    }

    private boolean parseShowAllLinksByNameCommand(String command) {
        Pattern showAllLinksByNamePattern = Pattern.compile("sn show --link\\s+\"([^\"]+)\"");
        Matcher showAllLinksByNameMatcher = showAllLinksByNamePattern.matcher(command);

        if (showAllLinksByNameMatcher.matches()) {
            String linkName = showAllLinksByNameMatcher.group(1);
            boolean linkExistenceCheck = noteManager.getAllLinksByName(linkName);
            if (!linkExistenceCheck) {
                LOGGER.logInfo(MessagesContants.ErrorNoLinkFound + linkName);

            return true;
            }
        }

        return false;
    }

    private void handleInvalidCommand() {
        LOGGER.logInfo("Commande invalide. Tapez 'sn --help' pour afficher l'aide.");
    }

    private boolean isImage(String path) {
        return path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif");
    }

    public void displayHelp() {
        LOGGER.logInfo("Bienvenue dans SuperNotes !");
        LOGGER.logInfo("Voici les commandes disponibles :");
        LOGGER.logInfo("- Pour ajouter une note texte: sn add \"Contenu de la note\" --tag \"Tag de la note\"");
        LOGGER.logInfo("- Pour ajouter une note image : sn add \"Chemin de l'image\" --tag \"Tag de la note\"");
        LOGGER.logInfo("- Pour exporter toutes les notes en PDF : sn export --all \"Chemin du fichier PDF\"");
        LOGGER.logInfo("- Pour exporter toutes les notes en TXT : sn export --text \"Chemin du fichier TXT\"");
        LOGGER.logInfo("- Pour supprimer des notes par ID : sn delete \"ID de la note\"");
        LOGGER.logInfo("- Pour supprimer des notes par tag : sn delete --tag \"Tag de la note\"");
        LOGGER.logInfo("- Pour exporter des notes par tag en PDF : sn export --tag \"Tag de la note\" \"Chemin du fichier PDF\"");
        LOGGER.logInfo("- Pour exporter des notes filtrées par contenu en PDF : sn export --word \"Mot clé\" \"Chemin du fichier PDF\" (le mot clé est facultatif)");
        LOGGER.logInfo("- Pour obtenir le contenu d'une page Notion : sn notion get --page \"ID de la page\"");
        LOGGER.logInfo("- Pour mettre à jour le contenu d'une page Notion : sn notion update \"Nouveau contenu\" --note \"Ancien contenu\"");
        LOGGER.logInfo("- Pour créer une nouvelle page Notion : sn notion create \"Contenu de la nouvelle page\"");
        LOGGER.logInfo("- Pour ajouter une note avec rappel : sn add \"Contenu de la note\" --tag \"Tag de la note\" --reminder \"Date et heure du rappel\"");
        LOGGER.logInfo("- Pour afficher les rappels pour une note par tag : sn get --reminder --tag \"Tag de la note\"");
        LOGGER.logInfo("- Pour supprimer les rappels pour une note par tag : sn delete --reminder --tag \"Tag de la note\"");
        LOGGER.logInfo("- Pour lier des notes : sn link --id \"ID_de_la_note\" --tag \"tag1\" [and/or] \"tag2\" [...] --name \"Nom_de_lien\" [--at/--before/--after \"Date\"]\n");
        LOGGER.logInfo("- Pour Afficher les Liens des notes : sn show --link \"Nom_de_lien\"\n");
        LOGGER.logInfo("- Pour traduire du texte vers une autre langue : sn translate \"text\" --to \"language\" [--tag \"Tag de la note\" (le tag est facultatif)]");
        LOGGER.logInfo("- Pour traduire une note existante vers une autre langue : sn translate --note <ID de la note> --to \"language\" [--tag \"Tag de la note\" (le tag est facultatif)]");
        LOGGER.logInfo("- Pour afficher toutes les notes : sn show notes");
        LOGGER.logInfo("Pour quitter l'application : exit");
    }


    public void showAllNotesDesigner(List<Note> notes) {
        String boldText = "\u001B[1m";
        String resetText = "\u001B[0m";
        int noteLength = 33;
        int tagLength = 18;
        int timeLength = 22;
        for (Note n : notes) {
            String content = n.getContent().toString();
            if (content != null && ((content.length()) > noteLength)) {
                noteLength = content.length();
            }
        }
        for (Note n : notes) {
            String tag = n.getTag();
            if (tag != null && (tagLength < (tag.length()))) {
                tagLength = tag.length();
            }
        }
        for (int i = 0; i < noteLength; i++) {
            System.out.print("-");
        }
        for (int i = 0; i < tagLength; i++) {
            System.out.print("-");
        }
        for (int i = 0; i < (timeLength + 7); i++) {
            System.out.print("-");
        }
        String fh1 = "| Note Content";
        System.out.print("\n|" + boldText + " Note Content" + resetText);
        for (int i = 0; i < (noteLength - fh1.length() + 2); i++) {
            System.out.print(" ");
        }
        String fh2 = "| Tag";
        System.out.print("|" + boldText + " Tag" + resetText);
        for (int i = 0; i < (tagLength - (fh2.length()) + 2); i++) {
            System.out.print(" ");
        }
        String fh3 = "| Time";
        System.out.print("|" + boldText + " Time" + resetText);
        for (int i = 0; i < (timeLength - (fh3.length()) + 2); i++) {
            System.out.print(" ");
        }
        System.out.print("|\n");

        for (int i = 0; i < noteLength; i++) {
            System.out.print("-");
        }
        for (int i = 0; i < tagLength; i++) {
            System.out.print("-");
        }
        for (int i = 0; i < (timeLength + 7); i++) {
            System.out.print("-");
        }
        System.out.print("\n");

        for (Note n : notes) {
            String content = " ";

            if (n.getContent() != null) {
                content = n.getContent().toString();
            } else {
                content = " ";
            }

            if (n.getType() != null && n.getType().equals("image")) {
                String path = ((ImageNote) n).getPath();
                content = path;
            }
            String tag = n.getTag();
            if (tag == null) {
                tag = " ";
            }
            String time = n.getTime();
            if (time == null) {
                time = " ";
            }

            int contentlength = content.length();

            int startPoint = 0;
            int endPoint = noteLength;
            int furtherCall = 0;

            if (contentlength > noteLength) {
                System.out.print("| " + content.substring(startPoint, endPoint));
                furtherCall = 1;
            } else System.out.print("| " + content);
            for (int i = 0; i < (noteLength - content.length()); i++) {
                System.out.print(" ");
            }
            System.out.print("| " + tag);
            for (int i = 0; i < (tagLength - tag.length()); i++) {
                System.out.print(" ");
            }
            System.out.print("| " + time);
            for (int i = 0; i < (timeLength - time.length()); i++) {
                System.out.print(" ");
            }
            System.out.print("|");
            while (furtherCall == 1) {
                contentlength = contentlength - noteLength - 1;
                startPoint = endPoint + 1;

                System.out.print("\n");
                if (contentlength > noteLength) {
                    endPoint = endPoint + noteLength + 1;
                    System.out.print("| " + content.substring(startPoint, endPoint));
                    for (int i = 0; i < (noteLength - (endPoint - startPoint)); i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    for (int i = 0; i < tagLength + 1; i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    for (int i = 0; i < timeLength + 1; i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    furtherCall = 1;
                } else {
                    endPoint = endPoint + contentlength;
                    System.out.print("| " + content.substring(startPoint, endPoint));
                    for (int i = 0; i < (noteLength - (endPoint - startPoint)); i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    for (int i = 0; i < tagLength + 1; i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    for (int i = 0; i < timeLength + 1; i++) {
                        System.out.print(" ");
                    }
                    System.out.print("|");
                    furtherCall = 0;
                }
            }
            System.out.print("\n");
            System.out.print("|");
            for (int i = 0; i <= noteLength; i++) {
                System.out.print("-");
            }
            System.out.print("|");
            for (int i = 0; i <= tagLength; i++) {
                System.out.print("-");
            }
            System.out.print("|");
            for (int i = 3; i < (timeLength + 4); i++) {
                System.out.print("-");
            }
            System.out.print("|");
            System.out.print("\n");

        }
    }

}

