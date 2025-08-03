import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Encoder {
    private String initialMsg;
    private Tree tree;

    private String makeBinCode() {
        StringBuilder encoded = new StringBuilder();
        for (char c : initialMsg.toCharArray()) {
            encoded.append(tree.codetable.get(c));
        }
        return encoded.toString();
    }

    private byte[] binarizeMsg() {
        /*
         * the structure:
         * 1) [padding info (number of padding bits)] 
         * 2) [1st byte] ... [nth byte (m.b. padded)]
         */

        String stringCode = makeBinCode();
        // addition zeros for padding
        int paddingBits = (8 - (stringCode.length() % 8)) % 8;
        for (int i = 0; i < paddingBits; i++) 
            stringCode += "0";

        int byteCount = stringCode.length() / 8;
        byte[] bytes = new byte[byteCount + 1]; // +1 for padding info
        bytes[0] = (byte) paddingBits;

        for (int i = 1; i < bytes.length; i++) {
            String byteStr = stringCode.substring((i - 1) * 8, i * 8);
            bytes[i] = (byte) Integer.parseInt(byteStr, 2);
        }

        return bytes;
    }

    private void makeBinFile(String codeFilename) {
        byte[] bytes = binarizeMsg();
        try(FileOutputStream fos = new FileOutputStream(codeFilename)) {
            fos.write(bytes);
            System.out.println("Encoded message written!");
        } catch (IOException ex) {
            System.err.println("Error: unable to written code!");
            ex.printStackTrace();
        }
    }

    public Encoder(String initialFilename, String codeFilename) {
        try {
            this.initialMsg = new String(Files.readAllBytes(Paths.get(initialFilename)));
        } catch (IOException ex) {
            System.err.println("Error: uncorrect file name."); 
            ex.printStackTrace();
        }
        this.tree = new Tree(initialMsg);
        makeBinFile(codeFilename);
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

            public Node(int freq, char c) {
                this.freq = freq;
                this.c = c;
                this.left = null;
                this.right = null;
            }

            public Node(int freq) { this(freq, '\0'); }

            public int getFreq() { return freq; }
            public char getChar() { return c; }
            public Node getLeft() { return left; }
            public Node getRight() { return right; }

            public boolean isLeaf() { return left == null && right == null; }

            public void setChildren(Node left, Node right) {
                this.left = left;
                this.right = right;
            }
        }

        private static class NodeComparator implements Comparator<Node> {
            public int compare(Node l, Node r) { return Integer.compare(l.getFreq(), r.getFreq()); }
        } 
    }
}
