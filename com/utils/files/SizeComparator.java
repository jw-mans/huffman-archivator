package com.utils.files;

import java.nio.file.Path;

import logs.AppLogger;

import java.nio.file.Files;

public class SizeComparator {
    private static final AppLogger logger = AppLogger.getLogger(SizeComparator.class);

    public static String getRatio(boolean isCompressing, String sourceFilename, String targetFilename) {
        try {
            long sourceSize = Files.size(Path.of(sourceFilename));
            long targetSize = Files.size(Path.of(targetFilename));
            float ratio = (float) sourceSize / targetSize;
            if (!isCompressing) ratio = 1 / ratio;
            logger.info(String.format("Compession ratio received.\n Source: %d.\n Target: %d.", sourceSize, targetSize));
            return String.format("Compression ratio is %.3f", ratio);
        } catch (Exception ex) {
            logger.info("Error, unable to receive compression ratio.");
            return null;
        }
    }
}
