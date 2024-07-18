package src;

import src.Services.EncryptionService;

import java.io.*;

import static src.Algorithms.*;

public class EncryptDecryptApplication {
    private File inputFile = null;
    private File outputFile = null;
    private BufferedReader bufferedReader = null;
    private Algorithms preferedAlgorithm = SHIFT;
    private boolean isEncryptModeON = true;
    private char[] inputCharArray = null;
    private int placesToMove = 0;
    private static final EncryptionService encryptionService = new EncryptionService();

    public void start(String[] args) throws IOException {
        readInputArgs(args);
        setArgs();
        if(!isInputCharArrayPresent()) processInputFile(inputFile);
        String result = encryptionService.encrypt(inputCharArray);
        processResult(result);
    }

    private void setArgs() {
        encryptionService.setAlgorithm(preferedAlgorithm);
        encryptionService.setEncryptModeON(isEncryptModeON);
        encryptionService.setPlacesToMove(placesToMove);
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

    private boolean isInputCharArrayPresent() {
        return inputCharArray != null;
    }

    private void readInputArgs(String[] args) {
        for(int i = 0; i < args.length; i++) {
            Arguments arg = parseArg(args[i]);
            switch (arg) {
                case MODE -> isEncryptModeON = args[i + 1].equals("enc");
                case KEY -> placesToMove = Integer.parseInt(args[i + 1]);
                case DATA -> inputCharArray = args[i + 1].toCharArray();
                case IN -> inputFile = new File(args[i + 1]);
                case OUT -> outputFile = new File(args[i + 1]);
                case ALG -> preferedAlgorithm = Algorithms.valueOf(args[i + 1].toUpperCase());
            }
        }
    }

    private Arguments parseArg(String arg) {
        return Arguments.valueOf(arg.substring(1).toUpperCase());
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
