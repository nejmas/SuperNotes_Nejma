import * as vscode from 'vscode';
//import * as child_process from 'child_process';

export function activate(context: vscode.ExtensionContext) {
	
  let disposable = vscode.commands.registerCommand('jarunner.JarFileRun', () => {
    // Get the path to the JAR file from user input
    
        // Open a new terminal
        const terminal = vscode.window.createTerminal('My JAR Terminal');
        
        // Command to run the JAR file
        const command = `java -jar "${'/home/aya/Desktop/app-all.jar'}"`;

        // Change this to the appropriate path if needed
        terminal.sendText(command);

        // Show the terminal
        terminal.show();
      
    
  });

  context.subscriptions.push(disposable);
}


// This method is called when your extension is deactivated
export function deactivate() {}
