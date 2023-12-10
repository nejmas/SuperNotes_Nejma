package supernotes;

import supernotes.fileHandling.*;
import supernotes.management.*;
import supernotes.notes.*;
import supernotes.notionAPI.NotionAPI;
import supernotes.notionAPI.NotionPageManager;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        FileManager fileManager = new FileManager();
        CommandLineInterface commandLineInterface = new CommandLineInterface(new TextNoteFactory(), new ImageNoteFactory(), fileManager, new NotionPageManager(), new NotionAPI());
        String command = "";
        do {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Veuillez entrer votre commande : ");
            command = scanner.nextLine();

            commandLineInterface.parseCommand(command);

        } while (!command.equals("exit"));
    }
}
