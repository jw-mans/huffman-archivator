import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Decoder {
    private static class FileCodeReader {
        private static byte[] readFile(String filename) {
            try (FileInputStream fis = new FileInputStream(filename)) { return fis.readAllBytes(); }
            catch(IOException ex) {
                System.err.println("Error: unable to read code");
                ex.printStackTrace();
                return null;
            }
        }
    }
    private static class ByteDecoder {
        private static String makeString(String filename) {
            byte[] data = FileCodeReader.readFile(filename);
            int index = 0;
            // number of unique symbols
            int uniqueCount = data[index++] & 0xFF;
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
            return decoded.toString();
        }
    }

    private static class Decompressor {
        private static void decompress(String codeFn, String outputFn) {
            String decoded = ByteDecoder.makeString(codeFn);
            try {
                Files.writeString(Paths.get(outputFn), decoded);
            } catch (Exception ex) {
                System.err.println("Error: unable to write file.");
                ex.printStackTrace();
            }
        }
    }
    public static void decode(String codeFilename, String outputFilename) {
        try {
            Decompressor.decompress(codeFilename, outputFilename);
        } catch (Exception ex) {
            System.err.println("Error: Unable to decompress.");
            ex.printStackTrace();
        }
    }
}
