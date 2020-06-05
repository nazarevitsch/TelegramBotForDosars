package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class LastUpdatingDate {

    public static String lastUpdating() {
        String dateO = null;
        String dateSD = null;
        String[] dateOArr = null;
        String[] dateSDArr = null;
        try (BufferedReader reader1 = new BufferedReader(new FileReader(new File("updatingDateOrdine.txt")))) {
            dateO = reader1.readLine();
            dateOArr = dateO.split("\\.");
        } catch (Exception e) { }
        try (BufferedReader reader2 = new BufferedReader(new FileReader(new File("updatingDateStadiuDosar.txt")))) {
            dateSD = reader2.readLine();
            dateSDArr = dateSD.split("\\.");
        } catch (Exception e) {}
        if (dateO == null){
            if (dateSD == null) return null;
            else return dateSD;
        }
        for (int i = 2; i >= 0; i--){
            if (dateOArr[i].compareTo(dateSDArr[i]) == -1) return dateSD;
            if (dateOArr[i].compareTo(dateSDArr[i]) == 1) return dateO;
        }
        return dateO;
    }
}
