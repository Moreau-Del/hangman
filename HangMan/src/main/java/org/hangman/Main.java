package org.hangman;

public class Main {
    public static void main(String[] args) {

        WordRepository repository = new WordRepository();
        GameFactory gameFactory = new GameFactory(repository);
        CommandParser commandParser = new CommandParser();

        DialogueManager manager =
                new DialogueManager(gameFactory, commandParser);

        Console console = new Console(manager);
        console.start();
    }
}