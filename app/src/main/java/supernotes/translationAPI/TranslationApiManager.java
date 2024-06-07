package supernotes.translationAPI;

public interface TranslationApiManager {
    String translateText(String text, String targetLanguage, String tag);
    String translateNote(int noteId, String targetLanguage, String tag);
}
