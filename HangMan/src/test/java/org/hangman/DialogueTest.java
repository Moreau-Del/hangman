package org.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueTest {

    // старт диалога
    @Test
    void startShowsInitialState() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.start();

        assertTrue(response.contains("Добро пожаловать"));
        assertTrue(response.contains("Слово: _ _ _"));
        assertTrue(response.contains("Ошибки: 0/6"));
        assertTrue(response.contains("Введите букву или слово"));
        assertTrue(dialogue.isActive());
    }

    //––––––––––––––––––––––––
    // типы корректного ввода
    //––––––––––––––––––––––––

    // верная буква
    @Test
    void correctLetterIsProcessed() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("к");

        assertTrue(response.contains("Правильная буква!"));
        assertTrue(response.contains("Слово: к _ _"));
        assertTrue(response.contains("Ошибки: 0/6"));
        assertTrue(dialogue.isActive());
    }

    // неверная буква
    @Test
    void incorrectLetterIsProcessed() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("а");

        assertTrue(response.contains("Неверная буква."));
        assertTrue(response.contains("Ошибки: 1/6"));
        assertTrue(dialogue.isActive());
    }

    // правильное слово
    @Test
    void correctWordFinishesDialogue() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("кот");

        assertTrue(response.contains("Поздравляем! Вы победили!"));
        assertFalse(dialogue.isActive());
    }

    // неправильное слово
    @Test
    void incorrectWordDoesNotFinishDialogue() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("дом");

        assertTrue(response.contains("Неверное слово."));
        assertTrue(response.contains("Ошибки: 1/6"));
        assertTrue(dialogue.isActive());
    }

    // буква использовалась
    @Test
    void repeatedLetterDoesNotFinishDialogue() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        dialogue.handleMessage("к");
        String response = dialogue.handleMessage("к");

        assertTrue(dialogue.isActive());
        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals(0, game.getMistakes());
        assertTrue(response.contains("Эта буква уже использовалась."));
    }

    // некорректный ввод
    @Test
    void invalidInputDoesNotChangeGameState() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("123");

        assertTrue(response.contains("Некорректный ввод."));
        assertTrue(response.contains("Слово: _ _ _"));
        assertTrue(response.contains("Ошибки: 0/6"));
        assertTrue(dialogue.isActive());
    }

    // лимит ошибок
    @Test
    void sixthMistakeEndsGameWithLoss() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        String response = dialogue.handleMessage("е");

        assertEquals(GameState.LOST, game.getState());
        assertFalse(dialogue.isActive());
        assertEquals(6, game.getMistakes());
        assertTrue(response.contains("Вы проиграли."));
    }

    // победа завершает диалог
    @Test
    void winningEndsDialogue() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        dialogue.handleMessage("к");
        dialogue.handleMessage("о");
        String response = dialogue.handleMessage("т");

        assertEquals(GameState.WON, game.getState());
        assertFalse(dialogue.isActive());
        assertTrue(response.contains("Поздравляем! Вы победили!"));
    }

    // поражение завершает диалог
    @Test
    void messageAfterLossDoesNotContinueGame() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        dialogue.handleMessage("е");

        String response = dialogue.handleMessage("к");

        assertEquals(GameState.LOST, game.getState());
        assertFalse(dialogue.isActive());
        assertEquals(6, game.getMistakes());
        assertTrue(response.contains("Вы проиграли."));
    }

    // вывод после поражения
    @Test
    void secretWordIsShownAfterLoss() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        String response = dialogue.handleMessage("е");

        assertEquals(GameState.LOST, game.getState());
        assertTrue(response.contains("Загаданное слово: кот"));
    }

    // работа с регистром
    @Test
    void uppercaseLetterIsNormalized() {
        HangmanGame game = new HangmanGame("кот");
        Dialogue dialogue = new Dialogue(game);

        String response = dialogue.handleMessage("К");

        assertEquals(GameState.IN_PROGRESS, game.getState());
        assertEquals(0, game.getMistakes());
        assertTrue(response.contains("Правильная буква!"));
        assertTrue(response.contains("к _ _"));
    }
}
