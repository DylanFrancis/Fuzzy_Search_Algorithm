package main;

import java.io.*;
import java.util.LinkedList;
import java.util.Scanner;

public class FuzzySearch {
    public static void main(String[] args) {
        FuzzySearch fuzzySearch = new FuzzySearch();
        fuzzySearch.run();
    }

    private BKTree bkTree = new BKTree();

    public FuzzySearch() {
        read();
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.println("Enter string:");
            String word = scanner.next();
            LinkedList<String> list = (MaxLinkedList<String>) bkTree.find(word);
            while (list.size() > 0){
                System.out.println(list.pop());
            }
        }
    }

    private void read() {
        System.out.println("Reading in words...");
        File file = new File("data/researchlv2num1.csv");
//        File file = new File("data/text.csv");
//        File file = new File("data/testlarge.csv");
        try {
            InputStream inputFS = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
            long start = System.currentTimeMillis();
            br.lines().map(s -> {
                String[] p = s.split(",");// a CSV has comma separated lines
                String item;
                item = p[0];
                return item;
            }).forEach(word -> bkTree.insert(word));
            long end = System.currentTimeMillis();
            System.out.println("Words read.");
            System.out.println("Time: " + (end - start));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
