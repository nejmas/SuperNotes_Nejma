package supernotes.notes;

public class TextNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String noteContent, String tag) {
        return new TextNote(noteContent, tag);
    }
}