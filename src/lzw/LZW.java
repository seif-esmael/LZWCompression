/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package lzw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seifeldeen Mohamed Ahmed(20210168) & Ziyad Ayman Farouk (20210147)
 */
public class LZW {

    /**
     * @param filePath
     * @return 
     */        
    public static List<Integer> compress(String filePath) {  
        try {
            String input = readFile(filePath);
            Map<String, Integer> dictionary = new HashMap<>();
            List<Integer> compressed = new ArrayList<>();
            int startNumber = 128;
            for (int i = 0; i < 256; i++) {
                dictionary.put(String.valueOf((char) i), i);
            }
            String current = "";
            for (char c : input.toCharArray()) {
                current += c;
                if (!dictionary.containsKey(current)) {
                    compressed.add(dictionary.get(current.substring(0, current.length() - 1)));
                    dictionary.put(current, startNumber++);
                    current = String.valueOf(c);
                }
            }
            compressed.add(dictionary.get(current));
            return compressed;
        } catch (IOException ex) {
            Logger.getLogger(LZW.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }        
    }
    //__________________________________________________________________________________________________________________
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        }
        return content.toString();
    }
    //__________________________________________________________________________________________________________________
    public static void saveCompressedToFile(List<Integer> compressed, String outputPath) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                for (Integer code : compressed) {
                    writer.write(code.toString());
                    writer.write(" ");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //__________________________________________________________________________________________________________________
    public static void main(String[] args) {          
        //String text = "ABAABABBAABAABAAAABABBBBBBBB";
        String Ifile = "C:\\Users\\Seif\\Desktop\\Java\\LZW\\input.txt";
        String Ofile = "compressed.txt";
        List<Integer> compressed = compress(Ifile);
        saveCompressedToFile(compressed, Ofile);
        System.out.println("Compressed data saved to " + Ofile + " file");
    }                       
}
