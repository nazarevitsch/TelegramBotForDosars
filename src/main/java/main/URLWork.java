package main;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class URLWork {

    public static final String ordineLink = "http://cetatenie.just.ro/index.php/ro/ordine/articol-11";
    public static final String stadiuDosarLink = "http://cetatenie.just.ro/index.php/ro/centru-de-presa-2/dosar-articol-11";
    public static final String mainLink = "http://cetatenie.just.ro";
    public static final File fileOrdine = new File("Ordine.html");
    public static final File fileStadiuDosar = new File("StadiuDosar.html");

    public static void downloadHTML(File file, String link) throws Exception {
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        URLConnection connection = new URL(link).openConnection();
        Scanner scanner = new Scanner(connection.getInputStream());
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            writer.write(line + "\n");
        }
        writer.close();
    }

    public static void downloadAllMainHTML(){
        try {
            downloadHTML(fileOrdine, ordineLink);
            downloadHTML(fileStadiuDosar, stadiuDosarLink);
        } catch (Exception e){
            ERORWriter.writeException("---EROR DOWNLOAD ALL HTML:\nDATE:  " + (new Date()).toString());
        }
    }

    public static List<String> serchAllLinkOfOrdine(){
        List<String> list = new ArrayList<>();
        List<String> dates = new ArrayList<>();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileOrdine))) {
            String line = bufferedReader.readLine();
            int indicator = 0;
            while (line != null){
                for(int i = 2011; i <= 2020; i++) {
                    if (i == 2020){
                        indicator = line.indexOf("<strong>" + i + "</strong>");
                    }
                    indicator = line.indexOf("<strong>" + i + "&nbsp;</strong>");
                    if(indicator != -1){
                        while (true){
                            if(line.indexOf("</ul>") != -1) break;
                            if(line.indexOf("<li") != -1){
                                if(checkLastDateOrdine(line, dates)) {
                                    changeDateOrdine(dates.get(0));
                                    return list;
                                }
                                list.addAll(searchLinkOfOrdineInString(line));
                            }
                            line = bufferedReader.readLine();
                        }
                    }
                }
                line = bufferedReader.readLine();
            }
        } catch (Exception e){
            ERORWriter.writeException("---EROR SEARCH ALL LINK IN ORDINE:\nDATE:  " + (new Date()).toString() + "\nPATH:  NO FILE");
        }
        return list;
    }

    public static List<String> searchLinkOfOrdineInString(String line) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < line.length(); i++) {
            try {
                if (line.substring(i, (i + 4)).equals("href")) {
                    int l = 1;
                    boolean flag1 = false;
                    int firstIndex = 0, lastIndex = 0;
                    while (true) {
                        if (line.substring((i + 4 + l), (i + 5 + l)).equals("/") && !flag1) {
                            flag1 = true;
                            firstIndex = i + 4 + l;
                        }
                        if (line.substring((i + 4 + l), (i + 7 + l)).equals("pdf")) {
                            lastIndex = i + 7 + l;
                            break;
                        }
                        l++;
                    }
                    list.add(line.substring(firstIndex, lastIndex));
                }
            } catch (Exception e){}
        }
        return list;
    }

    public static boolean checkLastDateOrdine(String line, List<String> dates){
        File file = new File("updatingDateOrdine.txt");
        String date1 = "";
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file))){
            date1 = bufferedReader.readLine();
            if (date1 == null) changeDateOrdine("16.01.2012");
        } catch (Exception e){}
        String str = line.substring(line.indexOf("Data de"));
        int indexStart = str.indexOf(".") - 2;
        String date2 = str.substring(indexStart, indexStart + 10);
        dates.add(date2);
        return date1.equals(date2);
    }

    public static void changeDateOrdine(String date){
        File file = new File("updatingDateOrdine.txt");
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(date);
        } catch (Exception e){}
    }

    public static void downloadLink(String link){
        File file = new File("CurrentOrdine.pdf");
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file))) {
            InputStream inputStream = null;
            try {
               URLConnection connection = new URL(mainLink + link).openConnection();
               inputStream = connection.getInputStream();
           } catch (Exception e){
                ERORWriter.writeException("---EROR DOWNLOAD CONECTION:\nDATE:  " + (new Date()).toString() + "\nLINK:  " + link);
            }
            int inp = inputStream.read();
            while (inp != -1) {
                outputStream.write(inp);
                inp = inputStream.read();
            }
            inputStream.close();
        } catch (Exception e) {
            ERORWriter.writeException("---EROR DOWNLOAD ORDINE:\nDATE:  " + (new Date()).toString() + "\nLINK:  " + link);
        }
    }

    public static List<String> searchLinkInStadiuDosar() {
        List<String> list = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(fileStadiuDosar))) {
                String line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    if (line.contains("descarca documentul in format")) {
                        list.add(line.substring(line.indexOf("images") - 1, line.indexOf("pdf") + 3));
                    }
                }
            } catch (Exception e) {
                ERORWriter.writeException("---EROR SEARCH ALL LINK IN STADIU DOSAR:\nDATE:  " + (new Date()).toString() + "\nPATH:  NO FILE");
            }
        return list;
    }

    public static boolean checkDateStudioDosar(){
        File file = new File("updatingDateStadiuDosar.txt");
        boolean flag = false;
        String date = null;
        try(BufferedReader reader = new BufferedReader(new FileReader(fileStadiuDosar))) {
            String line = reader.readLine();
            String currentDate = null;
            try(BufferedReader reader1 = new BufferedReader(new FileReader(file))) {
                currentDate = reader1.readLine();
            } catch (Exception e){}
            while (line != null) {
                line = reader.readLine();
                if (line.contains("Actualizat la data de")) {
                    int index = line.substring(line.indexOf("Actualizat la data de")).indexOf(".");
                    date = line.substring(line.indexOf("Actualizat la data de")).substring(index - 2, index + 8);
                    if(!date.equals(currentDate)) flag = true;
                    break;
                }
            }
        } catch (Exception e){}
        if (flag) changeDateStudioDosar(date, file);
        return flag;
    }

    public static void changeDateStudioDosar(String date, File file){
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
            writer.write(date);
        } catch (Exception e){}
    }

}
