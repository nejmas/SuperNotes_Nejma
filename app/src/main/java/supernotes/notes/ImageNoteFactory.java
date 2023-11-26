package supernotes.notes;

import supernotes.helpers.ImageUtils;

public class ImageNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String noteContent, String tag) {

        byte[] imageBytes = ImageUtils.imageToByteArray(noteContent);

        return new ImageNote(imageBytes, tag);
    }
}