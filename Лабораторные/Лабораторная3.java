package Лабораторные;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class Лабораторная3 {
    

    public static void main(String args[]){
        BigInteger p, q;
        BigInteger n;
        BigInteger funcEiler;
        BigInteger e,d;
        BigInteger[] encode;
        String source, decode;
        p = getPorQ(300);
        q = getPorQ(300);
        n = getN(p, q);
        funcEiler = getFuncEiler(p, q);
        e = getE(funcEiler);
        d = getD(e, funcEiler);
        source = "i'm hackman";
        encode = encode(source.getBytes(), e, n);
        decode = new String(decode(encode, d, n), StandardCharsets.UTF_8);
        System.out.println("e:\t" + e);
        System.out.println("d:\t" + d);
        System.out.println("n:\t" + n);
        System.out.println("Source:\t" + source);
        System.out.println("Decode:\t" + decode);
    }

    public static byte[] decode(BigInteger[] source, BigInteger d, BigInteger n){
        byte[] output = new byte[source.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = source[i].modPow(d, n).byteValue();
        }
        return output;
    }

    public static BigInteger[] encode(byte[] source, BigInteger e, BigInteger n){
        BigInteger[] output = new BigInteger[source.length];
        for (int i = 0; i < output.length; i++) {
            output[i] = new BigInteger(String.valueOf(source[i])).modPow(e, n);
        }
        return output;
    }

    private static BigInteger getPorQ(int order){
        return simpleGenerate.getRandomSimpleBigInteger(order, 2000);
    }

    private static BigInteger getN(BigInteger p, BigInteger q){
        return p.multiply(q);
    }

    private static BigInteger getFuncEiler(BigInteger p, BigInteger q){
        return p.subtract(BigInteger.valueOf(1)).multiply(q.subtract(BigInteger.valueOf(1)));
    }

    private static BigInteger getE(BigInteger funcEiler){
        BigInteger testValue = simpleGenerate.getRandomSimpleBigInteger(1, 50);
        if(funcEiler.compareTo(testValue) == -1){
            return testValue;
        }else if(funcEiler.compareTo(testValue) == 1 && 
                !funcEiler.mod(testValue).equals(BigInteger.valueOf(0))){
            return testValue;
        }else{
            return getE(funcEiler);
        }
    }

    private static BigInteger getD(BigInteger e, BigInteger funcEiler){
        return e.modPow(BigInteger.valueOf(-1), funcEiler);
    }
}