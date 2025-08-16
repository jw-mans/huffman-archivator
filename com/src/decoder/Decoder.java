package com.src.decoder;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import com.src.Coder;
import com.utils.AppReader;

import logs.AppLogger;

public class Decoder extends Coder {
    private static final AppLogger logger = AppLogger.getLogger(Decoder.class);

    private static class Decompressor {
        private static String mkstr(String sourceFilename) {
            byte[] data = AppReader.readBytes(sourceFilename);
            logger.info(String.format("Bytes from %s received.", sourceFilename));

            int index = 0;
            // number of unique symbols
            int uniqueCount = data[index++] & 0xFF;
            logger.info("Counter received.");

            // code table
            Map<String, Character> decodeTable = new HashMap<>();
            for (int i = 0; i < uniqueCount; i++) {
                char sym = (char) (data[index++] & 0xFF);
                int codeLength = data[index++] & 0xFF;
                // code byte weight
                int codeBytes = (codeLength + 7) / 8;
                StringBuilder codeBits = new StringBuilder();
                for(int j = 0; j < codeBytes; j++) {
                    String byteStr = String.format(
                        "%8s", 
                        Integer.toBinaryString(data[index++] & 0xFF)).replace(
                            ' ', '0'
                        );
                    codeBits.append(byteStr);
                }
                //cutting 
                String code = codeBits.substring(0, codeLength);
                decodeTable.put(code, sym);
            }
            logger.info("Codetable unpacked.");

            // padding bits
            int paddingBits = data[index++] & 0xFF;
            // read message by bits
            StringBuilder messageBits = new StringBuilder();
            while(index < data.length) {
                String byteStr = String.format(
                    "%8s", 
                    Integer.toBinaryString(data[index++] & 0xFF)).replace(
                            ' ', '0'
                        );
                messageBits.append(byteStr);
            }
            // remove padding bits
            if (paddingBits > 0 && paddingBits <= 7) {
                messageBits.setLength(messageBits.length() - paddingBits);
            }
            logger.info("Message bits extracted.");
            
            // table decoding 
            StringBuilder decoded = new StringBuilder();
            StringBuilder currentCode = new StringBuilder();
            for (char bit : messageBits.toString().toCharArray()) {
                currentCode.append(bit);
                if (decodeTable.containsKey(currentCode.toString())) {
                    decoded.append(decodeTable.get(currentCode.toString()));
                    currentCode.setLength(0);
                }
            }
            logger.info("Message decoded.");

            return decoded.toString();
        }

        private static void decompress(String sourceFilename, String targetFilename) {
            String decoded = mkstr(sourceFilename);
            try {
                Files.writeString(Paths.get(targetFilename), decoded);
                logger.info(String.format("Message written to %s.", targetFilename));
            } catch (Exception ex) {
                logger.severe(String.format("Error, unable to write message to %s.", ex));
            }
        }
    }

    @Override
    public void code(String sourceFilename, String targetFilename) {
        try {
            Decompressor.decompress(sourceFilename, targetFilename);
            logger.info(String.format("%s decompressed into %s", sourceFilename, targetFilename));
        } catch (Exception ex) {
            logger.severe(String.format("Error, unable to decompress %s.", sourceFilename));
        }
    }
    public static void decode(String sourceFilename, String targetFilename) {
        new Decoder().code(sourceFilename, targetFilename);
    }
}
