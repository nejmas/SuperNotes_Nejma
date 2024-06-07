package supernotes.translationAPI;

public class TranslationPageManager implements TranslationManager {

    private final TranslationApiManager translationApiManager;

    public TranslationPageManager(TranslationApiManager translationApiManager) {
        this.translationApiManager = translationApiManager;
    }

    @Override
    public String translateText(String text, String targetLanguage, String tag) {
        return translationApiManager.translateText(text, targetLanguage, tag);
    }

    @Override
    public String translateNote(int noteId, String targetLanguage, String tag) {
        return translationApiManager.translateNote(noteId, targetLanguage, tag);
    }
}
