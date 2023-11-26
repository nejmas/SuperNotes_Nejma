package supernotes.notes;

public class TextNote implements Note<String> {
    private String content;
    private String tag;

    public TextNote() {}

    public TextNote(String content, String tag)
    {
        this.content = content;
        this.tag = tag;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
