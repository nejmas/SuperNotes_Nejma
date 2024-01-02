package supernotes.notes;

import java.util.List;


public interface Note<T> {
    int getId();
    void setId(int id);
    
    String getTitle();
    void setTitle(String title); 

    T getContent();
    void setContent(T content);

    String getTag();
    void setTag(String content); 

    String getPageId();
    void setPageId(String pageId); 

    
    String getParentPageId();

    void setParentPageId(String pageId);


    String getTime();
    void setTime(String newTime);

    String getType();


    void setParentPageId(String pageId); 


}
