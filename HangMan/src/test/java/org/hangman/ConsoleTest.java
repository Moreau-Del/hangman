package org.hangman;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream; // имитирует ввод пользователя
import java.io.ByteArrayOutputStream; // перехватывает вывод консоли;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class ConsoleTest {

    // получение диалога по ID
    @Test
    void consoleGetsDialogueFromManager() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        ByteArrayInputStream input =
                new ByteArrayInputStream(new byte[0]);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Console console = new Console(
                manager,
                new Scanner(input),
                new PrintStream(output)
        );

        assertNotNull(manager.getDialogue("console"));
    }

    // Передача \new и изменение состояния диалога
    @Test
    void consolePassesMessageToDialogue() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        ByteArrayInputStream input =
                new ByteArrayInputStream("\\new\n".getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Console console = new Console(
                manager,
                new Scanner(input),
                new PrintStream(output)
        );

        assertThrows(
                java.util.NoSuchElementException.class,
                console::start
        );

        assertEquals(
                DialogueState.PLAYING,
                manager.getDialogue("console").getState()
        );
    }

    // Вывод ответа на \help
    @Test
    void consolePrintsDialogueResponse() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        ByteArrayInputStream input =
                new ByteArrayInputStream("\\help\n".getBytes(StandardCharsets.UTF_8));

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        Console console = new Console(
                manager,
                new Scanner(input),
                new PrintStream(output)
        );

        assertThrows(
                java.util.NoSuchElementException.class,
                console::start
        );

        assertTrue(output.toString().contains("Доступные команды:"));
    }
}
