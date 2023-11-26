package supernotes.management;

import supernotes.fileHandling.FileHandler;
import supernotes.notes.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandLineInterface {
    private final NoteFactory textNoteFactory;
    private final NoteFactory imageNoteFactory;
    private final FileHandler fileHandler;
    private final NoteManager noteManager;

    public CommandLineInterface(NoteFactory textNoteFactory, NoteFactory imageNoteFactory, FileHandler fileHandler) {
        this.textNoteFactory = textNoteFactory;
        this.imageNoteFactory = imageNoteFactory;
        this.fileHandler = fileHandler;
        this.noteManager = new NoteManagerDataBase();
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
        
        if (addNoteMatcher.matches()) {
            String noteContent = addNoteMatcher.group(1);
            String noteTag = addNoteMatcher.group(2);

            NoteFactory noteFactory = isImage(noteContent) ? imageNoteFactory : textNoteFactory;
            Note note = noteFactory.createNote(noteContent, noteTag);

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

        else if(!command.equals("exit")) {
            System.out.println("Commande invalide.");
        }
    }

    private boolean isImage(String path) {
        return path.endsWith(".jpg") || path.endsWith(".png") || path.endsWith(".gif");
    }

}

