package main;

public class ControllerBackEnd extends Thread {

    public void run(){
        ControllerURLandPDF controllerURLandPDF = new ControllerURLandPDF();
        DosarSearcher dosarSearcher = new DosarSearcher();
        StatisticCrater statisticCrater = StatisticCrater.crater();
        while (true){
            try {
                controllerURLandPDF.startWork();
                dosarSearcher.start();
                statisticCrater.start();
                System.out.println("CONTROLLER SLEEP");
                sleep(18000000);
            }catch (Exception e){
                System.out.println("PIZDEZ");
            }
        }
    }
}
