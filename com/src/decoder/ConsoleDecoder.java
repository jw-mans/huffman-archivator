package com.src.decoder;

import com.src.CoderInterface;

public class ConsoleDecoder implements CoderInterface {
    @Override
    public void code(String sourceFilename, String targetFilename) {
        Decoder.decode(sourceFilename, targetFilename);
    }
}
