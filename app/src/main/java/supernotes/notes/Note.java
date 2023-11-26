package supernotes.notes;

public interface Note<T> {
    T getContent();
    void setContent(T content);

    String getTag();
    void setTag(String content); 
}
