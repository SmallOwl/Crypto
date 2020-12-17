package Лабораторные;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Практики.hash;

public class Лабораторная4 {

    //Факторы протокола
    static BigInteger N;
    static BigInteger g;
    static BigInteger k;
    //Данные клиента
    static String clientSalt;
    static BigInteger clientX;
    static String clientPassword;
    static BigInteger clientV;
    static BigInteger clientA;
    static BigInteger clientB;
    static BigInteger clientU;
    static BigInteger clientS;
    static BigInteger clientSmallA;
    static BigInteger clientK;
    static BigInteger clientR;
    //Данные сервера
    static String serverSalt;
    static BigInteger serverV;
    static String serverI;
    static BigInteger serverA;
    static BigInteger serverB;
    static BigInteger serverU;
    static BigInteger serverS;
    static BigInteger serverK;
    static BigInteger serverR;

    public static void main(String args[]) throws NoSuchAlgorithmException {
        protocolFactorsInit(5);
        //Этап - 1 - Регистрация
        clientRegistration();
        serverRegistrationHandler(clientSalt, clientV);
        //Этап - 2 - Аутенфикация
        //Фаза - 1
        clientSendIandA();
        serverHandlerIandA(serverI, clientA, serverV);
        clientBuildKey(serverB);
        //Фаза - 2
        getClientMandR();
        getServerMandR();
    }

    //Этап - 1 - Регистрация
    //Клиентская часть
    private static void clientRegistration() throws NoSuchAlgorithmException {
        clientSalt = getSalt(300);
        clientPassword = "myPass";
        clientX = new BigInteger(hash(clientSalt, clientPassword));
        clientV = g.modPow(clientX, N);
    }

    //Этап - 1 - Регистрация
    //Серверная часть
    private static void serverRegistrationHandler(String clientS, BigInteger clientV){
        serverSalt = clientSalt;
        serverI = "myUserName";
        serverV = clientV;
    }

    //Этап - 2 - Аутенфикация
    //Фаза - 1
    //Клиентская часть
    private static void clientSendIandA(){
        //Тут происходит отправка I
        clientSmallA = simpleGenerate.getRandomBigInteger(300, false);
        clientA = g.modPow(clientSmallA, N);
    }

    private static void clientBuildKey(BigInteger serverB) throws NoSuchAlgorithmException {
        clientU = new BigInteger(hash(clientA.toString(), serverB.toString()));
        if(clientU.compareTo(BigInteger.ZERO) == 0){
            System.exit(-1);
        }
        clientX = new BigInteger(hash(serverSalt.toString(), clientPassword.toString()));
        clientS = serverB.subtract(k.multiply(g.modPow(clientX, N))).modPow(clientSmallA.add(clientU.multiply(clientX)), N);
        clientK = new BigInteger(hash(clientS.toString()));
    }

    //Этап - 2 - Аутенфикация
    //Фаза - 1
    //Серверная часть
    private static void serverHandlerIandA(String I, BigInteger A, BigInteger v) throws NoSuchAlgorithmException {
        if(A.compareTo(BigInteger.ZERO) == 0){
            System.exit(-1);
        }
        serverA = A;
        BigInteger b = simpleGenerate.getRandomBigInteger(300, false);
        serverB = k.multiply(v).add(g.modPow(b, N)).mod(N);
        //I(username) уже в памяти
        //Происходит отправка s и B
        clientB = serverB;
        serverU = new BigInteger(hash(A.toString(), serverB.toString()));
        if(serverU.compareTo(BigInteger.ZERO) == 0){
            System.exit(-1);
        }
        serverS = A.multiply(v.modPow(serverU, N)).modPow(b, N);
        serverK = new BigInteger(hash(serverS.toString()));
    }

    //Фаза - 2
    //Клиентская часть
    private static void getClientMandR() throws NoSuchAlgorithmException {
        BigInteger M = new BigInteger(hash(
            new String(xor(hash(N.toString()), hash(g.toString()))),
            new String(hash(serverI)),
            clientS.toString() , clientA.toString(), clientB.toString(), k.toString()
        ));
        BigInteger R = new BigInteger(hash(clientA.toString(), M.toString(), clientK.toString()));
        System.out.println("clientR:\t" + R);
    }

    //Фаза - 2
    //Серверная часть
    private static void getServerMandR() throws NoSuchAlgorithmException {
        BigInteger M = new BigInteger(hash(
            new String(xor(hash(N.toString()), hash(g.toString()))),
            new String(hash(serverI)),
            serverS.toString() , serverA.toString(), serverB.toString(), k.toString()
        ));
        BigInteger R = new BigInteger(hash(serverA.toString(), M.toString(), serverK.toString()));
        System.out.println("serverR:\t" + R);
    }


    //Инициализация факторов протокола
    private static void protocolFactorsInit(int nOrder) {
        //Получение q
        BigInteger checkBigInt = simpleGenerate.getRandomSimpleBigInteger(nOrder, 2000);
        while(true){
            if(simpleGenerate.checkBigIntegerModSimple(checkBigInt.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE), 2000) && 
            simpleGenerate.checkSimpleMillerRabin(checkBigInt.multiply(BigInteger.valueOf(2)).add(BigInteger.ONE), (int)(Math.log10(nOrder)/Math.log(2)))){
                break;
            }
            checkBigInt = simpleGenerate.getRandomSimpleBigInteger(nOrder, 2000);
        }
        //Получение N
        N = checkBigInt.multiply(BigInteger.TWO).add(BigInteger.ONE);
        //Получение g
        BigInteger phi = N.subtract(BigInteger.ONE);
        List<BigInteger> pFactor = getFact(phi);
        for(BigInteger i = BigInteger.TWO; i.compareTo(N) != 1; i = i.add(BigInteger.ONE)){
            Boolean genOk = true;
            for(int j = 0; j < pFactor.size(); j++){
                if(i.modPow(phi.divide(pFactor.get(j)),N).compareTo(BigInteger.ONE) == 0){
                    genOk = false;
                    break;
                }
            }
            if(genOk){
                g = i;
                break;
            }
        }
        //Получение k
        k = BigInteger.valueOf(3);
    }

    //Максимум на core i5 2-ого поколения 20 порядок
    private static List<BigInteger> getFact(BigInteger n){
        List<BigInteger> pFactor = new ArrayList<BigInteger>();
        for(BigInteger i = BigInteger.valueOf(2); i.multiply(i).compareTo(n) != 1; i = i.add(BigInteger.ONE)){
            if(n.mod(i).compareTo(BigInteger.ZERO) == 0){
                pFactor.add(i);
                while(n.mod(i).compareTo(BigInteger.ZERO) == 0){
                    n = n.divide(i);
                }
            }
        }
        if(n.compareTo(BigInteger.ZERO) == 1){
            pFactor.add(n);
        }
        return pFactor;
    }

    private static String getSalt(int order) {
        return simpleGenerate.getRandomBigInteger(order, false).toString();
    }

    private static byte[] hash(String... args) throws NoSuchAlgorithmException {
        String source = "";
        for (String string : args) {
            source = source + string;
        }
        return MessageDigest.getInstance("SHA-512").digest((source).getBytes());
    }

    private static byte[] xor(byte[] a, byte[] b) {
        byte[] result = new byte[Math.min(a.length, b.length)];
      
        for (int i = 0; i < result.length; i++) {
          result[i] = (byte) (((int) a[i]) ^ ((int) b[i]));
        }
      
        return result;
      }
}
