package org.hangman;

import java.util.HashMap;
import java.util.Map;

public class DialogueManager {

    // ID диалогов
    private final Map<String, Dialogue> dialogues = new HashMap<>();

    private final GameFactory gameFactory;
    private final CommandParser commandParser;

    // конструктор получает зависимости для создания и обработки диалогов
    public DialogueManager(GameFactory gameFactory, CommandParser commandParser) {
        this.gameFactory = gameFactory;
        this.commandParser = commandParser;
    }

    // проверяет, существует ли диалог
    // да - возвращает его
    // нет - создает новый
    public Dialogue getDialogue(String conversationId) {
        if (!dialogues.containsKey(conversationId)) {
            Dialogue dialogue = new Dialogue(gameFactory, commandParser);
            dialogues.put(conversationId, dialogue);
        }

        return dialogues.get(conversationId);
    }
}