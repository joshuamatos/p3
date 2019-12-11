package edu.uprm.cse.datastructures.cardealer.util;

import java.util.Comparator;


public abstract class BTree<K,V> implements Map<K,V>{

    int currentSize;
    int deleted;
    TreeNode root;
    Comparator<K> keyComparator;

    class MapEntry implements Comparable<MapEntry>{
        K key;
        V value;
        boolean deleted;
        Comparator<K> keyComparator;
        MapEntry(K key, V value, Comparator<K> keyComparator){
            this.keyComparator = keyComparator;
            this.key = key;
            this.value = value;
            deleted = false;
        }
        @Override
        public int compareTo(MapEntry o) {
            return keyComparator.compare(this.key, o.key);
        }
    }

    class MapEntryComparator implements Comparator<MapEntry>{

        @Override
        public int compare(MapEntry o1, MapEntry o2) {
            return o1.compareTo(o2);
        }
    }

    class TreeNode{
        SortedList<MapEntry> entries;
        TreeNode left, right, center, parent, temp;
        Comparator<K> comparator;
        TreeNode(K keyA, K keyB, V a, V b, Comparator<K> comparator){
            this.comparator = comparator;
            this.entries = new CircularSortedDoublyLinkedList<>(new MapEntryComparator());
            this.entries.add(new MapEntry(keyA, a, comparator));
            if(keyB != null) {
                this.entries.add(new MapEntry(keyB, b, comparator));
            }
        }
        TreeNode(MapEntry entryA, MapEntry entryB, Comparator<K> comparator){
            this.comparator = comparator;
            this.entries = new CircularSortedDoublyLinkedList<>(new MapEntryComparator());
            this.entries.add(entryA);
            if(entryB != null && !entryB.deleted){
                this.entries.add(entryB);
            }
        }
    }

    public BTree(Comparator<K> keyComparator){
        this.currentSize = 0;
        this.deleted = 0;
        this.keyComparator = keyComparator;
    }

    abstract boolean isLeaf(TreeNode treeNode);

    /*
        Splits a node with 3 keys into three nodes. Center will be the root of the other two. Then connect that root
        with it's parent. Call Split recursively as necessary up until the root.
    */
    abstract void split(TreeNode treeNode);

    public void print() {
        this.printAux(this.root, 0);
    }

    private void printAux(TreeNode N, int i) {
        if (N != null) {
            this.printAux(N.right, i + 4);
            if(N.entries.size() > 1) {
                System.err.print((i/4)+"|");
                for (int j=0; j < i; ++j) {
                    System.err.print(" ");
                }
                System.err.println(N.entries.last().value);
            }
            this.printAux(N.center, i + 4);
            System.err.print((i/4)+"|");
            for (int j=0; j < i; ++j) {
                System.err.print(" ");
            }

            System.err.println(N.entries.first().value);

            this.printAux(N.left, i + 4);
        }

    }





}
