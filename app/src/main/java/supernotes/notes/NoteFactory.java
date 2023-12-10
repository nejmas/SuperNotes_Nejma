package supernotes.notes;

public interface NoteFactory {
    Note createNote(String title, String noteContent, String tag, String parentPageId, String pageId);
}
