package org.hangman;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class WordRepositoryTest {

    @Test
    void randomWordIsNotNull() {
        WordRepository repository = new WordRepository();

        String word = repository.getRandomWord();

        assertNotNull(word);
    }

    @Test
    void randomWordIsNotEmpty() {
        WordRepository repository = new WordRepository();

        String word = repository.getRandomWord();

        assertFalse(word.isEmpty());
    }
}
