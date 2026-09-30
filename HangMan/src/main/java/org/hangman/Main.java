package org.hangman;

public class Main {
    public static void main(String[] args) {
        HangmanGame game = new HangmanGame("корова");
        Console console = new Console(game);

        console.startGame();
    }
}