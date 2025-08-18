package com.src.encoder;

import java.util.*;

import com.src.Coder;
import com.src.encoder.alg.HuffmanTree;
import com.utils.files.AppReader;
import com.utils.files.AppWriter;

import logs.AppLogger;

public class Encoder extends Coder {
    private static final AppLogger logger = AppLogger.getLogger(Encoder.class);

    private static class Compressor {
        private static byte[] mkbytes(String sourceFilename) {
            //String msg = AppReader.readFile(sourceFilename);
            //logger.info("Message read.");
            byte[] data = AppReader.readBytes(sourceFilename); 
            logger.info("File read as bytes.");

            // making 0s n 1s code
            StringBuilder encoded = new StringBuilder();
            //Map<Character, String> codetable = HuffmanTree.getCodesMap(msg);
            //for (char c : msg.toCharArray()) {
            //    encoded.append(codetable.get(c));
            //}
            Map <Byte, String> codetable = HuffmanTree.getCodesMap(data);
            for(byte b : data) encoded.append(codetable.get(b));
            String stringCode = encoded.toString();
            logger.info("0s & 1s code generated.");

            // forming code table in bin
            List<Byte> output = new ArrayList<>();
            output.add((byte) codetable.size());
            for(Map.Entry<Byte, String> entry : codetable.entrySet()) {
                Byte oneByte = entry.getKey();
                String code = entry.getValue();
                output.add((byte) oneByte); // symbol (1 byte)
                output.add((byte) code.length()); // code length

                while (code.length() % 8 != 0)
                    code += "0"; // padding to byte
                for (int i = 0; i < code.length(); i += 8) {
                    String byteStr = code.substring(i, i + 8);
                    output.add((byte) Integer.parseInt(byteStr, 2));
                }
            }
            logger.info("Binary code table formed and added.");

            // padding and message
            int paddingBits = (8 - (stringCode.length() % 8)) % 8; // addition zeros for padding
            for (int i = 0; i < paddingBits; i++) 
                stringCode += "0";
            output.add((byte) paddingBits);
            for (int i = 0; i < stringCode.length(); i+=8) {
                String byteStr = stringCode.substring(i, i + 8);
                output.add((byte) Integer.parseInt(byteStr, 2));
            }
            logger.info("Padding info and message added.");

            //making bytes array to write to .bin-file
            /* the structure:
            * 1) [number of unique symbols]
            * 2) [1st symbol + code length + its code] ... [last symbol +code length + its code]
            * 3) [padding info (number of padding bits)] 
            * 4) [1st byte] ... [last byte (m.b. padded)]
            */

            byte[] bytes = new byte[output.size()];
            for (int i = 0; i < output.size(); i++) {
                bytes[i] = output.get(i);
            }
            logger.info("Bytes formed.");
            return bytes;
        }

        private static void compress(String sourceFilename, String targetFilename) {
            byte[] data = mkbytes(sourceFilename);
            AppWriter.writeFile(data, targetFilename);
        }
    }

    @Override
    public void code(String sourceFilename, String targetFilename) {
        try {
            Compressor.compress(sourceFilename, targetFilename);
            logger.info(String.format("%s compressed into %s", sourceFilename, targetFilename));
        } catch (Exception ex) {
            logger.severe(String.format("Error, unable to compress %s.", sourceFilename));
        }
    }
    public static void encode(String sourceFilename, String targetFilename) {
        new Encoder().code(sourceFilename, targetFilename);
    }
}
