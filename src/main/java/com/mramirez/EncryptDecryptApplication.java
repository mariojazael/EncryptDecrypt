package com.mramirez;

import com.mramirez.Services.EncryptionService;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import static com.mramirez.Algorithms.SHIFT;

public class EncryptDecryptApplication {
    private final EncryptionService encryptionService;
    private File inputFile = null;
    private File outputFile = null;
    private BufferedReader bufferedReader = null;
    private Algorithms preferedAlgorithm = SHIFT;
    private boolean isEncryptModeON = true;
    private char[] inputCharArray = null;
    private int placesToMove = 0;

    EncryptDecryptApplication(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    public String start(String[] args) throws IOException {
        readInputArgs(args);
        setArgs();
        if(!isInputCharArrayPresent()) processInputFile(inputFile);
        String result = encryptionService.encrypt(inputCharArray);
        processResult(result);
        return result;
    }

    private void setArgs() {
        encryptionService.setAlgorithm(preferedAlgorithm);
        encryptionService.setEncryptModeON(isEncryptModeON);
        encryptionService.setPlacesToMove(placesToMove);
    }

    private void processResult(String result) {
        if (outputFile == null) {
            System.out.println(result);
        } else {
            try (BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8))) {
                writer.write(result);
            } catch (IOException e) {
                System.err.println("Error al escribir en el archivo de salida: " + e.getMessage());
            }
        }
    }

    private boolean isInputCharArrayPresent() {
        return inputCharArray != null;
    }

    private void readInputArgs(String[] args) {
        for(int i = 0; i < args.length; i += 2) {
            ApplicationArguments arg = parseArg(args[i]);
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

    private ApplicationArguments parseArg(String arg) {
        return ApplicationArguments.valueOf(arg.substring(1).toUpperCase());
    }

    private void processInputFile(File inputFile) throws IOException {
        if (inputFile != null) {
            try {
                if (inputFile.exists()) {
                    if (!outputFile.exists() && outputFile.createNewFile()) {
                            // Usa InputStreamReader con una codificación específica
                            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), StandardCharsets.UTF_8));
                            StringBuilder content = new StringBuilder();
                            String line;
                            while ((line = bufferedReader.readLine()) != null) {
                                content.append(line);
                            }
                            inputCharArray = content.toString().toCharArray();
                    }
                } else {
                    throw new FileNotFoundException("El archivo de entrada especificado no existe: " + inputFile.getAbsolutePath());
                }
            } catch (IOException e) {
                System.err.println("Error al procesar el archivo de entrada: " + e.getMessage());
            } finally {
                if (bufferedReader != null) bufferedReader.close();
            }
        }
    }
}

