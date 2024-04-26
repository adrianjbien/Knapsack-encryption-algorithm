package com.example.plecakowy;

import java.util.Arrays;

public class Converter {
    public String asciiToBinaryString(String text) {
        byte[] bytes = text.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return binary.toString();
    }

    public byte[] convertStringToByte(String text) {
        byte[] byteArray = new byte[text.length()];

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            byte value = (byte) (c == '1' ? 1 : 0);
            byteArray[i] = value;
        }
        return byteArray;
    }

    public String BytesToAscii(String bytes) {
        return Arrays.stream(bytes.split("(?<=\\G.{8})"))/* regex to split the bits array by 8*/
                .parallel()
                .map(eightBits -> (char)Integer.parseInt(eightBits, 2))
                .collect(
                        StringBuilder::new,
                        StringBuilder::append,
                        StringBuilder::append
                ).toString();
    }

    public String convertByteToString(byte[] data) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < data.length; i++) {
            result.append(data[i]);
        }
        return result.toString();
    }

    public int[] convertStringToIntArray(String text) {
        String[] items = text.replaceAll("\\[", "").replaceAll("]", "").replaceAll("\\s", "").split(",");

        int[] results = new int[items.length];

        for (int i = 0; i < items.length; i++) {
            try {
                results[i] = Integer.parseInt(items[i]);
            } catch (NumberFormatException nfe) {
                //NOTE: write something here if you need to recover from formatting errors
            };
        }
        return results;
    }

    public byte[] convertFileToBytes(byte[] fileData) {
        StringBuilder summary = new StringBuilder();
        for (byte b : fileData) {
            summary.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        return this.convertStringToByte(summary.toString());
    }

    public byte[] convertBytesToFile(byte[] data) {
        byte[] result = new byte[data.length / 8];
        StringBuilder temp = new StringBuilder();
        int counter = 0;
        for (byte b : data) {
            temp.append(b);
            if (temp.length() == 8) {
                result[counter++] = (byte) Integer.parseInt(temp.toString(), 2);
                temp.setLength(0);
            }
        }
        return result;
    }

    public int[] convertByteToInt(byte[] data) {
        int[] result = new int[data.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = data[i];
        }
        return result;
    }
}
