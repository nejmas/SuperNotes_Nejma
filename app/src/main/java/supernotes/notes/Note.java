package supernotes.notes;

public class Note {
    private int id;
    private String title;
    private String content;
    private String path;
    private static int count = 0;

    //default constructor
    public Note(String title, String content) {
        this.id = count++;
        this.title = title;
        this.content = content;
    }
    
    public Note(String title, String content, String path) {
        this.id = count++;
        this.title = title;
        this.content = content;
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPath() {
        return path;
    }
}