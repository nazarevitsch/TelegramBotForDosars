package main;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PDFWorker {

    public static List<String> PDFReaderOrdine() {
        List<String> list = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File("CurrentOrdine.pdf"))) {
            if (!document.isEncrypted()) {
                String[] lines = new PDFTextStripper().getText(document).split("\\r?\\n");

                for (String line : lines) {
                    int k1 = line.indexOf("(");
                    int k2 = line.indexOf(")");
                    int a = line.indexOf("/");
                    if(k1 < a && a < k2 && k1 != -1 && k2 != -1 && a != -1)  list.add(line.substring(k1, (k2 + 1)).replaceAll(" ", ""));
                }
            }
        }catch (Exception e) {
            ERORWriter.writeException("---EROR PDF READER:\nDATE:  " + (new Date()).toString());
        }
        return list;
    }

    public static List<String> PDFReaderStadiuDosar() {
        List<String> list = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File("CurrentOrdine.pdf"))) {
            if (!document.isEncrypted()) {
                String[] lines = new PDFTextStripper().getText(document).split("\\r?\\n");
                for (String line : lines) {
                    if(line.contains("/RD/") && line.contains("P")) {
                        int index = line.indexOf("/RD/");
                        String str1 = line.substring(0, index);
                        String str2 = line.substring(index + 4, index + 8);
                        list.add(str1 + "/" + str2);
                    }
                }
            }
        }catch (Exception e) {}
        return list;
    }

    public static List<String> PDFReaderStadiuDosarFull() {
        List<String> list = new ArrayList<>();
        try (PDDocument document = PDDocument.load(new File("CurrentOrdine.pdf"))) {
            if (!document.isEncrypted()) {
                String[] lines = new PDFTextStripper().getText(document).split("\\r?\\n");
                for (String line : lines) {
                    if(line.contains("/RD/")) {
                        list.add(line);
                    }
                }
            }
        }catch (Exception e) {}
        return list;
    }
}
