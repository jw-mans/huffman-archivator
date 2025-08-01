import java.util.*;

public class Tree {
    private String msg;
    private Map<Character, String> codetable = new HashMap<>();

    public Tree() {
        Scanner scanner = new Scanner(System.in);
        this.msg = scanner.nextLine();

        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : msg.toCharArray()) {
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);
        }

        PriorityQueue<Node> nodes = new PriorityQueue<>(new NodeComparator());

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) {
            nodes.offer(new Node(entry.getValue(), entry.getKey()));
        }

        while (nodes.size() > 1) {
            Node left = nodes.poll();
            Node right = nodes.poll();

            Node parent = new Node(left.getFreq() + right.getFreq());
            parent.setChildren(left, right);
            nodes.offer(parent);
        }

        Node root = nodes.peek();
        encode(root, "", codetable);

        for (char c : msg.toCharArray()) {
            System.out.print(codetable.get(c));
        }

        scanner.close();
    }

    private void encode(Node root, String code, Map<Character, String> codes) {
        if (root == null) return;
        if (root.isLeaf()) {
            codes.put(root.getChar(), code);
        }
        encode(root.getLeft(), code + "0", codes);
        encode(root.getRight(), code + "1", codes);
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

        public Node(int freq) {
            this(freq, '\0');
        }

        public int getFreq() {
            return freq;
        }

        public char getChar() {
            return c;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setChildren(Node left, Node right) {
            this.left = left;
            this.right = right;
        }

        public boolean isLeaf() {
            return left == null && right == null;
        }
    }

    private static class NodeComparator implements Comparator<Node> {
        public int compare(Node l, Node r) {
            return Integer.compare(l.getFreq(), r.getFreq());
        }
    }
}
