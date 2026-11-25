package org.hangman;

import java.util.Scanner;
import java.io.PrintStream;

public class Console {
    private Scanner scanner;
    private PrintStream output;
    private DialogueManager dialogueManager;

    public Console(DialogueManager dialogueManager) {
        //this.dialogueManager = dialogueManager;
        //this.scanner = new Scanner(System.in);
        this(dialogueManager, new Scanner(System.in), System.out);
    }

    public Console(DialogueManager dialogueManager,
                   Scanner scanner,
                   PrintStream output) {
        this.dialogueManager = dialogueManager;
        this.scanner = scanner;
        this.output = output;
    }

    private String readInput() {

        return scanner.nextLine();
    }



    public void start() {
        Dialogue dialogue = dialogueManager.getDialogue("console");

        output.println(dialogue.start());

        while (true) {
            String input = readInput();
            String response = dialogue.handleMessage(input);
            output.println(response);
        }
    }
}