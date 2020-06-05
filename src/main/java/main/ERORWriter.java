package main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class ERORWriter {

    public synchronized static void writeException(String exception){
        File file = new File("EXCEPTIONS.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))){
            writer.write(exception + "\n\n\n");
        } catch (Exception e){}
    }
}
