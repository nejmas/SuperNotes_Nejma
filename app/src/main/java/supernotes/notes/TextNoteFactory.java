package supernotes.notes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class TextNoteFactory implements NoteFactory {

    @Override
    public Note createNote(String noteContent, String tag, String parentPageId, String pageId) {
        
        return new TextNote(noteContent, tag, parentPageId, pageId);
    }

}