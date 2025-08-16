package com.utils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import logs.AppLogger;

public class AppReader {
    private static final AppLogger logger = AppLogger.getLogger(AppReader.class);
        
    private static byte[] executeRead(String sourceFilename, boolean isBytes) {
        try { 
            byte[] data = Files.readAllBytes(Paths.get(sourceFilename));
            logger.info(String.format("%s read.", sourceFilename));
            return data;
        } catch (Exception ex) {
            logger.severe(String.format("Error, unable to read %s", sourceFilename));
            return null;
        }
    }

    public static String readFile(String sourceFilename) {
        byte[] data = executeRead(sourceFilename, false);
        return data != null ? new String(data, StandardCharsets.UTF_8) : null;
    }

    public static byte[] readBytes(String sourceFilename) {
        return (byte[]) executeRead(sourceFilename, true);
    }
}
