package org.hangman;

public class GameFactory {

    private final WordRepository wordRepository;

    public GameFactory(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    public HangmanGame createGame() {
        String word = wordRepository.getRandomWord();
        return new HangmanGame(word);
    }
}