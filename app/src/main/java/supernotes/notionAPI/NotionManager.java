package supernotes.notionAPI;

public interface NotionManager {
    String extractNewPageId(String newPage);
    String extractParentPageId(String notionPage);
    String extractPageTitle(String notionPage);

}
