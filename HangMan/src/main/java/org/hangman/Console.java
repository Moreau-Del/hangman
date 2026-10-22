package org.hangman;

import java.util.Scanner;

public class Console {
    private Scanner scanner;
    private DialogueManager dialogueManager;

    public Console(DialogueManager dialogueManager) {
        this.dialogueManager = dialogueManager;
        this.scanner = new Scanner(System.in);
    }

    private String readInput() {
        return scanner.nextLine();
    }

    public void start() {
        Dialogue dialogue = dialogueManager.getDialogue("console");

        System.out.println(dialogue.start());

        while (true) {
            String input = readInput();
            String response = dialogue.handleMessage(input);
            System.out.println(response);
        }
    }
}