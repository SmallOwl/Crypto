package Лабораторные;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class simpleGenerate {

    //Генератор простого большого числа
    public static BigInteger getRandomSimpleBigInteger(int order, int checkModSimpleMax){
        BigInteger checkInt = getRandomBigInteger(order, true);
        while(true){
            if(checkBigIntegerModSimple(checkInt, checkModSimpleMax) && checkSimpleMillerRabin(checkInt, (int)(Math.log10(order)/Math.log(2)))){
                return checkInt;
            }
            checkInt = getRandomBigInteger(order, true);
        }
    }

    //Тест Миллера-Рабина на составные числа
    public static boolean checkSimpleMillerRabin(BigInteger checkInt, int iterrations){
        int s = 0;
        BigInteger t = checkInt.subtract(BigInteger.valueOf(1));
        while(t.mod(BigInteger.valueOf(2)).equals(BigInteger.valueOf(0))){
            s++;
            t = t.divide(BigInteger.valueOf(2));
        }
        for(int A = 0; A < iterrations; A++){
            BigInteger a = getRandomBigInteger(BigInteger.valueOf(2), checkInt.subtract(BigInteger.valueOf(2)));
            BigInteger x = a.modPow(t, checkInt);
            if(x.equals(BigInteger.valueOf(1)) || x.equals(checkInt.subtract(BigInteger.valueOf(1)))){
                continue;
            }
            for(int B = 0; B < s - 1; B++){
                x = x.modPow(BigInteger.valueOf(2), checkInt);
                if(x.equals(BigInteger.valueOf(1))){
                    return false;
                }else if(x.equals(checkInt.subtract(BigInteger.valueOf(1)))){
                    break;
                }
            }
            if(!x.equals(checkInt.subtract(BigInteger.valueOf(1)))){
                return false;
            }
        }
        return true;
    }

    //Деление большого случайного числа на маленькие простые числа
    public static boolean checkBigIntegerModSimple(BigInteger checkInt, int maxSimple){
        for (Integer smallSimple : getListOfSimple(maxSimple)) {
            if(checkInt.mod(BigInteger.valueOf(smallSimple)).equals(BigInteger.valueOf(0))){
                return false;
            }
        }
        return true;
    }

    //Решето Сундарама
    //Генерация списка простых чисел до maxSimple
    private static List<Integer> getListOfSimple(int maxSimple){
        maxSimple /= 2;
        boolean[] source = new boolean[maxSimple + 1];
        List<Integer> result = new ArrayList<Integer>();
        source[0] = false;
        for(int i = 1; i <= maxSimple; i++){
            source[i] = true;
        }
        int maxI = (int)(Math.sqrt(2*maxSimple + 1) - 1)/2;
        for(int i = 1; i <= maxI; i++){
            int maxJ = (maxSimple - i)/(2*i + 1);
            for(int j = i; j <= maxJ; j++){
                source[i + j + 2*i*j] = false;
            }
        }
        result.add(Integer.valueOf(2));
        for(int i = 1; i <= maxSimple; i++){
            if(source[i] == true){
                result.add(Integer.valueOf(i * 2 + 1));
            }
        }
        return result;
    }

    //Генерация большого случайного числа по порядку 
    public static BigInteger getRandomBigInteger(int order, boolean odd){
        Random r = new Random();
        char[] resultCharArray = new char[order + 1];
        resultCharArray[0] = Character.forDigit(1 + r.nextInt(9), 10);
        for(int i = 1; i <= order; i++){
            resultCharArray[i] = Character.forDigit(r.nextInt(10), 10);
        }
        if(Integer.valueOf(resultCharArray[order]) % 2 == 0){
            resultCharArray[order] = (char)(resultCharArray[order] + 1);
        } 
        return new BigInteger(String.valueOf(resultCharArray));
    }

    //Генерация большого случайного числа в диапазоне
    public static BigInteger getRandomBigInteger(BigInteger min, BigInteger max){
        Random r = new Random();
        char[] resultCharArray = new char[max.toString().length()];
        String resMin = min.toString();
        String resMax = max.toString();
        int length = resMax.length();
        while(resMin.length() < length){
            resMin = "0" + resMin;
        }
        int i = 0;
        while((i < length) && (resMin.charAt(i) == resMax.charAt(i))){
            resultCharArray[i] = resMin.charAt(i);
            i++;
        }
        if(i < length){
            resultCharArray[i] = (char)(resMin.charAt(i) + r.nextInt(resMax.charAt(i) - resMin.charAt(i) + 1));
            i++;
            if((i - 1 < length) && (resultCharArray[i - 1] == resMin.charAt(i - 1))){
                while((i < length) && (resMin.charAt(i - 1) == resultCharArray[i - 1])){
                    resultCharArray[i] = (char)(resMin.charAt(i) + r.nextInt(10 - Character.getNumericValue(resMin.charAt(i))));
                    i++;
                }
            }else if((i - 1 < length) && (resultCharArray[i - 1] == resMax.charAt(i - 1))){
                while((i < length) && (resMax.charAt(i - 1) == resultCharArray[i - 1])){
                    if(Character.getNumericValue(resMax.charAt(i)) == 0){
                        resultCharArray[i] = Character.forDigit(0, 10);
                    }else{
                        resultCharArray[i] = Character.forDigit(r.nextInt(Character.getNumericValue(resMax.charAt(i))), 10);
                    }
                    i++;
                }
            }
            while(i < length){
                resultCharArray[i] = Character.forDigit(r.nextInt(10), 10);
                i++;
            }
        }
        return new BigInteger(String.valueOf(resultCharArray));
    }
}