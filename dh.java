import java.math.BigInteger;

public class dh {
    
    public static void main(String[] args){
        BigInteger g;
        BigInteger p;
        BigInteger a;
        BigInteger b;
        BigInteger A;
        BigInteger B;
        BigInteger K1;
        BigInteger K2;
        a = getPrivateKey(100);
        b = getPrivateKey(100);
        p = getP(300);
        g = getG(p);
        A = getPublicKey(g, a, p);
        B = getPublicKey(g, b, p);
        K1 = getGeneralKey(A, b, p);
        K2 = getGeneralKey(B, a, p);
        System.out.println("Секретный ключ а:\t" + a);
        System.out.println("Секретный ключ b:\t" + b);
        System.out.println("Константа p:\t" + p);
        System.out.println("Константа g:\t" + g);
        System.out.println("Публичный ключ A:\t" + A);
        System.out.println("Публичный ключ B:\t" + B);
        System.out.println("Общий секретный ключ на основе A и b:\t" + K1);
        System.out.println("Общий секретный ключ на основе B и a:\t" + K2);
    }

    private static BigInteger getPrivateKey(int order){
        return simpleGenerate.getRandomBigInteger(order, false);
    }

    private static BigInteger getP(int order){
        return simpleGenerate.getRandomSimpleBigInteger(order, 2000);
    }

    private static BigInteger getG(BigInteger p){
        BigInteger g = simpleGenerate.getRandomBigInteger(BigInteger.valueOf(2), BigInteger.valueOf(9));
        while(!g.modPow(p.subtract(BigInteger.ONE).multiply(BigInteger.valueOf(-1)), p).equals(BigInteger.ONE)){
            g = simpleGenerate.getRandomBigInteger(BigInteger.valueOf(2), BigInteger.valueOf(9));
        }
        return g;
    }

    private static BigInteger getPublicKey(BigInteger g, BigInteger privateKey, BigInteger p){
        return g.modPow(privateKey, p);
    }

    private static BigInteger getGeneralKey(BigInteger publicKey, BigInteger privateKey, BigInteger p){
        return publicKey.modPow(privateKey, p);
    }
}