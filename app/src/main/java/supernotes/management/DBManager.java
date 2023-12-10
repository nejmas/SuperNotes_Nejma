package supernotes.management;
import supernotes.notes.Note;

import java.util.ArrayList;

public interface DBManager {
    void createNotesTable();
    void addTextNote(String title, String content, String tag, String parent_page_id, String page_id);
    void addImageNote(String title, byte[] imageBytes, String tag, String parent_page_id, String page_id);
    void deleteNoteByTag(String tag);
    ArrayList<Note> getAllNotes();
    ArrayList<Note> getAllNotesByTag(String tag);
    ArrayList<Note> getAllNotesLike(String contentMotif);
    String getParentPageId();
    String getPageId(String Content);
    void updateNoteContentInDB(String pageId, String newContent);
    boolean doesNoteExist(String pageId);
}
