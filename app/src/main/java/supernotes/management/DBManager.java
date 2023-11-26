package supernotes.management;
import supernotes.notes.Note;

import java.util.ArrayList;

public interface DBManager {
    void createNotesTable();
    void addTextNote(String content, String tag);
    void addImageNote(byte[] imageBytes, String tag);
    void deleteNoteByTag(String tag);
    ArrayList<Note> getAllNotes();
    ArrayList<Note> getAllNotesByTag(String tag);
    ArrayList<Note> getAllNotesLike(String contentMotif);
}
