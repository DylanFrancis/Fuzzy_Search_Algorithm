package main;

import java.util.*;

public class BKTree {
    private final int SEARCH_TOLERANCE = 10;
    private final int ADD_TOLERANCE = 2;
    private Node root;
    private int size = 0;

    public List<String> find(String word){
        Stack<Node> stack = new Stack<>();
        List<String> words = new MaxLinkedList<>(15);

        stack.push(root);
        while (stack.size() > 0){
            Node cur = stack.pop();

            // Calculate tolerance
            int dist = calculateEditDistance(word, cur.getWord());
            int rangeX = dist - SEARCH_TOLERANCE;
            int rangeY = dist + SEARCH_TOLERANCE;
            if(dist <= ADD_TOLERANCE) words.add(cur.getWord());

            // Pick Children
            List<Edge> list = cur.getEdges();
            list.forEach(edge -> {
                if(edge.getDist() >= rangeX && edge.getDist() <= rangeY)
                    stack.push(edge.getChild());
            });
        }

        return words;
    }

    public void insert(String word){
        size++;
        if(size % 1000 == 0)
            System.out.println(size);
        if(root == null){
            root = new Node(word);
            return;
        }
        doInsert(word, root);
    }

    private void doInsert(String word, Node cur){
        int dist = calculateEditDistance(word, root.getWord());
        Edge edge = cur.hasDistance(dist);

        while(cur.hasDistance(dist) != null) cur = cur.hasDistance(dist).getChild();

        Node newNode = new Node(word);
        Edge newEdge = new Edge(dist, cur, newNode);
        cur.addEdge(newEdge);
    }

    /**
     * Credit: http://rosettacode.org/wiki/Levenshtein_distance#Java
     */
    private int calculateEditDistance(String word1, String word2){
        word1 = word1.toLowerCase();
        word2 = word2.toLowerCase();

        int [] costs = new int [word2.length() + 1];
        for (int j = 0; j < costs.length; j++)
            costs[j] = j;
        for (int i = 1; i <= word1.length(); i++) {
            costs[0] = i;
            int nw = i - 1;
            for (int j = 1; j <= word2.length(); j++) {
                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), word1.charAt(i - 1) == word2.charAt(j - 1) ? nw : nw + 1);
                nw = costs[j];
                costs[j] = cj;
            }
        }
        return costs[word2.length()];
    }

    private class Edge{
        private int dist;
        private Node parent;
        private Node child;

        public Edge(int dist, Node parent, Node child) {
            this.dist = dist;
            this.parent = parent;
            this.child = child;
        }

        public int getDist() {
            return dist;
        }

        public Node getParent() {
            return parent;
        }

        public Node getChild() {
            return child;
        }
    }

    private class Node{
        private String word;
        private List<Edge> edges;
        private int traverse = 0;

        public Node(String word) {
            this.word = word;
            edges = new LinkedList<>();
        }

        public String getWord() {
            return word;
        }

        public boolean isLeaf(){
            return edges.size() == 0;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public void addEdge(Edge newEdge){
            edges.add(newEdge);
        }

        public Node nextNode(){
            if(traverse == edges.size()) {
                traverse = 0;
                return null;
            } else {
                int idx = traverse;
                traverse++;
                return edges.get(idx).getChild();
            }
        }

        public boolean hasNext(){
            return traverse != edges.size();
        }

        public Edge nextEdge(){
            if(traverse == edges.size()) {
                traverse = 0;
                return null;
            } else {
                int idx = traverse;
                traverse++;
                return edges.get(idx);
            }
        }

        public Edge hasDistance(int dist){
            for(int x = 0; x < edges.size(); x++){
                if (edges.get(x).getDist() == dist)
                    return edges.get(x);
            }
            return null;
        }
    }
}
