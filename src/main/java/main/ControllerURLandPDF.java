package main;

import java.io.File;
import java.util.Date;
import java.util.List;

public class ControllerURLandPDF {

    public void startWork(){
        URLWork.downloadAllMainHTML();
        updateOrdine();
        updateStadiuDosar();
    }

    public void updateOrdine(){
        List<String> linksOrdine = URLWork.serchAllLinkOfOrdine();
        for (int i = 0; i < linksOrdine.size(); i++) {
            StatisticCrater.crater().setNeedWork(true);
            workOrdine(linksOrdine.get(i));
            System.out.println("OR: " + i);
            System.out.println(linksOrdine.get(i));
        }
    }

    public void updateStadiuDosar() {
        List<String> linksSD = URLWork.searchLinkInStadiuDosar();
        if (URLWork.checkDateStudioDosar()) {
            for (int i = linksSD.size() - 1; i >= 0; i--) {
                StatisticCrater.crater().setNeedWork(true);
                workStadiuDosar(linksSD.get(i));
                System.out.println("SD: " + i);
            }
        }
    }

    public void workOrdine(String currentLink) {
        URLWork.downloadLink(currentLink);
        List<String> numbers = PDFWorker.PDFReaderOrdine();
        for (int l = 0; l < numbers.size(); l++) {
            try {
                int index = numbers.get(l).indexOf("/");
                String directoryYear = numbers.get(l).substring(index + 1, index + 5);                if (!WorkWithDataBase.getWorker().contain(directoryYear, numbers.get(l).substring(1, index))){
                    File file = new File("Ordine\\" + directoryYear + "\\" + Integer.valueOf(numbers.get(l).substring(1, index)) / 1000 + ".txt");
                    WorkWithDataBase.getWorker().write(file, numbers.get(l).substring(1, index) + "   " + URLWork.mainLink + currentLink);
                }

//                File file = new File("Ordine\\" + directoryYear + "\\" + fileThousand + ".txt");
//                if (!containNumber(file, numbers.get(l).substring(1, index))) {
//                    WorkWithDataBase.getWorker().write(file, numbers.get(l).substring(1, index) + "   " + URLWork.mainLink + currentLink);
//                }

            } catch (Exception e) {
                ERORWriter.writeException("---EROR URL AND PDF CONTROLLER(WORK ORDINE):\nDATE:  " + (new Date()).toString() + "\nPATH:  "
                        + "\nLINK OF ORDINE:  " + currentLink + "\nNUMBER OF DOSAR:  " + numbers.get(l));
            }
        }
    }

    public void workStadiuDosar(String currentLink) {
        URLWork.downloadLink(currentLink);
        List<String> numbers = PDFWorker.PDFReaderStadiuDosar();
        for (int l = 0; l < numbers.size(); l++) {
            try {
                int index = numbers.get(l).indexOf("/");
                String directoryYear = numbers.get(l).substring(index + 1, index + 5);
                if (!WorkWithDataBase.getWorker().contain(directoryYear, numbers.get(l).substring(0, index))){
                    File file = new File("Ordine\\" + directoryYear + "\\" + Integer.valueOf(numbers.get(l).substring(0, index)) / 1000 + ".txt");
                    WorkWithDataBase.getWorker().write(file, numbers.get(l).substring(0, index) + "   " + URLWork.mainLink + currentLink);
                }

//                File file = new File("Ordine\\" + directoryYear + "\\" + fileThousand + ".txt");
//                if (!containNumber(file, numbers.get(l).substring(0, index))) {
//                    WorkWithDataBase.getWorker().write(file, numbers.get(l).substring(0, index) + "   " + URLWork.mainLink + currentLink);
//                }

            } catch (Exception e) {
                ERORWriter.writeException("---EROR URL AND PDF CONTROLLER(WORK STADIU DOSAR):\nDATE:  " + (new Date()).toString() + "\nPATH:  "
                        + "\nLINK OF ORDINE:  " + currentLink + "\nNUMBER OF DOSAR:  " + numbers.get(l));
            }
        }
    }

//    public boolean containNumber(File file, String number){
//        List<String> list = new ArrayList<>();
//        try {
//            list = WorkWithDataBase.getWorker().read(file);
//        } catch (Exception e){
//            ERORWriter.writeException("---EROR READ FROM DATA BASE FOR CONTAIN:\nDATE:  " + (new Date()).toString() + "\nPATH:  NO FILE\nLINE:  " + number);
//        }
//        for(int i = 0; i < list.size(); i++){
//            if (list.get(i).split("   ")[0].equals(number))return true;
//        }
//        return false;
//    }

}
