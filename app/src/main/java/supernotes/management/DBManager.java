package supernotes.management;
import supernotes.notes.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface DBManager {
    void createNotesTable();
    int addTextNote(String title, String content, String tag, String parent_page_id, String page_id, String time);
    int addImageNote(String title, byte[] imageBytes, String tag, String parent_page_id, String page_id, String time, String path);
    void createRemindersTable();
    void addReminder(int noteId, LocalDateTime reminderDateTime);
    List<LocalDateTime> getReminders(int noteId);
    boolean deleteRemindersByNoteId(int noteId);

    void deleteNoteByTag(String tag);

    ArrayList<Note> getAllNotes();
    ArrayList<Note> getAllNotesByTag(String tag);
    ArrayList<Note> getAllNotesLike(String contentMotif);

    String getParentPageId();
    String getPageId(String Content);
    void updateNoteContentInDB(String pageId, String newContent);
    boolean doesNoteExist(String pageId);
}
