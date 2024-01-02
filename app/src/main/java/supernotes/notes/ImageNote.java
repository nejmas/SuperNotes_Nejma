package supernotes.notes;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ImageNote implements Note<byte[]> {
    private int id;
    private String title;
    private String type;
    private byte[] content;
    private String tag;
    private String pageId;
    private String parentPageId;
    private String time;
    private String path;


    public ImageNote(String title, String type, byte[] content, String tag, String pageId, String parentPageId, String time, String path) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.pageId = pageId;
        this.type = type;
        this.parentPageId = parentPageId;
        this.time = time;
        this.path = path;
    }

    public ImageNote() {
    }

    public ImageNote(String title, byte[] content, String tag, String parentPageId, String pageId) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.parentPageId = parentPageId;
        this.pageId = pageId;
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy h:mm a");
        this.time = dateTime.format(formatter);
    }


    public ImageNote() {}

    public ImageNote(String title, byte[] content, String tag, String pageId, String parentPageId, String time) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.pageId = pageId;
        this.parentPageId = parentPageId;
        this.time = time;
    }

    public ImageNote(String title, String path, byte[] imageBytes, String tag, String parentPageId, String pageId) {
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.pageId = pageId;
        this.parentPageId = parentPageId;
        this.time = time;
        this.path = path;

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
        return parentPageId;
    }

    @Override
    public void setParentPageId(String parentPageId) {
        this.parentPageId = parentPageId;
    }

    @Override
    public String getTime() {
        return time;
    }

    @Override
    public void setTime(String newTime) {
        this.time = newTime;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getPath() {
        return path;
    }

}
