package main;

import java.util.LinkedList;

public class MaxLinkedList<R> extends LinkedList<R> {
    private int size;

    public MaxLinkedList(int size) {
        this.size = size;
    }

    @Override
    public boolean add(R r) {
        if(size() == size)
            removeFirst();
        return super.add(r);
    }
}
