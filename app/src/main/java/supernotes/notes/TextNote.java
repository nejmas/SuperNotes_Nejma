package supernotes.notes;

import java.util.ArrayList;
import java.util.List;

public class TextNote implements Note<String> {
    private int id;
    private String title;
    private String content;
    private String tag;
    private String ParentPageId;
    private String pageId;
    

    public TextNote() {}

    public TextNote(String title, String content, String tag, String ParentPageId, String pageId)
    {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.ParentPageId = ParentPageId;
        this.pageId = pageId;
        // this.reminders = new ArrayList<>();
    }

    @Override 
    public int getId() {
        return this.id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public String getPageId() {
        return pageId;
    }

    @Override
    public void setPageId(String pageId) {
        this.pageId = pageId;
    }

    @Override
    public String getParentPageId() {
        return ParentPageId;
    }

    @Override
    public void setParentPageId(String ParentPageId) {
        this.ParentPageId = ParentPageId;
    }
}
