public class caesarCipher {
  
    public static String encode(String source, int key){
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

    public static String decode(String source, int key){
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

}
