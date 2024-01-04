package supernotes.notes;

import supernotes.helpers.ImageUtils;

public class ImageNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String noteContent, String tag, String parentPageId, String pageId) {

        byte[] imageBytes = ImageUtils.imageToByteArray(noteContent);

        return new ImageNote(noteContent, imageBytes, tag, parentPageId, pageId);
    }
}
