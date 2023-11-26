package supernotes.notes;

import java.awt.*;

public class ImageNote implements Note<byte[]> {
    private byte[] content;
    private String tag;

    public ImageNote() {}

    public ImageNote(byte[] content, String tag)
    {
        this.content = content;
        this.tag = tag;
    }

    @Override
    public byte[] getContent() {
        return content;
    }

    @Override
    public void setContent(byte[] content) {
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
