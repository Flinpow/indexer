/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.indexerproject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Indexer {
    public static final String FREQ_OPERATION = "freq";
    public static final String FREQWORD_OPERATION = "freq-word";
    public static final String SEARCH_OPERATION = "search";
     public static void main(String[] args) {

       try {
        if (args[0].contains(FREQ_OPERATION)){
            freqNFile(args[1], args[2]);
        } else if(args[0].contains(FREQWORD_OPERATION)) {
            
        } else if (args[0].contains(SEARCH_OPERATION)) {
            
        }else {
            System.out.println("Forneça um argumento válido");
        }
       }catch(Exception e) {
           System.out.println(e);
           System.out.println("Forneça todos os 3 argumentos para a execução correta do indexador");
       }
    }
    
    private static void freqNFile(String n, String file) throws IOException { 
        System.out.println("Iniciando operação --freq N ARQUIVO");
        try{
           int wordsNumber = Integer.valueOf(n);
           String content = readFile(file, StandardCharsets.UTF_8);
           
           Pattern pattern = Pattern.compile("\\b\\w+\\b");
           Matcher matcher = pattern.matcher(content);
           int count = 0;
           
           while(matcher.find()) {
               count++;
           }
           
            System.out.println("o número de palavras no arquivo é: " + count);
        } catch(NumberFormatException e) {
            System.out.println("Forneça um número válido para a operação de busca"); 
        }
    }
        
    
    private static void freqWordNFile() {
        System.out.println("Operação --freq-word PALAVRA ARQUIVO");
    }
    
    private static void searchTermNFile() {
        System.out.println("Operação --search TERMO ARQUIVO");
    }
    static String readFile(String path, Charset encoding) throws IOException 
{
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return encoding.decode(ByteBuffer.wrap(encoded)).toString();
}
}
