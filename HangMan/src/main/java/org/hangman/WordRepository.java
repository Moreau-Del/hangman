package org.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WordRepository {
    private final List<String> words;
    private final Random random;

    public WordRepository() {
        words = new ArrayList<>();
        random = new Random();

        var resource = getClass().getResourceAsStream("/words.txt");

        if (resource == null) {
            throw new IllegalStateException("Файл words.txt не найден.");
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(resource, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                words.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении words.txt.", e);
        }
    }

    public String getRandomWord() {
        int index = random.nextInt(words.size());

        return words.get(index);
    }
}
