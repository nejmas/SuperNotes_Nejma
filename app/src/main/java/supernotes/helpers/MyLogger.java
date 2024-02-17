package supernotes.helpers;

public class MyLogger {
    public static MyLogger instance;

    private MyLogger()
    {
    }

    public static MyLogger getInstance()
    {
        if(instance == null)
        {
            instance = new MyLogger();
        }

        return instance;
    }

    public void logInfo(String message)
    {
        System.out.println(message);
    }

    public void logError(String message)
    {
        System.out.println(message);
    }
}
