package com.src.encoder.alg;
import java.util.*;

import logs.AppLogger;

public class HuffmanTree {
    private static AppLogger logger = AppLogger.getLogger(HuffmanTree.class);

    private Map<Byte, String> codetable = new HashMap<>();

    private static class Node {
        public Node left, right;
        public int freq;
        public byte b;
        Node(int freq, byte b) {
            this.freq = freq;
            this.b = b;
        }
        Node(int freq) { this(freq, (byte)0); }
        boolean isLeaf() {return left == null && right == null; }
    }

    private static class NodeComparator implements Comparator<Node> {
        public int compare(Node l, Node r) { return Integer.compare(l.freq, r.freq); }
    } 


    private void makeCode(Node root, String code) {
        if (root == null) return;
        if (root.isLeaf()) 
            this.codetable.put(root.b, code);
    
        makeCode(root.left, code + "0");
        makeCode(root.right, code + "1");
    }
    
    private HuffmanTree(byte[] bytes) {
        Map<Byte, Integer> freqMap = new HashMap<>();
        for (byte b : bytes) 
            freqMap.put(b, freqMap.getOrDefault(b, 0) + 1);

        PriorityQueue<Node> nodes = new PriorityQueue<>(new NodeComparator());

        for (Map.Entry<Byte, Integer> entry : freqMap.entrySet()) 
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
        makeCode(root, "");
        logger.info("Tree created.");
    }

    public static Map<Byte, String> getCodesMap(byte[] bytes) {
        return new HuffmanTree(bytes).codetable;
    }
}
