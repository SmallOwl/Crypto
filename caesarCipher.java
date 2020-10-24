import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class caesarCipher {
   

    private static String encode(String source, int key){
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        char[] sourceChars = source.toLowerCase().toCharArray();
        for(int i = 0; i < sourceChars.length; i++){
            for(int j = 0; j < alphabet.length(); j++){
                if(sourceChars[i] == alphabet.charAt(j)){
                    sourceChars[i] = alphabet.charAt((j + key) % 33);
                    break;
                }
            }
        }
        return String.valueOf(sourceChars);
    }

    private static String decode(String source, int key){
        String alphabet = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";
        char[] sourceChars = source.toLowerCase().toCharArray();
        for(int i = 0; i < sourceChars.length; i++){
            for(int j = 0; j < alphabet.length(); j++){
                if(sourceChars[i] == alphabet.charAt(j)){
                    int newAlphabetIndex = (j - key) % 33;
                    if(newAlphabetIndex < 0){
                        newAlphabetIndex = 33 + newAlphabetIndex;
                    }
                    sourceChars[i] = alphabet.charAt(newAlphabetIndex);
                    break;
                }
            }
        }
        return String.valueOf(sourceChars);
    }

    private static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes( Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
