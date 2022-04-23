package com.company;
public class Action {

    private int row;
    private int column;
    private int value;
    private int previousValue;



    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getPreviousValue() {
        return previousValue;
    }

    public void setPreviousValue(int previousValue) {
        this.previousValue = previousValue;
    }










    public Action(int row, int column, int value,int previousValue) {
        this.row = row;
        this.column = column;
        this.value = value;
        this.previousValue = previousValue;
    }
    }