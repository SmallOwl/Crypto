package Лабораторные;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Лабораторная1 {
    
    public static void main(String[] args){
        int key = 3;
        int wordSize = 3;
        String source = readFile("txt/Том1.txt").toLowerCase();
        String encode = caesarCipher.encode(source, key);
        writeFile("txt/Encode.txt", encode);
        Map<String, Double> freqBase = getFrequency(
            readFile("txt/Том1.txt").toLowerCase() + readFile("txt/Том2.txt").toLowerCase() 
            + readFile("txt/Том3.txt").toLowerCase() + readFile("txt/Том4.txt").toLowerCase(), 
            wordSize);
        Map<String, Double> freqEncode = getFrequency(encode, wordSize);
        System.out.println("Выберите способ дешифрования:");
        System.out.println("1.Замена всех букв в соответствии с их частотой;");
        System.out.println("2.Нахождение сдвига по самым популярным символам");
        Scanner in = new Scanner(System.in);
        int choose = in.nextInt();
        in.close();
        if(choose == 1){
            writeFile("txt/Decode.txt", freqDecode(getAssociation(freqBase, freqEncode), encode, wordSize));
        }else if(choose == 2){
            writeFile("txt/Decode.txt", caesarCipher.decode(encode, getShift(freqBase, freqEncode)));
        }
    }

    private static int getShift(Map<String, Double> freqBase, Map<String, Double> freqEncode){
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        Entry<String, Double> maxEncode = null;
        Entry<String, Double> maxBase = null;
        for (Entry<String, Double> checkEntry : freqEncode.entrySet()) {
            if(maxEncode == null || checkEntry.getValue() > maxEncode.getValue()){
                maxEncode = checkEntry;
            }
        }
        for (Entry<String, Double> checkBase : freqBase.entrySet()) {
            if(maxBase == null || checkBase.getValue() > maxBase.getValue()){
                maxBase = checkBase;
            }
        }
        return Math.abs(alphabet.indexOf(maxBase.getKey().toCharArray()[0]) - alphabet.indexOf(maxEncode.getKey().toCharArray()[0]));
    }

    //Получение частот используемых символов/подстрок
    private static Map<String, Double> getFrequency(String source, int wordSize){
        Map<String, Double> frequency = new HashMap<String, Double>();
        int counter = 0;
        String checkWord = "";
        for(int i = 0; i < source.length(); i++){
            if(Character.UnicodeBlock.of(source.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)){
                if(checkWord.length() + 1 == wordSize){
                    checkWord = checkWord + source.charAt(i);
                    Double lastFreq = frequency.get(checkWord);
                    if(lastFreq != null){
                        frequency.put(checkWord, Double.valueOf(lastFreq + 1));
                    }else{
                        frequency.put(checkWord, Double.valueOf(1));
                    }
                    checkWord = "";
                    counter++;
                }else{
                    checkWord = checkWord + source.charAt(i);
                }
            }
        }
        if(!checkWord.equals("")){
            frequency.put(checkWord, Double.valueOf(1));
        }
        for (Entry<String, Double> checkSeq : frequency.entrySet()) {
            frequency.put(checkSeq.getKey(), Double.valueOf(checkSeq.getValue()*100/counter));
        }
        return frequency;
    }

    //Получение таблицы соответствий символов
    private static Map<String, String> getAssociation(Map<String, Double> freqBase, Map<String, Double> freqEncode){
        Map<String, String>associatedStrings = new HashMap<String, String>();
        Entry<String, Double> minEntry = null;
        for (Entry<String, Double> entryEncode : freqEncode.entrySet()) {
            for (Entry<String, Double> entryBase : freqBase.entrySet()) {
                if(minEntry == null || 
                Math.abs(entryEncode.getValue().doubleValue() - entryBase.getValue().doubleValue()) < 
                Math.abs(entryEncode.getValue().doubleValue() - minEntry.getValue().doubleValue())){
                    minEntry = entryBase;
                }
            }
            associatedStrings.put(entryEncode.getKey(), minEntry.getKey());
            freqBase.remove(minEntry.getKey());
        }
        return associatedStrings;
    }

    private static String freqDecode(Map<String,String> associatedStrings, String source, int wordSize){
        char[] result = source.toCharArray();
        String checkWord = "";
        for(int i = 0; i < result.length; i++){
            if(Character.UnicodeBlock.of(source.charAt(i)).equals(Character.UnicodeBlock.CYRILLIC)){
                if(checkWord.length() + 1 == wordSize){
                    checkWord = checkWord + source.charAt(i);
                    char[] replase = associatedStrings.get(checkWord).toCharArray();
                    int counter = wordSize - 1;
                    int k = i;
                    while(counter >= 0){
                        if(Character.UnicodeBlock.of(result[k]).equals(Character.UnicodeBlock.CYRILLIC)){
                            result[k] = replase[counter];
                            counter--;
                        }
                        k--;
                    }
                    checkWord = "";
                }else{
                    checkWord = checkWord + source.charAt(i);
                }
            }
        }
        if(!checkWord.equals("")){
            if(associatedStrings.get(checkWord) != null){
                char[] replase = associatedStrings.get(checkWord).toCharArray();
                for(int j = result.length - checkWord.length(); j < result.length; j++){
                    result[j] = replase[j - (result.length - wordSize + 1)];
                }
            }  
        }
        return String.valueOf(result);
    }

    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeFile(String filePath, String source){
        try {
            Files.write(Paths.get(filePath), source.getBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}