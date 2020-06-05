package main;

import java.io.*;
import java.util.Date;

public  class  WorkWithDataBase {

    private static WorkWithDataBase worker;

    private WorkWithDataBase(){}

    public synchronized void write(File file, String line)throws Exception {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(line + "\n");
        } catch (Exception e) {
            ERORWriter.writeException("---EROR WRITE IN DATA BASE:\nDATE:  " + (new Date()).toString() + "\nPATH:  " + file.getCanonicalPath() + "\nDOSAR NUMBER:  " + line);
        }
    }

//    public synchronized List<String> read(File file){
//        List<String> list = new ArrayList<>();
//        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            String line = reader.readLine();
//            while (line != null) {
//                list.add(line);
//                line = reader.readLine();
//            }
//        } catch (Exception e) {
//            ERORWriter.writeException("---EROR READ FROM DATA BASE:\nDATE:  " + (new Date()).toString() + "\nPATH:  NO FILE");
//        }
//        return list;
//    }

    public synchronized boolean contain(String year, String number){
        int thousand = Integer.valueOf(number) / 1000;
        boolean flag = false;
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("Ordine\\" + year + "\\" + thousand + ".txt")))) {
            String line = reader.readLine();
            while (line != null){
                if (line.split("   ")[0].equals(number)){
                    flag = true;
                    break;
                }
                line = reader.readLine();
            }
        } catch (Exception e){
            return false;
        }
        return flag;
    }

    public synchronized String containReturnLink(String year, String number){
        int thousand = Integer.valueOf(number) / 1000;
        String link = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(new File("Ordine\\" + year + "\\" + thousand + ".txt")))) {
            String line = reader.readLine();
            while (line != null){
                if (line.split("   ")[0].equals(number)){
                    link = line.split("   ")[1];
                    break;
                }
                line = reader.readLine();
            }
        } catch (Exception e){
            return "";
        }
        return link;
    }

    public synchronized static WorkWithDataBase getWorker(){
        if(worker == null) worker = new WorkWithDataBase();
        return worker;
    }
}
