package org.hangman;

public class Dialogue {

    private HangmanGame game;

    public Dialogue(HangmanGame game) {
        this.game = game;
    }

    public String start() {
        return "Добро пожаловать в игру «Виселица»!" +
                "\n" + createGameState() +
                "\nВведите букву или слово:\n";
    }

    public String handleMessage(String message) {
        UserInput input = processInput(message);

        String response;

        if (input.type() == InputType.INVALID) {
            response = "Некорректный ввод. Введите букву или слово.";
        } else {
            GuessResult result;

            if (input.type() == InputType.LETTER) {
                result = game.guessLetter(input.value().charAt(0));
            } else {
                result = game.guessWord(input.value());
            }

            response = createResponse(result, input.type());
        }

        return response + "\n" + createGameState();
    }

    public boolean isActive() {
        return game.getState() == GameState.IN_PROGRESS;
    }


    private UserInput processInput(String input) {
        input = normalizeInput(input);
        InputType type = validateInput(input);

        return new UserInput(input, type);
    }

    private String normalizeInput(String input) {
        return input.toLowerCase();
    }

    private InputType validateInput(String input) {
        if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
            return InputType.LETTER;
        }

        if (input.length() > 1 && input.chars().allMatch(Character::isLetter)) {
            return InputType.WORD;
        }

        return InputType.INVALID;
    }

    private String createResponse(GuessResult result, InputType type) {
        switch (result) {
            case CORRECT:
                return "Правильная буква!";

            case INCORRECT:
                if (type == InputType.LETTER) {
                    return "Неверная буква.";
                } else {
                    return "Неверное слово.";
                }

            case ALREADY_USED:
                return "Эта буква уже использовалась.";

            case WON:
                return "Поздравляем! Вы победили!";

            case LOST:
                return "Вы проиграли.";
        }

        return "";
    }

    private String createGameState() {
        if (game.getState() == GameState.LOST) {
            return "Загаданное слово: " + game.getSecretWord() +
                    "\nОшибки: " + game.getMistakes() + "/6";
        }

        return "Слово: " + game.getMaskedWord() +
                "\nОшибки: " + game.getMistakes() + "/6";
    }
}
