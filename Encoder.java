import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Encoder {

    private static class FileTextReader {
        private static String readFile (String ifn) {
            try {
                return new String(Files.readAllBytes(Paths.get(ifn)));
            } catch (IOException ex) {
                System.err.println("Error: incorrect file name."); 
                ex.printStackTrace();
                return null;
            }
        } 
    }

    private static class Tree {
        private Map<Character, String> codetable = new HashMap<>();

        private Tree(String msg) {
            Map<Character, Integer> freqMap = new HashMap<>();
            for (char c : msg.toCharArray()) 
                freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);

            PriorityQueue<Node> nodes = new PriorityQueue<>(new NodeComparator());

            for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) 
                nodes.offer(new Node(entry.getValue(), entry.getKey()));
        
            while (nodes.size() > 1) {
                Node left = nodes.poll();
                Node right = nodes.poll();
                Node parent = new Node(left.getFreq() + right.getFreq());
                parent.setChildren(left, right);
                nodes.offer(parent);
            }
            Node root = nodes.peek();
            makeCode(root, "", codetable);

            printCodetable();
            printMessage(msg);
        }

        private void printCodetable() {
            for(Map.Entry<Character, String> entry : codetable.entrySet()) {
                System.out.printf("\'%c\' : %s%n", entry.getKey(), entry.getValue());
            }
        }

        private void printMessage(String msg) {
            System.out.println("\nYour message:");
            for (char c : msg.toCharArray()) {
                System.out.print(codetable.get(c));
            }
            System.out.print("\n");
        }

        private void makeCode(Node root, String code, Map<Character, String> codes) {
            if (root == null) return;
            if (root.isLeaf()) 
                codes.put(root.getChar(), code);
        
            makeCode(root.getLeft(), code + "0", codes);
            makeCode(root.getRight(), code + "1", codes);
        }

        private static class Node {
            private int freq;
            private char c;
            private Node left, right;

            private Node(int freq, char c) {
                this.freq = freq;
                this.c = c;
                this.left = null;
                this.right = null;
            }

            private Node(int freq) { this(freq, '\0'); }

            private int getFreq() { return freq; }
            private char getChar() { return c; }
            private Node getLeft() { return left; }
            private Node getRight() { return right; }

            private boolean isLeaf() { return left == null && right == null; }

            private void setChildren(Node left, Node right) {
                this.left = left;
                this.right = right;
            }
        }

        private static class NodeComparator implements Comparator<Node> {
            public int compare(Node l, Node r) { return Integer.compare(l.getFreq(), r.getFreq()); }
        } 
    }

    private static class ByteEncoder {
        private static byte[] makeBytes(String initialFn) { 

            String msg = FileTextReader.readFile(initialFn);

            // making 0s n 1s code
            StringBuilder encoded = new StringBuilder();
            Tree tree = new Tree(msg);
            for (char c : msg.toCharArray()) {
                encoded.append(tree.codetable.get(c));
            }
            String stringCode = encoded.toString();
            
            // forming code table in bin
            List<Byte> output = new ArrayList<>();
            output.add((byte) tree.codetable.size());
            for(Map.Entry<Character, String> entry : tree.codetable.entrySet()) {
                char sym = entry.getKey();
                String code = entry.getValue();
                output.add((byte) sym); // symbol (1 byte)
                output.add((byte) code.length()); // code length

                while (code.length() % 8 != 0)
                    code += "0"; // padding to byte
                for (int i = 0; i < code.length(); i += 8) {
                    String byteStr = code.substring(i, i + 8);
                    output.add((byte) Integer.parseInt(byteStr, 2));
                }
            }

            // padding and message
            int paddingBits = (8 - (stringCode.length() % 8)) % 8; // addition zeros for padding
            for (int i = 0; i < paddingBits; i++) 
                stringCode += "0";
            output.add((byte) paddingBits);
            for (int i = 0; i < stringCode.length(); i+=8) {
                String byteStr = stringCode.substring(i, i + 8);
                output.add((byte) Integer.parseInt(byteStr, 2));
            }

            //making bytes array to write to .bin-file
            /* the structure:
            * 1) [number of unique symbols]
            * 2) [1st symbol + code length + its code] ... [last symbol +code length + its code]
            * 1) [padding info (number of padding bits)] 
            * 2) [1st byte] ... [last byte (m.b. padded)]
            */

            byte[] bytes = new byte[output.size()];
            for (int i = 0; i < output.size(); i++) {
                bytes[i] = output.get(i);
            }
            return bytes;
        }
    }

    private static class Compressor {
        private static void compress(String initialFn, String compressedFn) {
            byte[] bytes = ByteEncoder.makeBytes(initialFn);
            
            try(FileOutputStream fos = new FileOutputStream(compressedFn)) {
                fos.write(bytes);
                System.out.println("Encoded message written!");
            } catch (IOException ex) {
                System.err.println("Error: unable to write code!");
                ex.printStackTrace();
            }
        }
    }

    public static void encode(String initialFilename, String codeFilename) {
        try {
            Compressor.compress(initialFilename, codeFilename);
        } catch (Exception ex) {
            System.err.println("Error: uncorrect file name."); 
            ex.printStackTrace();
        }
    }
}
