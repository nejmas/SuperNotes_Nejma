package supernotes.management;

import supernotes.notes.Note;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface NoteManager{
    int addNote(Note note);
    ArrayList<Note> getByTag(String tag);
    void deleteByTag(String tag);
    String getParentPageId();
    String getPageId(String Content);
    void updateNoteContentInDB(String pageId, String newContent);
    boolean doesNoteExist(String pageId);
    void addReminder(int noteId, LocalDateTime reminderDateTime);
    List<LocalDateTime> getReminders(int noteId);
    boolean deleteRemindersByNoteId(int noteId);
    void addNoteWithReminderToCalendar(String noteContent, String reminderDateTime);


}
