package org.hangman;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HangmanGameTest {

    // ===== guessLetter() =====

    @Test
    void correctLetter() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult result = game.guessLetter('о');

        assertEquals(GuessResult.CORRECT, result);
        assertEquals(0, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals("_ о _", game.getMaskedWord());
    }

    @Test
    void incorrectLetter() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult result = game.guessLetter('а');

        assertEquals(GuessResult.INCORRECT, result);
        assertEquals(1, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals("_ _ _", game.getMaskedWord());
    }

    @Test
    void repeatedCorrectLetter() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult firstResult = game.guessLetter('о');
        GuessResult secondResult = game.guessLetter('о');

        assertEquals(GuessResult.CORRECT, firstResult);
        assertEquals(GuessResult.ALREADY_USED, secondResult);
        assertEquals(0, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals("_ о _", game.getMaskedWord());
    }

    @Test
    void repeatedIncorrectLetter() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult firstResult = game.guessLetter('а');
        GuessResult secondResult = game.guessLetter('а');

        assertEquals(GuessResult.INCORRECT, firstResult);
        assertEquals(GuessResult.ALREADY_USED, secondResult);
        assertEquals(1, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals("_ _ _", game.getMaskedWord());
    }

    @Test
    void repeatedLetterOccurrences() {
        HangmanGame game = new HangmanGame("корова");

        GuessResult result = game.guessLetter('о');

        assertEquals(GuessResult.CORRECT, result);
        assertEquals(0, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals("_ о _ о _ _", game.getMaskedWord());
    }

    @Test
    void lastNecessaryLetter() {
        HangmanGame game = new HangmanGame("кот");

        game.guessLetter('к');
        game.guessLetter('о');
        GuessResult result = game.guessLetter('т');

        assertEquals(GuessResult.WON, result);
        assertEquals(GameState.WON, game.getState());
    }

    @Test
    void maxMistakesReached() {
        HangmanGame game = new HangmanGame("кот");

        game.guessLetter('а');
        game.guessLetter('б');
        game.guessLetter('в');
        game.guessLetter('г');
        game.guessLetter('д');
        GuessResult result = game.guessLetter('е');

        assertEquals(GuessResult.LOST, result);
        assertEquals(GameState.LOST, game.getState());
        assertEquals(6, game.getMistakes());
    }

    @Test
    void guessLetterAfterGameEnds() {
        // Сценарий WON
        HangmanGame wonGame = new HangmanGame("кот");

        wonGame.guessLetter('к');
        wonGame.guessLetter('о');
        wonGame.guessLetter('т');

        int mistakesAfterWin = wonGame.getMistakes();

        GuessResult resultAfterWin = wonGame.guessLetter('а');

        assertEquals(GuessResult.WON, resultAfterWin);
        assertEquals(GameState.WON, wonGame.getState());
        assertEquals(mistakesAfterWin, wonGame.getMistakes());

        // Сценарий LOST
        HangmanGame lostGame = new HangmanGame("кот");

        lostGame.guessLetter('а');
        lostGame.guessLetter('б');
        lostGame.guessLetter('в');
        lostGame.guessLetter('г');
        lostGame.guessLetter('д');
        lostGame.guessLetter('е');

        int mistakesAfterLoss = lostGame.getMistakes();

        GuessResult resultAfterLoss = lostGame.guessLetter('ж');

        assertEquals(GuessResult.LOST, resultAfterLoss);
        assertEquals(GameState.LOST, lostGame.getState());
        assertEquals(mistakesAfterLoss, lostGame.getMistakes());
    }

    // ===== guessWord() =====

    @Test
    void correctWord() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult result = game.guessWord("кот");

        assertEquals(GuessResult.WON, result);
        assertEquals(GameState.WON, game.getState());
        assertEquals(0, game.getMistakes());
    }

    @Test
    void incorrectWord() {
        HangmanGame game = new HangmanGame("кот");

        GuessResult result = game.guessWord("дом");

        assertEquals(GuessResult.INCORRECT, result);
        assertEquals(1, game.getMistakes());
        assertEquals(GameState.IN_PROGRESS, game.getState());
    }

    @Test
    void maxMistakesWithLettersAndWords() {
        HangmanGame game = new HangmanGame("кот");

        game.guessLetter('а');   // 1-я ошибка
        game.guessWord("дом");   // 2-я ошибка
        game.guessLetter('б');   // 3-я ошибка
        game.guessWord("лес");   // 4-я ошибка
        game.guessLetter('в');   // 5-я ошибка

        GuessResult result = game.guessWord("мир"); // 6-я ошибка

        assertEquals(GuessResult.LOST, result);
        assertEquals(6, game.getMistakes());
        assertEquals(GameState.LOST, game.getState());
    }

    @Test
    void guessWordAfterGameEnds() {
        // Сценарий WON
        HangmanGame wonGame = new HangmanGame("кот");

        wonGame.guessWord("кот");

        int mistakesAfterWin = wonGame.getMistakes();

        GuessResult resultAfterWin = wonGame.guessWord("дом");

        assertEquals(GuessResult.WON, resultAfterWin);
        assertEquals(GameState.WON, wonGame.getState());
        assertEquals(mistakesAfterWin, wonGame.getMistakes());

        // Сценарий LOST
        HangmanGame lostGame = new HangmanGame("кот");

        lostGame.guessWord("дом");
        lostGame.guessWord("лес");
        lostGame.guessWord("мир");
        lostGame.guessWord("сыр");
        lostGame.guessWord("мак");
        lostGame.guessWord("лук");

        int mistakesAfterLoss = lostGame.getMistakes();

        GuessResult resultAfterLoss = lostGame.guessWord("кот");

        assertEquals(GuessResult.LOST, resultAfterLoss);
        assertEquals(GameState.LOST, lostGame.getState());
        assertEquals(mistakesAfterLoss, lostGame.getMistakes());
    }

    // ===== getMaskedWord() =====

    @Test
    void maskedWordAtStart() {
        HangmanGame game = new HangmanGame("корова");

        assertEquals("_ _ _ _ _ _", game.getMaskedWord());
    }

    @Test
    void maskedWordAfterSeveralLetters() {
        HangmanGame game = new HangmanGame("корова");

        game.guessLetter('к');
        game.guessLetter('о');
        game.guessLetter('в');

        assertEquals("к о _ о в _", game.getMaskedWord());
    }

    @Test
    void maskedWordAfterFullGuess() {
        HangmanGame game = new HangmanGame("корова");

        game.guessLetter('к');
        game.guessLetter('о');
        game.guessLetter('р');
        game.guessLetter('в');
        game.guessLetter('а');

        assertEquals("к о р о в а", game.getMaskedWord());
    }

    // ===== Полные игровые сценарии =====

    @Test
    void winningGameScenario() {
        HangmanGame game = new HangmanGame("корова");

        game.guessLetter('к');       // К _ _ _ _ _
        game.guessLetter('а');       // К _ _ _ _ А
        game.guessLetter('о');       // К О _ О _ А
        game.guessLetter('м');       // ошибка
        game.guessWord("собака");    // ошибка
        game.guessLetter('в');       // К О _ О В А
        GuessResult result = game.guessLetter('р'); // К О Р О В А → победа

        assertEquals(GuessResult.WON, result);
        assertEquals(GameState.WON, game.getState());
        assertEquals(2, game.getMistakes());
        assertEquals("к о р о в а", game.getMaskedWord());
    }

    @Test
    void losingGameScenario() {
        HangmanGame game = new HangmanGame("корова");

        game.guessLetter('о');       // правильная буква
        game.guessLetter('м');       // 1 ошибка
        game.guessLetter('б');       // 2
        game.guessWord("собака");    // 3
        game.guessLetter('д');       // 4
        game.guessWord("машина");    // 5
        GuessResult result = game.guessLetter('е'); // 6 → поражение

        assertEquals(GuessResult.LOST, result);
        assertEquals(GameState.LOST, game.getState());
        assertEquals(6, game.getMistakes());
        assertEquals("_ о _ о _ _", game.getMaskedWord());
    }
}
