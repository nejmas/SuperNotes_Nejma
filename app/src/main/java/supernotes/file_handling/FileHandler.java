package supernotes.file_handling;

public interface FileHandler {
    void exportPdfFile(String filePath, String tag);
    void exportPdfFileUsingFilter(String filePath, String filter);
    void exportToText(String filePath);
}
