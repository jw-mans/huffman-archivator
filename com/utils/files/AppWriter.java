package com.utils.files;

import java.nio.file.Files;
import java.nio.file.Paths;

import logs.AppLogger;

public class AppWriter {
    private static final AppLogger logger = AppLogger.getLogger(AppWriter.class);
    public static void writeFile(byte[] data, String targetFilename) {
        try {
            Files.write(Paths.get(targetFilename), data);
            logger.info(String.format("Message written to %s.", targetFilename));
        } catch (Exception ex) {
            logger.severe(String.format("Error, unable to write message to %s.", ex));
        }
    }
}
