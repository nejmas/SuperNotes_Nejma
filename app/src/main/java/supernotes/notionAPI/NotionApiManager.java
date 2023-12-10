package supernotes.notionAPI;

public interface NotionApiManager {
    String createNotionPage(String parentPageId, String propertiesJson);
    String retrievePageContent(String pageId);
    String updatePageProperties(String pageId, String propertiesJson);
}
