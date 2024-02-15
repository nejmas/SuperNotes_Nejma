package supernotes.notes;


public class TextNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String noteContent, String tag, String parentPageId, String pageId) {
        
        return new TextNote(noteContent, tag, parentPageId, pageId);
    }

}