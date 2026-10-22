package org.hangman;

public class Dialogue {

    private final GameFactory gameFactory;
    private HangmanGame currentGame;
    private DialogueState state;
    private final CommandParser commandParser;

    public Dialogue(GameFactory gameFactory, CommandParser commandParser) {
        this.gameFactory = gameFactory;
        this.commandParser = commandParser;
        this.state = DialogueState.CONVERSATION;
    }

    public String start() {
        return "Добро пожаловать в игру «Виселица»!" +
                "\nВведите \\new, чтобы начать новую игру." +
                "\nВведите \\help для получения справки.";
    }

    public DialogueState getState() {
        return state;
    }

    public String handleMessage(String message) {
        Command command = commandParser.parse(message);

        if (command == Command.HELP) {
            return createHelpMessage();
        }

        if (command == Command.NEW) {
            if (state == DialogueState.CONVERSATION ||
                    state == DialogueState.GAME_OVER) {

                startNewGame();
                return createGameState() +
                        "\nВведите букву или слово:\n";
            }

            if (state == DialogueState.PLAYING) {
                return "Завершите текущую игру, чтобы начать новую.";
            }
        }

        switch (state) {
            case CONVERSATION:
                return handleConversationMessage(message);

            case PLAYING:
                return handleGameMessage(message);

            case GAME_OVER:
                return handleGameOverMessage(message);
        }

        throw new IllegalStateException("Неизвестное состояние диалога");
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
        if (currentGame.getState() == GameState.LOST) {
            return "Загаданное слово: " + currentGame.getSecretWord() +
                    "\nОшибки: " + currentGame.getMistakes() + "/6";
        }

        return "Слово: " + currentGame.getMaskedWord() +
                "\nОшибки: " + currentGame.getMistakes() + "/6";
    }

    private String createHelpMessage() {
        return """
            Доступные команды:
            \\help — показать справку.
            
            Введите одну букву, чтобы угадать её,
            или введите слово целиком.
            """;
    }

    private void startNewGame() {
        currentGame = gameFactory.createGame();
        state = DialogueState.PLAYING;
    }

    private String handleConversationMessage(String message) {
        return "Введите \\new, чтобы начать новую игру.";
    }

    private String handleGameMessage(String message) {
        UserInput input = processInput(message);

        String response;

        if (input.type() == InputType.INVALID) {
            response = "Некорректный ввод. Введите букву или слово.";
        } else {
            GuessResult result;

            if (input.type() == InputType.LETTER) {
                result = currentGame.guessLetter(input.value().charAt(0));
            } else {
                result = currentGame.guessWord(input.value());
            }

            response = createResponse(result, input.type());

            if (result == GuessResult.WON || result == GuessResult.LOST) {
                state = DialogueState.GAME_OVER;
            }
        }

        return response + "\n" + createGameState();
    }

    private String handleGameOverMessage(String message) {
        return "Игра завершена. Введите \\new, чтобы начать новую игру.";
    }
}
