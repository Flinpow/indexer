/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.indexerproject;

import java.util.LinkedList;

public class IndexerHashTable<Integer, String> {

    private static final int INITIAL_CAPACITY = 5;
    private static final double LOAD_FACTOR = 0.75;
    public static int TABLE_SIZE;
    private LinkedList<Entry<Integer, String>>[] table;

    public IndexerHashTable() {

        TABLE_SIZE = 0;
        table = new LinkedList[INITIAL_CAPACITY];
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            table[i] = new LinkedList<Entry<Integer, String>>();
        }
    }

    public void put(int key, String value) {
        if (TABLE_SIZE + 1 > LOAD_FACTOR * table.length) {
            // Redimensionar a tabela se o fator de carga for atingido.
            resize();
        }
        int index = getIndex(key, table.length);
        LinkedList<Entry<Integer, String>> slot = table[index];

        if (slot == null) {
            slot = new LinkedList<>();
            table[index] = slot;
        }
//        for (Entry<Integer, String> entry : slot) {
//            if (entry.getKey().equals(key)) {
//                entry.setValue(value);
//                return;
//            }
//        }

        slot.add(new Entry(key, value));
        table[index] = slot;
        TABLE_SIZE++;
    }

    public String get(int key) {
        int index = getIndex(key, table.length);
        LinkedList<Entry<Integer, String>> slot = table[index];

        for (Entry<Integer, String> entry : slot) {
            if (entry.getKey().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int getWordsCount(int key) {
        int index = getIndex(key, table.length);
        LinkedList<Entry<Integer, String>> slot = table[index];
        return slot == null ? 0 : slot.size();
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
        System.out.println("Redimensionando a tabela");
        int newSize = table.length * 2;
        //garante que o tamanho da tabela seja um número primo pois facilita na alocação dos valores.
        while (!isPrimo(newSize)) {
            newSize++;
        }

        LinkedList<Entry<Integer, String>>[] newTable = new LinkedList[newSize];
        for (LinkedList<Entry<Integer, String>> bucket : table) {
            if (bucket != null) {
                for (Entry<Integer, String> entry : bucket) {
                    int newIndex = getIndex((int) entry.getKey(), newSize);
                    if (newTable[newIndex] == null) {
                        newTable[newIndex] = new LinkedList<>();
                    }
                    newTable[newIndex].add(entry);
                }
            }
        }
        table = newTable;
        System.out.println("a tabela agora tem o tamanho de [" + table.length + "]");
    }

}
