/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.indexerproject;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Indexer {

    public static final String FREQ_OPERATION = "freq";
    public static final String FREQWORD_OPERATION = "freq-word";
    public static final String SEARCH_OPERATION = "search";

    public static void main(String[] args) {
        //Veriica o tipo da operação passada no primeiro parâmetro de entrada - arg[0]
 

       try {
        if (args[0].contains(FREQ_OPERATION) && !args[0].contains(FREQWORD_OPERATION)){
            freqNFile(args[1], args[2]);
        } else if(args[0].contains(FREQWORD_OPERATION)) {
            freqWordNFile(args[1], args[2]);    
        } else if (args[0].contains(SEARCH_OPERATION)) {
            searchTermNFile(args[1], args[2]);
        }else {
            System.out.println("Forneça um argumento válido");
        }
       }catch(ArrayIndexOutOfBoundsException e) {
           System.out.println("Forneça todos os 3 argumentos para a execução correta do indexador");
       }
    }

    private static void freqNFile(String n, String filePath) {
        System.out.println("Iniciando operação --freq N ARQUIVO");
        IndexerHashTable hashTable = new IndexerHashTable();
        try {
            // apenas testando a leitura e a contagem de palavras dentro do arquivo. ainda não faz o procedimento necessário pro trabalho
            int wordsNumber = Integer.valueOf(n);
            String content = readFile(filePath, StandardCharsets.UTF_8);

            Pattern pattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = pattern.matcher(content);
            int count = 0;

            while (matcher.find()) {
                String currentWord = matcher.group();
                if (currentWord.length() > 2) {
                    currentWord = sanitazeString(currentWord);
                    hashTable.put(currentWord.hashCode(), currentWord);
                    
                }
                System.out.println(matcher.group());
            }

            System.out.println("o número de palavras no arquivo é: " + count);
        } catch (NumberFormatException e) {
            System.out.println("Forneça um número válido para a operação de busca");
        } catch (AccessDeniedException e) {
            System.out.println("Para desta operação forneca o caminho para um arquivo de texto");
        }
    }

    private static void freqWordNFile(String word, String filePath) {
        System.out.println("Operação --freq-word PALAVRA ARQUIVO");
        IndexerHashTable hashTable = new IndexerHashTable();
        try {
            // apenas testando a leitura e a contagem de palavras dentro do arquivo. ainda não faz o procedimento necessário pro trabalho
            String content = readFile(filePath, StandardCharsets.UTF_8);

            Pattern pattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = pattern.matcher(content);
            int count = 0;

            while (matcher.find()) {
                 String currentWord = matcher.group();
                if (currentWord.length() > 2) {
                    currentWord = sanitazeString(currentWord);
                    hashTable.put(currentWord.hashCode(), currentWord);
                }
            }
            System.out.println("a palavra {" + word + "} aparece " + hashTable.getWordsCount(sanitazeString(word).hashCode()) + "x dentro do arquivo");
        } catch (AccessDeniedException e) {
            System.out.println("Para desta operação forneca o caminho para um arquivo de texto");
        }

    }

    private static void searchTermNFile(String word, String filePath) {
        System.out.println("Operação --search TERMO ARQUIVO");
        File[] files = getFiles(filePath);
        if (files != null) {
            System.out.println(files.length + " arquivos presentes no diretório " + filePath);
        } else {
            System.out.println("Diretório inexsistente");
        }
    }

    //retorna o conteúdo do arquivo em formato de String. utilizando nos casos de --freq e --freq-word
    static String readFile(String path, Charset encoding) throws AccessDeniedException {
        byte[] encoded = null;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException ex) {
            System.out.println("Não foi possivel ler o arquivo");
        }
        return encoding.decode(ByteBuffer.wrap(encoded)).toString();
    }

    // traz todos os arquivos presentes no diretório passado por parâmetro na operação de --search
    private static File[] getFiles(String pathName) {
        File[] arquivos = null;

        File path = new File(pathName);
        arquivos = path.listFiles();
        return arquivos;
    }

    private static String sanitazeString(String s) {
        // retira todos os caracteres que não sejam letras
        String str = new String();
        str = s.toLowerCase().replaceAll("[^a-zA-Z]", "");
        return str;

    }
}
