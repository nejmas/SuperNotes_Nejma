package supernotes.notes;

import supernotes.helpers.ImageUtils;

public class ImageNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String title, String noteContent, String tag, String parentPageId, String pageId) {

        byte[] imageBytes = ImageUtils.imageToByteArray(noteContent);

        return new ImageNote(title, imageBytes, tag, parentPageId, pageId);
    }
}