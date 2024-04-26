package com.example.plecakowy;

import java.math.BigInteger;
import java.util.Random;

public class Operations {
    private Converter converter = new Converter();

    // ZNAJDUJE LICZBE WZGLEDNIE PIERWSZA DO LICZBY M
    public int findRelativelyPrime(int m) {
        Random random = new Random();
        int n = random.nextInt(m);
        while (!BigInteger.valueOf(m).gcd(BigInteger.valueOf(n)).equals(BigInteger.ONE)) {
            n = random.nextInt(m);
        }
        return n;
    }

    public int getM(int[] privateKey) {
        int m = 0;
        Random random = new Random();
        for (int mass : privateKey) {
            m += mass;
        }
        m += random.nextInt(10);
        return m;
    }

    // KLUCZ PRYWATNY JEST CIAGIEM SUPERROSNACYM I OD NIEGO ZACZYNA SIE CALY ALGORYTM (NIM SIE DESZYFRUJE)
    public int[] generatePrivateKey(int len) {
        int[] result = new int[len];
        int sum = 0;
        Random random = new Random();
        int firstElement = random.nextInt(1,10);
        result[0] = firstElement;
        sum += firstElement;
        if (len == 1) {
            return result;
        } else {
            for (int i = 1; i < len; i++) {
                result[i] = random.nextInt(sum + 1, (2 * sum) + 1);
                sum += result[i];
            }
        }
        return result;
    }

    // TWORZY KLUCZ PUBLICZNY Z WCZESNIEJ ZNANEGO KLUCZA PRYWATNEGO (NIM SIE SZYFRUJE)
    public int[] createPublicKey(int[] privateKey, int n, int m) {
        int[] result = new int[privateKey.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (privateKey[i] * n) % m;
        }
        return result;
    }

    // OBLICZA MODULO ^ -1
    public int modInverse(int a, int m) {
        int m0 = m;
        int y = 0;
        int x = 1;

        if (m == 1) {
            return 0;
        }

        while (a > 1){
            int q = a / m;

            int t = m;

            m = a % m;
            a = t;
            t = y;

            y = x - q * y;
            x = t;
        }

        if (x < 0){
            x = x + m0;
        }
        return x;
    }

    // DZIELENIE NA BLOKI BITOW, KAZDY O DLUGOSCI KLUCZA PUBLICZNEGO
    public byte[][] divideIntoBlocks(byte[] text, int len) {
        int numberOfBlocks = (text.length + len - 1) / len;
        byte[][] result = new byte[numberOfBlocks][len];
        int counter = 0;

        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < len; j++) {
                int index = i * len + j;
                if (index < text.length) {
                    result[i][j] = text[index];
                } else {
                    counter++;
                    result[i][j] = 0;
                }
            }
        }
        return result;
    }

    public int howManyZerosAdded(byte[] plainMessage, int keyLength) {
        int len = plainMessage.length;
        int counter = 0;
        while (len % keyLength != 0) {
            counter++;
            len++;
        }
        return counter;
    }

    public int[] encipher(byte[] message, int[] publicKey) {
        byte[][] dividedData = this.divideIntoBlocks(message, publicKey.length);
        int[] result = new int[dividedData.length];
        int sum = 0;
        for (int i = 0; i < dividedData.length; i++) {
            for (int j = 0; j < publicKey.length; j++) {
                sum += dividedData[i][j] * publicKey[j];
            }
            result[i] = sum;
            sum = 0;
        }
        return result;
    }

    public byte[] decipher(int n, int m, int[] privateKey, int[] cipher) {
        int nInverse = this.modInverse(n, m);
        byte[] result = new byte[privateKey.length * cipher.length];
        int temp = 0;
        int counter = 0;
        for (int i = 0; i < cipher.length; i++) {
            temp = (cipher[i] * nInverse) % m;
            for (int j = privateKey.length - 1; j >= 0; j--) {
                if (temp < privateKey[j]) {
                    result[i * privateKey.length + j] = 0;
                } else {
                    result[i * privateKey.length + j] = 1;
                    temp -= privateKey[j];
                }
            }
        }
        return result;
    }
}
