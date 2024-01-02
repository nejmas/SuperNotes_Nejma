package supernotes.management;
import supernotes.notes.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface DBManager {
    void createNotesTable();
    void addTextNote(String title, String content, String tag, String parent_page_id, String page_id);
    void addTextNote(String title, String type, String content, String tag, String parent_page_id, String page_id, String time);
    void addImageNote(String title, byte[] imageBytes, String tag, String parent_page_id, String page_id);
    void addImageNote(String title, String type, byte[] imageBytes, String tag, String parent_page_id, String page_id, String time, String path);
    void createRemindersTable();
    void addReminder(int noteId, LocalDateTime reminderDateTime);
    List<LocalDateTime> getReminders(int noteId);
    boolean deleteRemindersByNoteId(int noteId);

    int addTextNote(String title, String content, String tag, String parent_page_id, String page_id);
    int addImageNote(String title, byte[] imageBytes, String tag, String parent_page_id, String page_id);
    void deleteNoteByTag(String tag);

    ArrayList<Note> getAllNotes();
    ArrayList<Note> getAllNotesByTag(String tag);
    ArrayList<Note> getAllNotesLike(String contentMotif);

    String getParentPageId();
    String getPageId(String Content);
    void updateNoteContentInDB(String pageId, String newContent);
    boolean doesNoteExist(String pageId);
}
