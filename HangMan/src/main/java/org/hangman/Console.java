package org.hangman;

import java.util.Scanner;

public class Console {
    private Scanner scanner;
    private Dialogue dialogue;

    public Console(Dialogue dialogue) {
        this.dialogue = dialogue;
        this.scanner = new Scanner(System.in);
    }

    private String readInput() {

        return scanner.nextLine();
    }

    public void startGame() {
        System.out.println(dialogue.start());

        while (dialogue.isActive()) {
            String input = readInput();
            String response = dialogue.handleMessage(input);
            System.out.println(response);
        }
    }
}
