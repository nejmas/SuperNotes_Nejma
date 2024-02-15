package supernotes.notionAPI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.http.entity.StringEntity;


import java.io.IOException;
import java.util.Scanner;

public class NotionAPI implements NotionApiManager{
    private static final Logger LOGGER = LoggerFactory.getLogger(NotionAPI.class);
    public static final String BASEURL = "https://api.notion.com/v1/";
    private final HttpClient httpClient;
    private String apiKey; 
    
    public NotionAPI() {
        this.httpClient = HttpClients.createDefault();
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String createNotionPage(String parentPageId, String propertiesJson) {

        if (apiKey == null || apiKey.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            LOGGER.info("Veuillez saisir votre clé API Notion : ");
            String userApiKey = scanner.nextLine();
            setApiKey(userApiKey);
        }

        String endpoint = "pages";
        HttpPost request = new HttpPost(BASEURL + endpoint);

        request.addHeader("Authorization", "Bearer " + apiKey);
        request.addHeader("Notion-Version", "2021-05-13");
        request.addHeader("Content-Type", "application/json");

        try {
            // Créez le JSON pour les propriétés de la nouvelle page
            StringEntity requestBody = new StringEntity(propertiesJson);
            request.setEntity(requestBody);

            // Exécution de la requête et récupération de la réponse
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                System.err.println("Erreur lors de la création de la note sur notion: " + statusCode);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Une erreur s'est produite.", e);
            return null;
        }
    }

    public String retrievePageContent(String pageId) {
        if (apiKey == null || apiKey.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            LOGGER.info("Veuillez saisir votre clé API Notion : ");
            String userApiKey = scanner.nextLine();
            setApiKey(userApiKey);
        }

        String endpoint = "pages/" + pageId;
        HttpGet request = new HttpGet(BASEURL + endpoint);

        request.addHeader("Authorization", "Bearer " + apiKey);
        request.addHeader("Notion-Version", "2021-05-13");

        try {
            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                System.err.println("Erreur lors de la récupération de la page: " + statusCode);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Une erreur s'est produite.", e);
            return null; 
        }
    }

    public String updatePageProperties(String pageId, String propertiesJson) {

        if (apiKey == null || apiKey.isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            LOGGER.info("Veuillez saisir votre clé API Notion : ");
            String userApiKey = scanner.nextLine();
            setApiKey(userApiKey);
        }

        String endpoint = "pages/" + pageId;
        HttpPatch request = new HttpPatch(BASEURL + endpoint);

        request.addHeader("Authorization", "Bearer " + apiKey);
        request.addHeader("Notion-Version", "2021-05-13");
        request.addHeader("Content-Type", "application/json");

        try {
            StringEntity requestBody = new StringEntity(propertiesJson);
            request.setEntity(requestBody);

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                System.err.println("Erreur lors de la mise à jour de la page: " + statusCode);
                return null;
            }
        } catch (IOException e) {
            LOGGER.error("Une erreur s'est produite.", e);
            return null;
        }
    }

}




