package supernotes.notes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class TextNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String title, String noteContent, String tag, String parentPageId, String pageId) {
        return new TextNote(title, noteContent, tag, parentPageId, pageId);
    }

}