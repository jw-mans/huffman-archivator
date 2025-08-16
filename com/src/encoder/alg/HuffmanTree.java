package com.src.encoder.alg;
import java.util.*;

import logs.AppLogger;

public class HuffmanTree {
    private static AppLogger logger = AppLogger.getLogger(HuffmanTree.class);

    private Map<Character, String> codetable = new HashMap<>();

    private static class Node {
        public Node left, right;
        public int freq;
        public char c;
        Node(int freq, char c) {
            this.freq = freq;
            this.c = c;
        }
        Node(int freq) { this(freq, '\0'); }
        boolean isLeaf() {return left == null && right == null; }
    }

    private static class NodeComparator implements Comparator<Node> {
            public int compare(Node l, Node r) { return Integer.compare(l.freq, r.freq); }
        } 


    private void makeCode(Node root, String code, Map<Character, String> codes) {
        if (root == null) return;
        if (root.isLeaf()) 
            codes.put(root.c, code);
    
        makeCode(root.left, code + "0", codes);
        makeCode(root.right, code + "1", codes);
    }
    
    private HuffmanTree(String msg) {
        Map<Character, Integer> freqMap = new HashMap<>();
        for (char c : msg.toCharArray()) 
            freqMap.put(c, freqMap.getOrDefault(c, 0) + 1);

        PriorityQueue<Node> nodes = new PriorityQueue<>(new NodeComparator());

        for (Map.Entry<Character, Integer> entry : freqMap.entrySet()) 
            nodes.offer(new Node(entry.getValue(), entry.getKey()));
        
        while (nodes.size() > 1) {
            Node left = nodes.poll();
            Node right = nodes.poll();
            Node parent = new Node(left.freq + right.freq);
            parent.left = left;
            parent.right = right;
            nodes.offer(parent);
        }
        Node root = nodes.peek();
        makeCode(root, "", codetable);
        logger.info("Tree created.");
    }

    public static Map<Character, String> getCodesMap(String msg) {
        return new HuffmanTree(msg).codetable;
    }
}
