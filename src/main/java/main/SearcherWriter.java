package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SearcherWriter {

    public static synchronized boolean write(String dosar, long chatID){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Searchers.txt"), true))) {
            writer.write( dosar + " " + chatID + "\n");
            return true;
        } catch (Exception e){return false;}
    }

    public static synchronized void write(List<String> dosars){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(new File("Searchers.txt")))) {
            for (int i = 0; i < dosars.size(); i++){
                writer.write(dosars.get(i) + "\n");
            }
        } catch (Exception e){}
    }

    public static synchronized List<String> read(){
        List<String> list = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("Searchers.txt")))) {
            String line = reader.readLine();
            while (line != null){
                list.add(line);
                line = reader.readLine();
            }
        } catch (Exception e){}
        return list;
    }
}
