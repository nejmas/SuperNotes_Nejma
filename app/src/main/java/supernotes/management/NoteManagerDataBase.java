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
            String title = note.getTitle();
            String noteContent = ((TextNote) note).getContent();
            String noteTag = note.getTag();
            String parent_page_id = note.getParentPageId();            
            String page_id = note.getPageId();

            dbManager.addTextNote(title, noteContent, noteTag, parent_page_id, page_id);
        }
        else if (note instanceof ImageNote){
            String title = note.getTitle();
            var imageContent = ((ImageNote) note).getContent();
            String noteTag = note.getTag();
            String parent_page_id = note.getParentPageId();     
            String page_id = note.getPageId();

            dbManager.addImageNote(title, imageContent, noteTag, parent_page_id, page_id);
        }
    }

    @Override
    public void deleteByTag(String tag)
    {
        dbManager.deleteNoteByTag(tag);
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
    
}
