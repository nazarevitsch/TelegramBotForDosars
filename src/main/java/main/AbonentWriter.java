package main;

import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class AbonentWriter {

    public synchronized static void write(Update update){
        File file = new File("Abonents.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write("-- " + update.getMessage().getChat().getUserName() + " | " +
                    update.getMessage().getChat().getFirstName() + " | " +
                    update.getMessage().getChat().getLastName() + " | " +
                    update.getMessage().getChat().getId() + " | " +
                    update.getMessage().getText() + "\n");
        } catch (Exception e){}
    }
}
