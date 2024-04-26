package com.example.plecakowy;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DaoFile {
    public byte[] readFile(String filePath) throws Exception
    {
        FileInputStream fis = new FileInputStream(filePath);
        int counter = fis.available();
        byte[] data = new byte[counter];
        fis.read(data);
        fis.close();
        return data;
    }

    public void writeFile(byte[] data, String filePath) throws Exception
    {
        FileOutputStream fos = new FileOutputStream(filePath);
        fos.write(data);
        fos.close();
    }

    public void writeDataFile(int[] data, String filePath) throws Exception {
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(filePath));
        for (int i = 0; i < data.length; i++) {
            dos.writeInt(data[i]);
        }
        dos.close();
    }

    public int[] readDataFile(String filePath) throws Exception {
        DataInputStream dis = new DataInputStream(new FileInputStream(filePath));
        ArrayList<Integer> list = new ArrayList<Integer>();
        while (dis.available() > 0) {
            list.add(dis.readInt());
        }
        dis.close();
        int[] data = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }
}
