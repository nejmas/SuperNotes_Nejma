package supernotes.helpers;

import java.io.IOException;
import java.util.Scanner;

public class InputScanner {
    private static Scanner instance;
    private InputScanner(){
    }

    public static Scanner getInstance(){
        if(instance == null){
            return new Scanner(System.in);
        }

        return instance;
    }

    public static void setInstance(Scanner scanner) {
        instance = scanner;
    }
}
