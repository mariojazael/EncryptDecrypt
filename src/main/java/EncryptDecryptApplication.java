import java.io.*;

public class EncryptDecryptApplication {
    private static final char[] ALPHABET = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private File inputFile = null;
    private File outputFile = null;
    private BufferedReader bufferedReader = null;
    private String preferedAlgorithm = "shift";
    private boolean isEncryptModeON = true;
    private char[] inputCharArray = null;
    private int placesToMove = 0;

    public void start(String[] args) throws IOException {
        readInputArgs(args);
        if(!isInputCharArrayPresent()) processInputFile(inputFile);
        String result = applyAlgorithm(preferedAlgorithm);
        processResult(result);
    }

    private void processResult(String result) {
        if(outputFile == null) {
            System.out.println(result);
        } else {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                writer.write(result);
            } catch (IOException e) {
                System.err.println("Error al escribir en el archivo de salida: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    private String applyAlgorithm(String preferedAlgorithm) {
        StringBuilder result = new StringBuilder();
        if(inputCharArray != null) {
            for (char currentChar : inputCharArray) {
                char cypheredChar;
                if(preferedAlgorithm.equals("unicode")) cypheredChar = processUnicode(currentChar, placesToMove);
                else cypheredChar = processShift(currentChar, placesToMove);
                result.append(cypheredChar);
            }
        }
        return result.toString();
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

    private boolean isInputCharArrayPresent() {
        return inputCharArray != null;
    }

    private void readInputArgs(String[] args) {
        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-mode" -> isEncryptModeON = args[i + 1].equals("enc");
                case "-key" -> placesToMove = Integer.parseInt(args[i + 1]);
                case "-data" -> inputCharArray = args[i + 1].toCharArray();
                case "-in" -> inputFile = new File(args[i + 1]);
                case "-out" -> outputFile = new File(args[i + 1]);
                case "-alg" -> preferedAlgorithm = args[i + 1];
            }
        }
    }

    private void processInputFile(File inputFile) throws IOException {
        if(inputFile != null) {
            try {
                if(inputFile.exists()) {
                    if(!outputFile.exists()) outputFile.createNewFile();

                    bufferedReader = new BufferedReader(new FileReader(inputFile));

                    StringBuilder content = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        content.append(line);
                    }
                    inputCharArray = content.toString().toCharArray();
                } else {
                    throw new FileNotFoundException("El archivo de entrada especificado no existe: " + inputFile.getAbsolutePath());
                }

            } catch (IOException e) {
                System.err.println("Error al procesar el archivo de entrada: " + e.getMessage());
                System.exit(1);
            } finally {
                if(bufferedReader != null) bufferedReader.close();
            }
        }
    }
}
