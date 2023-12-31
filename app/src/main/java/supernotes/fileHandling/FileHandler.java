package supernotes.fileHandling;

import java.util.List;

import supernotes.notes.Note;

public interface FileHandler {
    void exportPdfFile(String filePath, String tag);
    void exportPdfFileUsingFilter(String filePath, String filter);
    void exportToText(String filePath);
}
