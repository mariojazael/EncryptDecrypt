package com.mramirez;

import com.mramirez.Services.EncryptionService;
import java.io.IOException;

public class Main {
    private static final EncryptDecryptApplication encryptDecryptApplication = new EncryptDecryptApplication(new EncryptionService());

    public static void main(String[] args) throws IOException {
        encryptDecryptApplication.start(args);
    }
}
