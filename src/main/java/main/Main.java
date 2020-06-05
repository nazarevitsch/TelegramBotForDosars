package main;

public class Main {

    public static void main(String[] args) {
        BotController.start();
        (new ControllerBackEnd()).start();
        System.out.println("MAIN THREAD FINISH");
    }
}
