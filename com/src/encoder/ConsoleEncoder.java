package com.src.encoder;

import com.src.CoderInterface;

public class ConsoleEncoder implements CoderInterface {
    @Override
    public void code(String sourceFilename, String targetFilename) {
        Encoder.encode(sourceFilename, targetFilename);
    }
}
