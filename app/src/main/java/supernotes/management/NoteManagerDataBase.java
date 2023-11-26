package supernotes.management;

import supernotes.notes.ImageNote;
import supernotes.notes.Note;
import supernotes.notes.TextNote;

public class NoteManagerDataBase implements NoteManager {
    DBManager dbManager;

    public NoteManagerDataBase(){
        dbManager = new SQLiteDBManager();
    }

    @Override
    public void addNote(Note note) {
        if (note instanceof TextNote){
            String noteContent = ((TextNote) note).getContent();
            String noteTag = note.getTag();
            dbManager.addTextNote(noteContent, noteTag);
        }
        else if (note instanceof ImageNote){
            var imageContent = ((ImageNote) note).getContent();
            String noteTag = note.getTag();
            dbManager.addImageNote(imageContent, noteTag);
        }
    }

    @Override
    public void deleteByTag(String tag)
    {
        dbManager.deleteNoteByTag(tag);
    }
}
