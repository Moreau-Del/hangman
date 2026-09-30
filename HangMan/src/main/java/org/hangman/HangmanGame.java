package org.hangman;

import java.util.Set;
import java.util.HashSet;

/**
 * Представляет одну игровую сессию в "Виселице".
 * Хранит состояние игры и обрабатывает попытки пользователя
 * угадать букву или слово.
 */
public class HangmanGame {
    private String secretWord;
    private Set<Character> usedLetters = new HashSet<>();
    private int mistakes = 0;
    private int maxMistakes = 6;
    private GameState state = GameState.IN_PROGRESS;

    public HangmanGame(String secretWord) {
        this.secretWord = secretWord;
    }

    public GuessResult guessLetter(char letter){
        // Если игра уже закончена, новый ход не обрабатываем
        if (state == GameState.WON) {
            return GuessResult.WON;
        }

        if (state == GameState.LOST) {
            return GuessResult.LOST;
        }

        // Проверяем, не использовал ли игрок эту букву раньше
        if (usedLetters.contains(letter)) {
            return GuessResult.ALREADY_USED;
        }

        // Запоминаем новую букву
        usedLetters.add(letter);

        // Проверяем, есть ли буква в загаданном слове
        if (secretWord.indexOf(letter) >= 0) {

            // Если после этой буквы всё слово открыто — победа
            if (isWordGuessed()) {
                state = GameState.WON;
                return GuessResult.WON;
            }

            return GuessResult.CORRECT;
        }

        // Буквы нет в слове
        mistakes++;

        // Если закончились попытки — поражение
        if (mistakes >= maxMistakes) {
            state = GameState.LOST;
            return GuessResult.LOST;
        }

        return GuessResult.INCORRECT;
    }

    private boolean isWordGuessed() {
        for (char letter : secretWord.toCharArray()) {
            if (!usedLetters.contains(letter)) {
                return false;
            }
        }

        return true;
    }


    public GuessResult guessWord(String word) {
        if (state == GameState.WON) {
            return GuessResult.WON;
        }

        if (state == GameState.LOST) {
            return GuessResult.LOST;
        }

        if (secretWord.equals(word)) {
            state = GameState.WON;
            return GuessResult.WON;
        }

        mistakes++;

        if (mistakes >= maxMistakes) {
            state = GameState.LOST;
            return GuessResult.LOST;
        }

        return GuessResult.INCORRECT;
    }

    public String getMaskedWord() {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < secretWord.length(); i++) {
            char letter = secretWord.charAt(i);

            if (usedLetters.contains(letter)) {
                result.append(letter);
            } else {
                result.append('_');
            }

            if (i < secretWord.length() - 1) {
                result.append(' ');
            }
        }

        return result.toString();
    }

    public int getMistakes(){
        return mistakes;
    }

    public GameState getState(){
        return state;
    }
}


