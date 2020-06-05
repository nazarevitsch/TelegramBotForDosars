package main;

import java.io.Serializable;

public class StatisticUnit implements Serializable {

    private int year;
    private int[] monthYes;
    private int[] monthNo;

    public StatisticUnit(int year) {
        this.year = year;
        monthYes = new int[12];
        monthNo = new int[12];
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int[] getMonthYes() {
        return monthYes;
    }

    public void setMonthYes(int[] monthYes) {
        this.monthYes = monthYes;
    }

    public int[] getMonthNo() {
        return monthNo;
    }

    public void setMonthNo(int[] monthNo) {
        this.monthNo = monthNo;
    }
}
