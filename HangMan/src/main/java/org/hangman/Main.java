package org.hangman;

public class Main {
    public static void main(String[] args) {
        WordRepository repository = new WordRepository();
        String word = repository.getRandomWord();

        HangmanGame game = new HangmanGame(word);
        Console console = new Console(game);

        console.startGame();
    }
}