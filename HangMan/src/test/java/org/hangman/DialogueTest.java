package org.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueTest {

    // старт диалога
    @Test
    void startShowsInitialState() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);

        String response = dialogue.start();

        assertTrue(response.contains("Добро пожаловать"));
        assertTrue(response.contains("\\new"));
        assertTrue(response.contains("\\help"));
        assertEquals(DialogueState.CONVERSATION, dialogue.getState());
    }

    //––––––––––––––––––––––––
    // типы корректного ввода
    //––––––––––––––––––––––––

    // верная буква
    @Test
    void correctLetterIsProcessed() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("к");

        assertTrue(response.contains("Правильная буква!"));
        assertTrue(response.contains("Слово: к _ _"));
        assertTrue(response.contains("Ошибки: 0/6"));
    }

    // неверная буква
    @Test
    void incorrectLetterIsProcessed() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("а");

        assertTrue(response.contains("Неверная буква."));
        assertTrue(response.contains("Ошибки: 1/6"));
    }

    // правильное слово завершает игру (состояние game_over)
    @Test
    void correctWordFinishesGame() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("кот");

        assertTrue(response.contains("Поздравляем! Вы победили!"));
        assertEquals(DialogueState.GAME_OVER, dialogue.getState());
    }

    // неправильное слово
    @Test
    void incorrectWordDoesNotFinishDialogue() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("дом");

        assertTrue(response.contains("Неверное слово."));
        assertTrue(response.contains("Ошибки: 1/6"));
    }

    // буква использовалась
    @Test
    void repeatedLetterDoesNotFinishDialogue() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("к");
        String response = dialogue.handleMessage("к");

        assertTrue(response.contains("Эта буква уже использовалась."));
        assertTrue(response.contains("Ошибки: 0/6"));
    }

    // некорректный ввод
    @Test
    void invalidInputDoesNotChangeGameState() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("123");

        assertTrue(response.contains("Некорректный ввод."));
        assertTrue(response.contains("Слово: _ _ _"));
        assertTrue(response.contains("Ошибки: 0/6"));
    }

    // шестая ошибка завершает игру и переводит Dialogue в GAME_OVER
    @Test
    void sixthMistakeEndsGameWithLoss() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        String response = dialogue.handleMessage("е");

        assertTrue(response.contains("Вы проиграли."));
        assertEquals(DialogueState.GAME_OVER, dialogue.getState());
    }

    // после победы диалог продолжает принимать команды
    @Test
    void dialogueContinuesAfterWin() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("кот");

        String response = dialogue.handleMessage("\\help");

        assertEquals(DialogueState.GAME_OVER, dialogue.getState());
        assertTrue(response.contains("Доступные команды"));
    }

    // после поражения диалог продолжает принимать команды
    @Test
    void messageAfterLossDoesNotContinueGame() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        String lossResponse = dialogue.handleMessage("е");

        assertTrue(lossResponse.contains("Вы проиграли."));
        assertEquals(DialogueState.GAME_OVER, dialogue.getState());

        String response = dialogue.handleMessage("\\help");

        assertTrue(response.contains("Доступные команды"));
        assertEquals(DialogueState.GAME_OVER, dialogue.getState());
    }

    // вывод после поражения
    @Test
    void secretWordIsShownAfterLoss() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("а");
        dialogue.handleMessage("б");
        dialogue.handleMessage("в");
        dialogue.handleMessage("г");
        dialogue.handleMessage("д");
        String response = dialogue.handleMessage("е");

        assertTrue(response.contains("Загаданное слово: кот"));
    }

    // работа с регистром
    @Test
    void uppercaseLetterIsNormalized() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        String response = dialogue.handleMessage("К");

        assertTrue(response.contains("Правильная буква!"));
        assertTrue(response.contains("к _ _"));
    }

    // справка появляется, состояние не меняется
    @Test
    void helpCommandShowsInstructions() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);

        String response = dialogue.handleMessage("\\help");

        assertTrue(response.contains("Доступные команды"));
        assertTrue(response.contains("\\help"));
        assertEquals(DialogueState.CONVERSATION, dialogue.getState());
    }

    // можно продолжать играть
    @Test
    void gameContinuesAfterHelp() {
        GameFactory gameFactory = createTestGameFactory();
        CommandParser commandParser = new CommandParser();

        Dialogue dialogue = new Dialogue(gameFactory, commandParser);
        dialogue.handleMessage("\\new");

        dialogue.handleMessage("\\help");
        String response = dialogue.handleMessage("к");

        assertTrue(response.contains("Правильная буква!"));
    }



    private GameFactory createTestGameFactory() {
        return new GameFactory(new WordRepository()) {
            @Override
            public HangmanGame createGame() {
                return new HangmanGame("кот");
            }
        };
    }
}
