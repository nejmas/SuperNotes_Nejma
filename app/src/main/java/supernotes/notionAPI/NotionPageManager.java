package supernotes.notionAPI;
import org.json.*;
import org.json.JSONObject;
import org.json.JSONException;

public class NotionPageManager implements NotionManager{

    public String extractNewPageId(String newPage) {
        try {
            JSONObject jsonResponse = new JSONObject(newPage);
            return jsonResponse.optString("id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractParentPageId(String notionPage) {
        try {
            JSONObject jsonResponse = new JSONObject(notionPage);
            JSONObject parentObject = jsonResponse.getJSONObject("parent");
            return parentObject.getString("page_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String extractPageTitle(String notionPage) {
        try {
            JSONObject jsonResponse = new JSONObject(notionPage);
            JSONObject properties = jsonResponse.getJSONObject("properties");
            JSONObject titleObject = properties.getJSONObject("title");
            JSONArray titleArray = titleObject.getJSONArray("title");
            JSONObject textObject = titleArray.getJSONObject(0);
            return textObject.getJSONObject("text").getString("content");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
