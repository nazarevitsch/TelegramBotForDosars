package main;

import java.util.ArrayList;
import java.util.List;

public class StatisticCrater {

    private static StatisticCrater statisticCrater = null;
    private List<StatisticUnit> units;
    private boolean flag;
    private boolean needWork;

    private StatisticCrater(){
        units = UnitWreiter.read();
    }

    public synchronized void start() {
        if (needWork) {
            flag = true;
            units = new ArrayList<>();
            List<String> list = URLWork.searchLinkInStadiuDosar();
            for (int l = list.size() - 4; l >= list.size() - 4; l--) {
                System.out.println(list.get(l));
                URLWork.downloadLink(list.get(l));
                System.out.println("a");
                List<String> numbers = PDFWorker.PDFReaderStadiuDosarFull();
                int indexFirst = numbers.get(0).indexOf(".");
                int year = Integer.valueOf(numbers.get(0).substring(indexFirst + 4, indexFirst + 8));

                if (checkYear(year) == -1) {
                    StatisticUnit statisticUnit = new StatisticUnit(year);
                    go:
                    for (int i = numbers.size() - 1; i >= 0; i--) {
                        try {
                            int index = numbers.get(i).indexOf(".");
                            int month = Integer.valueOf(numbers.get(i).substring(index + 1, index + 3));
                            index = numbers.get(i).indexOf("/RD");
                            String dosar = numbers.get(i).substring(0, index);

                            if (WorkWithDataBase.getWorker().contain(year + "", dosar)) {
                                statisticUnit.getMonthYes()[month - 1] = statisticUnit.getMonthYes()[month - 1] + 1;
                                numbers.remove(i);
                                continue go;
                            }

//                            List<String> dosars = WorkWithDataBase.getWorker().read(new File("Ordine\\" + year + "\\" + (Integer.valueOf(dosar) / 1000) + ".txt"));
//                            for (int j = 0; j < dosars.size(); j++) {
//                                if (dosars.get(j).split("   ")[0].equals(dosar)) {
//                                    statisticUnit.getMonthYes()[month - 1] = statisticUnit.getMonthYes()[month - 1] + 1;
//                                    numbers.remove(i);
//                                    continue go;
//                                }
//                            }

                            statisticUnit.getMonthNo()[month - 1] = statisticUnit.getMonthNo()[month - 1] + 1;
                            numbers.remove(i);
                        } catch (Exception e) {
                            System.out.println("HUINA");
                        }
                    }
                    units.add(statisticUnit);
                }
            }
            flag = needWork = false;
            UnitWreiter.writeList(units);
            notifyAll();
        }
    }

    private int checkYear(int year){
        for (int i = 0; i < units.size(); i++){
            if (units.get(i).getYear() == year) return i;
        }
        return -1;
    }

    public synchronized List<StatisticUnit> getUnits() {
        try {
            if (flag) wait();
        } catch (Exception e){}
        return units;
    }

    public synchronized static StatisticCrater crater(){
        if (statisticCrater == null) {
            statisticCrater =new StatisticCrater();
        }
        return statisticCrater;
    }

    public synchronized void setNeedWork(boolean needWork) {
        this.needWork = needWork;
    }
}
