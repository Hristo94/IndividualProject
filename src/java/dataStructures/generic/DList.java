package dataStructures.generic;

import graph.Vertex;

public class DList {

    private int size;
    private Vertex first;

    public DList(){size = 0; first = null;}

    public int size(){return size;}

    public boolean isEmpty(){
        return (size == 0);
    }

    // simple addFront
    public void insert(Vertex v){
        if (first == null){
            first = v;
            v.setPrev(null);
            v.setNext(null);
        }
        else {
            insertAtFirst(v);
        }
        size++;
    }

    private void insertAtFirst(Vertex node){
        node.setNext(first);
        first.setPrev(node);
        node.setPrev(null);
        first = node;
    }

    public Vertex poll() {
        Vertex v = first;
        remove(v);
        return v;
    }

    // handles the cases when the node is at the front, at the end or between nodes
    public void remove(Vertex node){
        if (size == 1){first =  null;}
        else if (node == first){
            first = first.getNext();
            first.setPrev(null);
        }
        else if (node.getNext() == null ){
            node.getPrev().setNext(null);
        }
        else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
        size--;
    }

    // scan the elements to find the minimum
    public Vertex findMin() {
        Vertex min = first;
        Vertex cursor = min;
        while(cursor != null) {
            if(cursor.getDistance() < min.getDistance()) {
                min = cursor;
            }
            cursor = cursor.getNext();
        }
        return min;
    }
}