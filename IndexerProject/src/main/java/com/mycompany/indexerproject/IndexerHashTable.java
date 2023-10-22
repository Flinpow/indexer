/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.indexerproject;

import java.util.LinkedList;
import java.util.Map.Entry;

public class IndexerHashTable<Integer,String> {
    private static int TABLE_SIZE; 
    private LinkedList<Entry<Integer, String>>[] table;

    public IndexerHashTable() {
    }
    
    public IndexerHashTable(int size) {
        //garante que o tamanho da tabela seja um número primo pois facilita na alocação dos valores.
        while(!isPrimo(size)) {
            size++;
        }
        TABLE_SIZE = size;
        table = new LinkedList[size];
        for(int i=0; i < size; i++) {
            table[i] = new LinkedList<>();
        }
    }
    
    public void put(int key, String value) {
        int index = getIndex(key);
        LinkedList<Entry<Integer, String>> slot = table[index];
        
        for (Entry<Integer, String> entry : slot) {
            if (entry.getKey().equals(key)) {
                entry.setValue(value);
                return;
            }
        }
    }
    
    private int getIndex(int key) {
        return key % TABLE_SIZE;
    }
    
    public static boolean isPrimo(int number) {
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
    
}
