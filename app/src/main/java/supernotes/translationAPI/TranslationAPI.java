package supernotes.translationAPI;

import com.deepl.api.*;

import supernotes.management.NoteManager;
import supernotes.notes.Note;
import supernotes.notes.TextNoteFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.Scanner;

public class TranslationAPI implements TranslationApiManager {

    private final HttpClient httpClient;
    private String apiKey;
    private final NoteManager noteManager;

    public TranslationAPI(NoteManager noteManager) {
        this.httpClient = HttpClients.createDefault();
        this.noteManager = noteManager;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String translateText(String text, String targetLanguage, String tag) {
        if (apiKey == null || apiKey.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Veuillez saisir votre clé API DeepL : ");
            String userApiKey = scanner.nextLine();
            setApiKey(userApiKey);
        }

        try {
            Translator translator = new Translator(apiKey);
            TextResult result = translator.translateText(text, null, targetLanguage);
            String translatedText = result.getText();

            Note translatedNote = new TextNoteFactory().createNote(
                translatedText,
                tag,
                null,
                null
            );

            noteManager.addNote(translatedNote);

            return translatedText;
        } catch (Exception e) {
            System.err.println("Erreur lors de la traduction : " + e.getMessage());
            return null;
        }
    }
    
    public String translateNote(int noteId, String targetLanguage, String tag) {
        if (apiKey == null || apiKey.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Veuillez saisir votre clé API DeepL : ");
            String userApiKey = scanner.nextLine();
            setApiKey(userApiKey);
        }

        try {
            // Retrieve the note from the database using its ID
            Note note = noteManager.getNoteById(noteId);
            if (note == null) {
                System.err.println("Note not found.");
                return null;
            }

            String originalText = (String) note.getContent();

            // Translate the original text
            Translator translator = new Translator(apiKey);
            TextResult result = translator.translateText(originalText, null, targetLanguage);
            String translatedText = result.getText();

            // Create a translated note
            Note translatedNote = new TextNoteFactory().createNote(
                translatedText,
                tag,
                note.getParentPageId(),
                note.getPageId()
            );

            // Add the translated note to the database
            noteManager.addNote(translatedNote);

            return translatedText;
        } catch (Exception e) {
            System.err.println("Erreur lors de la traduction : " + e.getMessage());
            return null;
        }
    }
}
