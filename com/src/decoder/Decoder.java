package com.src.decoder;

import java.util.*;

import com.src.Coder;
import com.utils.files.AppReader;
import com.utils.files.AppWriter;

import logs.AppLogger;

public class Decoder extends Coder {
    private static final AppLogger logger = AppLogger.getLogger(Decoder.class);

    private static class Decompressor {
        private static byte[] mkbytes(String sourceFilename) {
            byte[] data = AppReader.readBytes(sourceFilename);
            logger.info(String.format("Bytes from %s received.", sourceFilename));

            int index = 0;
            // number of unique symbols
            int uniqueCount = data[index++] & 0xFF;
            logger.info("Counter received.");

            // code table
            Map<String, Byte> decodeTable = new HashMap<>();
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
                decodeTable.put(code, (byte) sym);
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
            List<Byte> decoded = new ArrayList<>();
            StringBuilder currentCode = new StringBuilder();
            for (char bit : messageBits.toString().toCharArray()) {
                currentCode.append(bit);
                if (decodeTable.containsKey(currentCode.toString())) {
                    decoded.add(decodeTable.get(currentCode.toString()));
                    currentCode.setLength(0);
                }
            }
            logger.info("Message decoded.");

            byte[] res = new byte[decoded.size()];
            for(int i = 0; i < decoded.size(); i++)
                res[i] = decoded.get(i);
            logger.info("Bytes maken.");
            return res;
        }

        private static void decompress(String sourceFilename, String targetFilename) {
            byte[] data = mkbytes(sourceFilename);
            AppWriter.writeFile(data, targetFilename);
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
