package ru.itmo.refactoring_lab2.logical.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

@Component
public class HashMapLoaderSaver {

    @Value("${collection.file.path}")
    private String savePath;

    public void writeHashMap(HashMap<String, IOElement> hashMap) throws IOException {
        FileOutputStream fileOutputStream
                = new FileOutputStream(savePath);
        ObjectOutputStream objectOutputStream
                = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(hashMap);
        objectOutputStream.flush();
        objectOutputStream.close();
    }

    public HashMap<String, IOElement> readHashMap() throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream
                = new FileInputStream(savePath);
        ObjectInputStream objectInputStream
                = new ObjectInputStream(fileInputStream);
        HashMap<String, IOElement> hashMap = (HashMap<String, IOElement>) objectInputStream.readObject();
        objectInputStream.close();
        return hashMap;
    }

    public void createNewFile(){
        try {
            Path newFilePath = Paths.get(savePath);
            Files.createFile(newFilePath);
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}
