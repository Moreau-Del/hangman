package org.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DialogueManagerTest {

    // Один пользователь → один диалог
    @Test
    void sameConversationIdReturnsSameDialogue() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        Dialogue first = manager.getDialogue("alice");
        Dialogue second = manager.getDialogue("alice");

        assertSame(first, second);
    }

    // Разные пользователи → разные диалоги
    @Test
    void differentConversationIdsReturnDifferentDialogues() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        Dialogue alice = manager.getDialogue("alice");
        Dialogue bob = manager.getDialogue("bob");

        assertNotSame(alice, bob);
    }

    // Игры независимы, проверка состояний
    @Test
    void dialoguesHaveIndependentGames() {
        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        Dialogue alice = manager.getDialogue("alice");
        Dialogue bob = manager.getDialogue("bob");

        alice.handleMessage("\\new");

        assertEquals(DialogueState.PLAYING, alice.getState());
        assertEquals(DialogueState.CONVERSATION, bob.getState());
    }
}
