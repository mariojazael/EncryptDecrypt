package src.Services;

import src.Algorithms;

public class EncryptionService {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private int placesToMove;
    private boolean isEncryptModeON;
    private Algorithms algorithm;

    public String encrypt(char[] inputCharArray) {
        StringBuilder result = new StringBuilder();
        if(inputCharArray != null) {
            for (char currentChar : inputCharArray) {
                char cypheredChar = processChar(currentChar, placesToMove);
                result.append(cypheredChar);
            }
        }
        return result.toString();
    }

    private char processChar(char currentChar, int placesToMove) {
        return switch(algorithm) {
            case UNICODE -> processUnicode(currentChar, placesToMove);
            case SHIFT -> processShift(currentChar, placesToMove);
        };
    }

    private char processUnicode(int integerRepresentation, int placesToMove) {
        return isEncryptModeON ? (char) (integerRepresentation + placesToMove) : (char) (integerRepresentation - placesToMove);
    }

    private char processShift(char c, int placesToMove) {
        if(!Character.isLetter(c)) return c;
        int index = 0;
        for(char letter : ALPHABET) {
            if(c == letter) break;
            else index++;
        }
        if(isEncryptModeON) {
            for (int i = 0; i < placesToMove; i++) {
                if (index >= ALPHABET.length) index = 1;
                else index++;
            }
        } else {
            for (int i = 0; i < placesToMove; i++) {
                if (index < 0) index = 24;
                else index--;
            }
        }
        return ALPHABET[index];
    }

    public void setPlacesToMove(int placesToMove) {
        this.placesToMove = placesToMove;
    }

    public void setEncryptModeON(boolean encryptModeON) {
        isEncryptModeON = encryptModeON;
    }

    public void setAlgorithm(Algorithms algorithm) {
        this.algorithm = algorithm;
    }
}
