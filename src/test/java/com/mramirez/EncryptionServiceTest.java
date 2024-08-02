package com.mramirez;

import com.mramirez.Services.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class EncryptionServiceTest {

    private static EncryptionService encryptionService;

    private static EncryptDecryptApplication encryptDecryptApp;

    private static Queue<String> answers;

    @BeforeAll
    static void beforeAll() {
        encryptionService = new EncryptionService();
        encryptDecryptApp = new EncryptDecryptApplication(encryptionService);
        fillAnswers();
    }

    @DisplayName("Test every declared input")
    @ParameterizedTest
    @MethodSource("encryptTestArgs")
    void encryptTest(String[] args) throws IOException {
        String output = encryptDecryptApp.start(args);
        assertAll("Expected conditions in the output",
                () -> assertThat(output).isNotNull(),
                () -> assertThat(output.length()).isGreaterThan(0),
                () -> assertThat(output).isEqualTo(answers.poll()));
    }

    static Stream<Arguments> encryptTestArgs() {
        return EncryptDecryptApplicationTest.fullArgs();
    }

    static void fillAnswers() {
        answers = new LinkedList<>(EncryptDecryptApplicationTest.getFullAnswers());
    }
}