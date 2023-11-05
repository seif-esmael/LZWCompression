
package lzw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Seifeldeen Mohamed Ahmed(20210168) & Ziyad Ayman Farouk (20210147)
 */
public class LZW {

    /**
     * @param filePath  the path of the file to be compressed
     */        
    public static void compress(String filePath) {
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
            saveCompressedToFile(compressed, "compressed.txt");
        } catch (IOException ex) {
            Logger.getLogger(LZW.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
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
    public static void saveDecompressedToFile(String decompressed, String outputPath) {
        try {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
                writer.write(decompressed);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void decompress(String filePath) {
        try {
            String inputToBeCompressed=readFile(filePath);
            String[] input=inputToBeCompressed.split(" ");
            int[] inputInt=new int[input.length];
            for (int i = 0; i < input.length; i++) {
                inputInt[i]=Integer.parseInt(input[i]);
            }
            Map<Integer, String> dictionary = new HashMap<>();
            StringBuilder output= new StringBuilder();
            int startNumber = 128;
            String lastOperationSymbols="";
            for (int i = 0; i < input.length; i++) {
                if(dictionary.containsKey(inputInt[i])){
                    output.append(dictionary.get(inputInt[i]));
                    dictionary.put( startNumber++,lastOperationSymbols+dictionary.get(inputInt[i]).charAt(0));
                    lastOperationSymbols=dictionary.get(inputInt[i]);

                }
                else{
                    if(i==0){
                        output.append((char) inputInt[i]);
                        lastOperationSymbols+=(char)inputInt[i];
                    }
                    else{
                        if(inputInt[i]>=128){
                            output.append(lastOperationSymbols).append(lastOperationSymbols.charAt(0));
                            dictionary.put(startNumber++,lastOperationSymbols+lastOperationSymbols.charAt(0));
                            lastOperationSymbols+=lastOperationSymbols.charAt(0);
                        }
                        else{
                            output.append((char)inputInt[i]);
                            dictionary.put(startNumber++,lastOperationSymbols+(char)inputInt[i]);
                            lastOperationSymbols=""+(char)inputInt[i];
                        }
                    }
                }
            }
            saveDecompressedToFile(output.toString(),"decompressed.txt");
        } catch (IOException ex) {
            Logger.getLogger(LZW.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public static void main(String[] args) {
        compress("input.txt");
        decompress("compressed.txt");

    }
}
