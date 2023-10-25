package com.mycompany.indexerproject;

import java.util.LinkedList;

public class IndexerHashTable<Integer, String> {

    private static final int INITIAL_CAPACITY = 5;
    private static final double LOAD_FACTOR = 0.75;
    public static int TABLE_SIZE;
    private LinkedList<IndexerHashTableEntry<Integer, String>>[] table;

    public IndexerHashTable() {
        TABLE_SIZE = 0;
        table = new LinkedList[INITIAL_CAPACITY];
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            table[i] = new LinkedList<>();
        }
    }

    public void put(int key, String value) {
        if (TABLE_SIZE + 1 > LOAD_FACTOR * table.length) {
            // Redimensionar a tabela se o fator de carga for atingido.
            resize();
        }
        int index = getIndex(key, table.length);
        LinkedList<IndexerHashTableEntry<Integer, String>> slot = table[index];

        if (slot == null) {
            slot = new LinkedList<>();
            table[index] = slot;
        }

        slot.add(new IndexerHashTableEntry(key, value));
        table[index] = slot;
        TABLE_SIZE++;
    }

    public String get(int key) {
        int index = getIndex(key, table.length);
        LinkedList<IndexerHashTableEntry<Integer, String>> slot = table[index];

        for (IndexerHashTableEntry<Integer, String> entry : slot) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int getWordsCount(int key, String word) {
        int count = 0;
        int index = getIndex(key, table.length);
        LinkedList<IndexerHashTableEntry<Integer, String>> slot = table[index];
        for (IndexerHashTableEntry<Integer, String> entry : slot) {
            if (entry.getValue() != null && entry.getValue().equals(word)) {
                count++;
            }
        }
        return count;
    }

    private int getIndex(int key, int tableSize) {
        return (Math.abs(key)) % tableSize;
    }

    private static boolean isPrimo(int number) {
        if (number <= 1) {
            return false;
        }

        if (number <= 3) {
            return true;
        }

        for (int i = 2; i * i <= number; i++) {
            if (number % i == 0) {
                return false;
            }
        }

        return true;
    }

    private void resize() {
        int newSize = table.length * 2;
        //garante que o tamanho da tabela seja um número primo pois facilita na alocação dos valores.
        while (!isPrimo(newSize)) {
            newSize++;
        }

        LinkedList<IndexerHashTableEntry<Integer, String>>[] newTable = new LinkedList[newSize];
        for (LinkedList<IndexerHashTableEntry<Integer, String>> bucket : table) {
            if (bucket != null) {
                for (IndexerHashTableEntry<Integer, String> entry : bucket) {
                    int newIndex = getIndex((int) entry.getKey(), newSize);
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }
                    newTable[newIndex].add(entry);
                }
            }
        }
        table = newTable;
    }

    public LinkedList<IndexerHashTableEntry<Integer, String>>[] getTable() {
        return table;
    }
    
}
