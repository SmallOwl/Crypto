public class hash{ 
      
    public static void main(String args[]){
        String sourceA = "Try it a";
        String sourceB = "Try it b";
        String key = "SmallOwl";
        System.out.println("SourceA:\t" + sourceA);
        System.out.println("SourceB:\t" + sourceB);
        System.out.println("Key:\t" + key);
        System.out.println("-------------------------------------");
        System.out.println("Hash First A:\t" + hash_first(sourceA, key));
        System.out.println("Hash First B:\t" + hash_first(sourceB, key));
        System.out.println("Hash Second A:\t" + hash_second(sourceA, key));
        System.out.println("Hash Second B:\t" + hash_second(sourceB, key));
    }

    private static String hash_first(String source, String key){
        char a = key.charAt(0);
        char b = key.charAt(1);
        char c = key.charAt(2);
        char d = key.charAt(3);
        char e = key.charAt(4);
        char f = key.charAt(5);
        char g = key.charAt(6);
        char h = key.charAt(7);
        char temp;
        source = getSource(source);
        for(int i = 0; i < source.length() + 7; i+=1){
            temp = h;
            h = a;
            if(i < source.length()/2){
                a = (char)(e ^ source.charAt(i) ^ ((b & c) | ~b & d));
            }else if(i > source.length()/2 & i < source.length()){
                a = (char)(e ^ source.charAt(i) ^ ((b & c) | (b & d) | (c & d)));
            }else{ 
                a = (char)(b ^ c ^ d);
            }
            while(a < 65){
                a = (char)(a + 16);
            }
            while(a > 122){
                a = (char)(a - 16);
            }
            b = c;
            c = d;
            d = e;
            e = f;
            f = g;
            g = temp;
        }
        return String.valueOf(new char[]{a,b,c,d,e,f,g,h});
    }

    private static String hash_second(String source, String key){
        char a = key.charAt(0);
        char b = key.charAt(1);
        char c = key.charAt(2);
        char d = key.charAt(3);
        char e = key.charAt(4);
        char f = key.charAt(5);
        char g = key.charAt(6);
        char h = key.charAt(7);
        char temp;
        source = getSource(source);
        for(int i = 0; i < source.length() + 7; i+=1){
            temp = h;
            h = a;
            if(i < source.length()/2){
                a = (char)(e ^ source.charAt(i) ^ ((a & c) | ~b & a));
            }else if(i > source.length()/2 & i < source.length()){
                a = (char)(e ^ source.charAt(i) ^ ((b & c) | (a & d) | (c & a)));
            }else{ 
                a = (char)(b ^ c ^ d);
            }
            while(a < 65){
                a = (char)(a + 16);
            }
            while(a > 122){
                a = (char)(a - 16);
            }
            b = c;
            c = d;
            d = e;
            e = f;
            f = g;
            g = temp;
        }
        return String.valueOf(new char[]{a,b,c,d,e,f,g,h});
    }

    private static String getSource(String source){
        int length = source.length();
        if((source.length() % 32) > 30){
            while((source.length() % 32) != 0){
                source += " ";
            }
        }
        while((source.length() % 32) < 30){
            source += " ";
        }
        char a = (char)(length & 0x0000FFFF);
        char b = (char)((length & 0xFFFF0000) >> 16);
        source = source + b + a;
        return source; 
    }
}