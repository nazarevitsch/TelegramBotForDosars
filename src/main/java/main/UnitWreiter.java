package main;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UnitWreiter {

    private static File file = new File("Units.obj");

    public static List<StatisticUnit> read() {
        List<StatisticUnit> list = new ArrayList<>();
        try (InputChild inputChild = new InputChild(new BufferedInputStream(new FileInputStream(file)))) {
            while (true) {
                list.add((StatisticUnit) (inputChild.readObject()));
            }
        } catch (Exception e) {
        }
        return list;
    }

    public static void writeList(List<StatisticUnit> units) {
        try (OutputChild outputStream = new OutputChild(new BufferedOutputStream(new FileOutputStream(file)))) {
            for (int i = 0; i < units.size(); i++) {
                outputStream.writeObject(units.get(i));
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static class OutputChild extends ObjectOutputStream {

        public OutputChild(OutputStream out) throws IOException {
            super(out);
        }

        @Override
        public void writeStreamHeader(){
        }
    }

    private static class InputChild extends ObjectInputStream {

        public InputChild(InputStream in) throws IOException {
            super(in);
        }

        @Override
        public void readStreamHeader(){}
    }
}
