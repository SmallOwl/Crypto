package Лабораторные;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Лабораторная4 {

    public static void main(String args[]) throws NoSuchAlgorithmException {
        //Факторы протокола
        BigInteger q = getQ(100, 2000);
        BigInteger n = getN(q);
        BigInteger g = getG(n);
        BigInteger k = getK();
        System.out.println("---------------------------------------------------");
        System.out.println("Факторы протокола");
        System.out.println("q:\t" + q);
        System.out.println("n:\t" + n);
        System.out.println("g:\t" + g);
        System.out.println("k:\t" + k);
        System.out.println("---------------------------------------------------");
        //Шаг 1 - Регистрация
        System.out.println("Шаг 1 - Регистрация");
        //Генерация на клиенте
        String clientS = getSalt(300);
        String clientP = "myPassword";
        byte[] clientX = hash(clientS, clientP);
        BigInteger clientV = getV(g, clientX, n);
        System.out.println("Генерация на клиенте");
        System.out.println("clientS:\t" + clientS);
        System.out.println("clientP:\t" + clientP);
        System.out.println("clientX:\t");
        for (byte b : clientX) {
            System.out.print(b + "\s");
        }
        System.out.println();
        System.out.println("clientV:\t" + clientV);
        //Сохранение на сервере
        String serverI = "myUserName";
        String serverS = clientS;
        BigInteger serverV = clientV;
        System.out.println("Сохранение на сервере");
        System.out.println("serverI:\t" + serverI);
        System.out.println("serverS:\t" + serverS);
        System.out.println("serverV:\t" + serverV);
        System.out.println("---------------------------------------------------");
        //Шаг 2 - Аутенфикация
        //Фаза 1
        System.out.println("Шаг 2 - Аутенфикация");
        System.out.println("Фаза 1");
        BigInteger clientSmallA = BigInteger.valueOf(new Random().nextInt());
        BigInteger clientA = getA(g, clientSmallA, n);
        System.out.println("clientSmallA:\t" + clientSmallA);
        System.out.println("clientA:\t" + clientA);
        if(clientA.compareTo(BigInteger.ZERO) == 0){
            System.out.println("A == 0");
            System.exit(-1);
        }
        BigInteger serverSmallB = BigInteger.valueOf(new Random().nextInt());
        BigInteger serverB = getB(k, serverV, g, serverSmallB, n);
        System.out.println("serverSmallB:\t" + serverSmallB);
        System.out.println("serverB:\t" + serverB);
        clientS = serverS;
        BigInteger clientB = serverB;
        if(serverB.compareTo(BigInteger.ZERO) == 0){
            System.out.println("B == 0");
            System.exit(-1);
        }
        byte[] hab = hash(clientA.toString(), serverB.toString());
        if(hab.length == 1 && hab[0] == 0){
            System.out.println("H(A,B) == 0");
            System.exit(-1);
        }
        System.out.println("H(A,B):\t");
        for (byte b : hab) {
            System.out.print(b + "\s");
        }
        System.out.println();


        clientX = hash(clientS, clientB.toString());
        clientS = getClientS(clientB, k, g, clientX, n, clientSmallA, hab);
        byte[] clientK = hash("", clientS);
        serverS = getServerS(clientA, serverV, n, serverSmallB, hab);
        byte[] serverK = hash("", serverS);
        System.out.println("clientK:\t");
        for (byte b : clientK) {
            System.out.print(b + "\s");
        }
        System.out.println();
        System.out.println("----------------------------------------------");
        System.out.println("serverK:\t");
        for (byte b : serverK) {
            System.out.print(b + "\s");
        }
        System.out.println();
    }

    private static String getServerS(BigInteger a, BigInteger v, BigInteger n, BigInteger b, byte[] u){
        return a.multiply(v.modPow(new BigInteger(u), n)).modPow(b, n).toString();
    }

    private static String getClientS(BigInteger b, BigInteger k, BigInteger g, byte[] x, BigInteger n, BigInteger a, byte[] u){
        return b.subtract(k.multiply(g.modPow(new BigInteger(x), n))).modPow(a.add(new BigInteger(u).multiply(new BigInteger(x))), n).toString();
    }

    private static BigInteger getB(BigInteger k, BigInteger v, BigInteger g, BigInteger b, BigInteger n){
        return k.multiply(v).add(g.modPow(b, n)).mod(n);
    }

    private static BigInteger getA(BigInteger g, BigInteger a, BigInteger n){
        return g.modPow(a, n);
    }

    private static BigInteger getV(BigInteger g, byte[] x, BigInteger n){
        String result = "";
        for(int i = 0; i < x.length; i++){
            result = result + g.modPow(new BigInteger(String.valueOf(x[i])), n);
        }
        return new BigInteger(result);
    }

    private static BigInteger getQ(int order, int checkModSimpleMax) {
        BigInteger checkBigInt = simpleGenerate.getRandomSimpleBigInteger(order, 2000);
        while(true){
            if(simpleGenerate.checkBigIntegerModSimple(checkBigInt.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE), checkModSimpleMax) && 
            simpleGenerate.checkSimpleMillerRabin(checkBigInt.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE), (int)(Math.log10(order)/Math.log(2)))){
                return checkBigInt;
            }
            checkBigInt = simpleGenerate.getRandomSimpleBigInteger(order, 2000);
        }
    }

    private static BigInteger getN(BigInteger q) {
        return q.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE);
    }

    private static BigInteger getG(BigInteger p){
        BigInteger g = simpleGenerate.getRandomBigInteger(BigInteger.valueOf(2), BigInteger.valueOf(9));
        while(!g.modPow(p.subtract(BigInteger.ONE).multiply(BigInteger.valueOf(-1)), p).equals(BigInteger.ONE)){
            g = simpleGenerate.getRandomBigInteger(BigInteger.valueOf(2), BigInteger.valueOf(9));
        }
        return g;
    }

    private static BigInteger getK() {
        return BigInteger.valueOf(3);
    }

    private static String getSalt(int order) {
        return simpleGenerate.getRandomBigInteger(order, false).toString();
    }

    private static byte[] hash(String salt, String password) throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("SHA-512").digest((password + salt).getBytes());
    }
}
