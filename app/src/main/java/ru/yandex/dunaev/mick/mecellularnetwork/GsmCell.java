package ru.yandex.dunaev.mick.mecellularnetwork;

public class GsmCell {
    private int countryCode;
    private int operatorId;
    private int cellId;
    private int lac;

    public GsmCell(int countryCode, int operatorId, int cellId, int lac) {
        this.countryCode = countryCode;
        this.operatorId = operatorId;
        this.cellId = cellId;
        this.lac = lac;
    }
}
