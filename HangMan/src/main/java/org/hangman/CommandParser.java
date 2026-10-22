package org.hangman;

public class CommandParser {

    public Command parse(String input) {
        if (input.equals("\\help")) {
            return Command.HELP;
        }

        if (input.equals("\\new")) {
            return Command.NEW;
        }

        return Command.UNKNOWN;
    }
}