package com.mramirez;

import com.mramirez.Services.EncryptionService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EncryptDecryptApplicationTest {

    private static final Queue<String> inputs = new LinkedList<>();

    private static final Queue<String> encryptShiftAnswers = new LinkedList<>();

    private static final Queue<String> decryptShiftAnswers = new LinkedList<>();

    private static final Queue<String> decryptUnicodeAnswers = new LinkedList<>();

    private static final Queue<String> encryptUnicodeAnswers = new LinkedList<>();

    private static TestConfiguration encryptShiftConfig;

    private static TestConfiguration decryptShiftConfig;

    private static TestConfiguration encryptUnicodeConfig;

    private static TestConfiguration decryptUnicodeConfig;

    @BeforeAll
    static void beforeAll() {
        fillQueues();
        initConfigurations();
    }

    @BeforeEach
    void setUp() {
        verifyQueues();
    }

    @Mock
    EncryptionService encryptionService;

    @InjectMocks
    EncryptDecryptApplication encryptDecryptApplication;

    @Captor
    ArgumentCaptor<Boolean> booleanArgumentCaptor;

    @Captor
    ArgumentCaptor<Algorithms> algorithmsArgumentCaptor;

    @Captor
    ArgumentCaptor<char[]> charArgumentCaptor;

    @DisplayName("Encryption using shift algorithm test")
    @ParameterizedTest
    @MethodSource("encryptShiftArgs")
    void testEncryptWithShift(String[] args) throws IOException {
        performTest(encryptShiftConfig, args);
    }

    @DisplayName("Decryption using shift algorithm test")
    @ParameterizedTest
    @MethodSource("decryptShiftArgs")
    void testDecryptWithShift(String[] args) throws IOException {
        performTest(decryptShiftConfig, args);
    }

    @DisplayName("Encryption using unicode algorithm test")
    @ParameterizedTest
    @MethodSource("encryptUnicodeArgs")
    void testEncryptWithUnicode(String[] args) throws IOException {
        performTest(encryptUnicodeConfig, args);
    }

    @DisplayName("Decryption using unicode algorithm test")
    @ParameterizedTest
    @MethodSource("decryptUnicodeArgs")
    void testDecryptWithUnicode(String[] args) throws IOException {
        performTest(decryptUnicodeConfig, args);
    }

    private void performTest(TestConfiguration testConfiguration, String[] args) throws IOException {
        given(encryptionService.encrypt(any())).willReturn(testConfiguration.answers().peek());

        String cypheredText = encryptDecryptApplication.start(args);

        // checking the expected values are actually set
        verify(encryptionService).setEncryptModeON(booleanArgumentCaptor.capture());
        assertThat(booleanArgumentCaptor.getValue()).isEqualTo(testConfiguration.encryptMode());

        verify(encryptionService).setAlgorithm(algorithmsArgumentCaptor.capture());
        assertThat(algorithmsArgumentCaptor.getValue()).isEqualTo(testConfiguration.algorithm());

        verify(encryptionService).encrypt(charArgumentCaptor.capture());
        assertThat(charArgumentCaptor.getValue()).isEqualTo(testConfiguration.inputs().poll().toCharArray());

        assertEquals(testConfiguration.answers().poll(), cypheredText);
    }

    static Stream<Arguments> decryptShiftArgs() {
        return Stream.of(Arguments.of((Object) new String[]{"-mode", "dec", "-key", "2", "-data", "cypher me!", "-alg", "shift"}),
                Arguments.of((Object) new String[]{"-mode", "dec", "-key", "4", "-data", "i am a silly guy", "-alg", "shift"}),
                Arguments.of((Object) new String[]{"-mode", "dec", "-key", "15", "-data", "today was a good day!", "-alg", "shift"}));
    }

    static Stream<Arguments> encryptShiftArgs() {
        return Stream.of(Arguments.of((Object) new String[]{"-mode", "enc", "-key", "2", "-data", "cypher me!", "-alg", "shift"}),
                Arguments.of((Object) new String[]{"-mode", "enc", "-key", "4", "-data", "i am a silly guy", "-alg", "shift"}),
                Arguments.of((Object) new String[]{"-mode", "enc", "-key", "15", "-data", "today was a good day!", "-alg", "shift"}));
    }

    static Stream<Arguments> encryptUnicodeArgs() {
        return Stream.of(Arguments.of((Object) new String[]{"-mode", "enc", "-key", "2", "-data", "cypher me!", "-alg", "unicode"}),
                Arguments.of((Object) new String[]{"-mode", "enc", "-key", "4", "-data", "i am a silly guy", "-alg", "unicode"}),
                Arguments.of((Object) new String[]{"-mode", "enc", "-key", "5", "-data", "today was a good day!", "-alg", "unicode"}));
    }

    static Stream<Arguments> decryptUnicodeArgs() {
        return Stream.of(Arguments.of((Object) new String[]{"-mode", "dec", "-key", "2", "-data", "cypher me!", "-alg", "unicode"}),
                Arguments.of((Object) new String[]{"-mode", "dec", "-key", "4", "-data", "i am a silly guy", "-alg", "unicode"}),
                Arguments.of((Object) new String[]{"-mode", "dec", "-key", "5", "-data", "today was a good day!", "-alg", "unicode"}));
    }

    private static void fillQueues() {
        fillInputQueue();
        fillEncryptShiftAnswers();
        fillDecryptShiftAnswers();
        fillEncryptUnicodeAnswers();
        fillDecryptUnicodeAnswers();
    }

    private static void fillInputQueue() {
        inputs.add("cypher me!");
        inputs.add("i am a silly guy");
        inputs.add("today was a good day!");
    }

    private static void fillEncryptShiftAnswers() {
        encryptShiftAnswers.add("earjgt og!");
        encryptShiftAnswers.add("l dp d vloob jxb");
        encryptShiftAnswers.add("wrgdb zdv d jrrg gdb!");
    }

    private static void fillDecryptShiftAnswers() {
        decryptShiftAnswers.add("awnfcp kc!");
        decryptShiftAnswers.add("g yk y qgjjw esw");
        decryptShiftAnswers.add("ezolj hld l rzzo olj!");
    }

    private static void fillEncryptUnicodeAnswers() {
        encryptUnicodeAnswers.add("e{rjgt\"og#");
        encryptUnicodeAnswers.add("m$eq$e$wmpp}$ky}");
        encryptUnicodeAnswers.add("ytif~%|fx%f%ltti%if~&");
    }

    private static void fillDecryptUnicodeAnswers() {
        decryptUnicodeAnswers.add("awnfcp\u001Ekc");
        decryptUnicodeAnswers.add("e\u001C]i\u001C]\u001Coehhu\u001Ccqu");
        decryptUnicodeAnswers.add("oj_\\t\u001Br\\n\u001B\\\u001Bbjj_\u001B_\\t\u001C");
    }

    private static void initConfigurations() {
        encryptShiftConfig = new TestConfiguration(inputs, encryptShiftAnswers, true, Algorithms.SHIFT);
        decryptShiftConfig = new TestConfiguration(inputs, decryptShiftAnswers, false, Algorithms.SHIFT);
        encryptUnicodeConfig = new TestConfiguration(inputs, encryptShiftAnswers, true, Algorithms.UNICODE);
        decryptUnicodeConfig = new TestConfiguration(inputs, encryptShiftAnswers, false, Algorithms.UNICODE);
    }

    private void verifyQueues() {
        if(inputs.isEmpty()) fillInputQueue();
        if(encryptShiftAnswers.isEmpty()) fillEncryptShiftAnswers();
        if(decryptShiftAnswers.isEmpty()) fillDecryptShiftAnswers();
        if(encryptUnicodeAnswers.isEmpty()) fillDecryptUnicodeAnswers();
        if(decryptUnicodeAnswers.isEmpty()) fillDecryptUnicodeAnswers();
    }
}