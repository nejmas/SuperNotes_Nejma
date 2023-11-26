package supernotes;

import supernotes.fileHandling.*;
import supernotes.management.*;
import supernotes.notes.*;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        CommandLineInterface commandLineInterface = new CommandLineInterface(new TextNoteFactory(), new ImageNoteFactory(), fileManager);
        String command = "";
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Veuillez entrer votre commande : ");
            command = scanner.nextLine();

            commandLineInterface.parseCommand(command);

        } while (!command.equals("exit"));
    }
}
