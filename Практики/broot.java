package Практики;

public class broot {

    private static char [] alphabet = new char[]{
            'а','б','в','г','д','е','ё','ж',
            'з','и','й','к','л','м','н','о',
            'п','р','с','т','у','ф','х','ц',
            'ч','ш','щ','ъ','ы','ь','э','ю','я'
    };

    public static void main(String[] args) {
        String source = "лол,лиёл,лкдл,аяжа";
        System.out.println(source);
        System.out.println(HashFunc(source));
    }


    
    public static String HashFunc(String input){
        String hash = "";
        int sum = 0,first=0,last=0;
        input = input.toLowerCase();
        String [] words = input.split("\\s|[.!,?\\-]");
        for (int i = 0;i<words.length;i++){
            for(int j = 0;j<words[i].length();j++){
                for (int k = 0;k<alphabet.length;k++) {
                    if(words[i].charAt(0)==alphabet[k]){
                        first=k;
                    }
                    if(words[i].charAt(words[i].length()-1)==alphabet[k]){
                        last=k+1;
                    }

                    if (words[i].charAt(j) == alphabet[k]) {
                        sum += k;
                    }
                }
            }
            sum = sum * ((last-first));
            hash +=Integer.toString(sum)+" " ;
            sum = 0;
        }
        return hash;
    }

}