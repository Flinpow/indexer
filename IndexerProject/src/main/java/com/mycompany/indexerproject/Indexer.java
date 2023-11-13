package com.mycompany.indexerproject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.exec.ExecuteException;

public class Indexer {

    public static final String FREQ_OPERATION = "freq";
    public static final String FREQWORD_OPERATION = "freq-word";
    public static final String SEARCH_OPERATION = "search";

    public static void main(String[] args) {
        //Veriica o tipo da operação passada no primeiro parâmetro de entrada - arg[0]

        try {
            if (args[0].contains(FREQ_OPERATION) && !args[0].contains(FREQWORD_OPERATION)) {
                freqNFile(args[1], args[2]);
            } else if (args[0].contains(FREQWORD_OPERATION)) {
                freqWordNFile(args[1], args[2]);
            } else if (args[0].contains(SEARCH_OPERATION)) {
                searchTermNFile(args[1], args[2]);
            } else {
                System.out.println("Forneça um argumento válido");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Forneça todos os 3 argumentos para a execução correta do indexador");
        }catch (ExecuteException e) {
            System.out.println("Adicone a propriedade ");
        }
    }

    private static void freqNFile(String n, String filePath) throws ExecuteException {
        System.out.println("Iniciando operação --freq N ARQUIVO");
        IndexerHashTable hashTable = new IndexerHashTable();
        try {
            int wordsNumber = Integer.valueOf(n);
            String content = readFile(filePath);

            Pattern pattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String currentWord = matcher.group();
                currentWord = sanitazeString(currentWord);
                if (currentWord.length() >= 2) {
                    hashTable.put(currentWord.hashCode(), currentWord);
                }
            }
            freqNFileOperation(hashTable, wordsNumber);
        } catch (NumberFormatException e) {
            System.out.println("Forneça um número válido para a operação de busca");
//        } catch (AccessDeniedException e) {
//            System.out.println("Para desta operação forneca o caminho para um arquivo de texto");
        } 
    }

    private static void freqWordNFile(String word, String filePath) throws ExecuteException {
        System.out.println("Iniciando operação --freq-word PALAVRA ARQUIVO");
        IndexerHashTable hashTable = new IndexerHashTable();
        String sanitezedWord = sanitazeString(word);
        try {
            String content = readFile(filePath);

            Pattern pattern = Pattern.compile("\\b\\w+\\b");
            Matcher matcher = pattern.matcher(content);

            while (matcher.find()) {
                String currentWord = matcher.group();
                currentWord = sanitazeString(currentWord);
                if (currentWord.length() >= 2) {                    
                    hashTable.put(currentWord.hashCode(), currentWord);
                }
            }
            System.out.println("a palavra {" + word + "} aparece " + hashTable.getWordsCount(sanitezedWord.hashCode(), sanitezedWord) + "x dentro do arquivo");
        } catch (Exception e) {
            System.out.println("Para desta operação forneca o caminho para um arquivo de texto");
        }

    }

    private static void searchTermNFile(String word, String directoryPath) throws ExecuteException {
        System.out.println("Iniciando operação --search TERMO ARQUIVO");
        List<File> files = getFiles(directoryPath);
        IndexerHashTable hashTable = new IndexerHashTable();
        
        if (files != null && files.size() > 0) {
            for (File file : files) {
                int wordsCount = 0;
                String filePath = file.getAbsolutePath();
                String content = readFile(filePath);

                Pattern pattern = Pattern.compile("\\b\\w+\\b");
                Matcher matcher = pattern.matcher(content);

                while (matcher.find()) {
                    String currentWord = matcher.group();
                    currentWord = sanitazeString(currentWord);
                    if (currentWord.length() >= 2) {
                        hashTable.put(currentWord.hashCode(), currentWord);
                        wordsCount++;
                    }
                }
                System.out.println(wordsCount + " palavras no arquivo " + file.getName());
            }
            System.out.println(files.size() + " arquivos presentes no diretório " + directoryPath);
        } else {
            System.out.println("Diretório vazio ou inexsistente");
        }
    }

    //retorna o conteúdo do arquivo em formato de String. utilizando nos casos de --freq e --freq-word
     public static String readFile(String filePath) {
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            
        } 

        return content.toString();
    }

    // traz todos os arquivos presentes no diretório passado por parâmetro na operação de --search
    private static  List<File> getFiles(String pathName) {
        List<File> arquivos = null;
        File path = new File(pathName);

        arquivos = Arrays.asList(path.listFiles());
        return arquivos;
    }

    private static String sanitazeString(String s) {
        // retira todos os caracteres que não sejam letras
        String str = new String();
        str = s.toLowerCase().replaceAll("[^a-zA-Z]", "");
        return str;

    }

    private static void freqNFileOperation(IndexerHashTable hashTable, int wordsNumber) {
        List<OcurrenceInFileEntry> entries = new ArrayList();
        LinkedList<IndexerHashTableEntry<Integer, String>>[] tableEntries = hashTable.getTable();
        int count = 0;

        for (LinkedList<IndexerHashTableEntry<Integer, String>> tableEntrie : tableEntries) {
            if (tableEntrie != null) {
                for (IndexerHashTableEntry<Integer, String> entry : tableEntrie) {
                    String word = entry.getValue();
                    int ocurrenceInFile = hashTable.getWordsCount(entry.getKey(), word);
                    OcurrenceInFileEntry fileEntry = new OcurrenceInFileEntry(word, ocurrenceInFile);
                    if (!entries.contains(fileEntry)) {
                        entries.add(fileEntry);
                    }
                }
            }
        }
        // ordena a lista (decrescente) a partir do atributo ocurrence
        List<OcurrenceInFileEntry> sortedEntries = entries.stream()
                .sorted(Comparator.comparingInt(OcurrenceInFileEntry :: getOcurrence).reversed())
                .collect(Collectors.toList());
        for (OcurrenceInFileEntry entry : sortedEntries) {
            if(count >= wordsNumber) break;
            System.out.println(entry.getWord() + ": " + entry.getOcurrence() + "x");
            count++;
        }
    }
}
