package supernotes.management;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;
import supernotes.reminders.GoogleCalendarReminder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class NoteManagerDataBase implements NoteManager {
    DBManager dbManager;

    public NoteManagerDataBase(){
        dbManager = new SQLiteDBManager();
    }

    @Override
    public int addNote(Note note) {
        int noteId = -1;
        if (note instanceof TextNote) {
            String title = note.getTitle();
            String noteContent = ((TextNote) note).getContent();
            String noteTag = note.getTag();
            String parent_page_id = note.getParentPageId();
            String page_id = note.getPageId();
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a");
            String time = dateTime.format(formatter);

            dbManager.addTextNote(title, "text", noteContent, noteTag, parent_page_id, page_id, time);
        }
        else if (note instanceof ImageNote){
    
            noteId = dbManager.addTextNote(title, noteContent, noteTag, parent_page_id, page_id);
            note.setId(noteId);

        } else if (note instanceof ImageNote) {
            String title = note.getTitle();
            var imageContent = ((ImageNote) note).getContent();
            String noteTag = note.getTag();
            String parent_page_id = note.getParentPageId();
            String page_id = note.getPageId();
            LocalDateTime dateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a");
            String time = dateTime.format(formatter);
            String type = "image";
            String path = note.getPath();

            dbManager.addImageNote(title, "image", imageContent, noteTag, parent_page_id, page_id, time, path);

    
            noteId = dbManager.addImageNote(title, imageContent, noteTag, parent_page_id, page_id);
            note.setId(noteId);

        }
        return noteId;
    }

    
    @Override
    public void addReminder(int noteId, LocalDateTime reminderDateTime) {
        dbManager.addReminder(noteId, reminderDateTime);
    }

    @Override
    public List<LocalDateTime> getReminders(int noteId) {
        return dbManager.getReminders(noteId);
    }

    @Override
    public void deleteByTag(String tag)
    {
        dbManager.deleteNoteByTag(tag);
    }


    @Override
    public ArrayList<Note> getByTag(String tag)
    {
        return dbManager.getAllNotesByTag(tag);
    }

    @Override
    public String getParentPageId()
    {
        return dbManager.getParentPageId();
    }

    @Override
    public String getPageId(String Content)
    {
        return dbManager.getPageId(Content);
    }
    
    @Override
    public void updateNoteContentInDB(String pageId, String newContent)
    {
        dbManager.updateNoteContentInDB(pageId, newContent);
    }

    @Override
    public boolean doesNoteExist(String pageId)
    {
        return dbManager.doesNoteExist(pageId);
    }

    @Override
    public List<Note> showAllNotes() {
        return dbManager.getAllNotes();
    }

    public boolean deleteRemindersByNoteId(int noteId) {
        return dbManager.deleteRemindersByNoteId(noteId);
    }

    @Override
    public void addNoteWithReminderToCalendar(String noteContent, String reminderDateTime) {
        GoogleCalendarReminder.addEventToCalendar(noteContent, reminderDateTime);
    }

}
