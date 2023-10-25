/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.indexerproject;

import java.util.Map;

public class IndexerHashTableEntry<Integer, String> implements Map.Entry<Integer, String> {

    private Integer key;
    private String value;
    private IndexerHashTableEntry<Integer,String> next;

    public IndexerHashTableEntry(Integer key, String value) {
        this.key = key;
        this.value = value;
    }
    
    
    @Override
    public Integer getKey() {
        return this.key;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public String setValue(String value) {
        this.value = value;
        return value;
    }

    public IndexerHashTableEntry<Integer, String> getNext() {
        return next;
    }

    public void setNext(IndexerHashTableEntry<Integer, String> next) {
        this.next = next;
    }
    
    
    
}
