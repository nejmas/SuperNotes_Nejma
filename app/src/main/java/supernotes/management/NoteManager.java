package supernotes.management;

import supernotes.notes.Note;
import java.util.List;
import java.util.function.Predicate;

public interface NoteManager{
    void addNote(Note note);
    void deleteByTag(String tag);
}
