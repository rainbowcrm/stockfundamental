package com.primus.common.datastructures;

public class DataPair<T> {
    T value1;
    T value2;

    public T getValue1() {
        return value1;
    }

    public void setValue1(T value1) {
        this.value1 = value1;
    }

    public T getValue2() {
        return value2;
    }

    public void setValue2(T value2) {
        this.value2 = value2;
    }

    public DataPair(T value1, T value2) {
        this.value1 = value1;
        this.value2 = value2;
    }

    public DataPair() {
    }

    public boolean inRange(T value) {
        if (value instanceof Double || value instanceof Float || value instanceof Integer) {
            if (value instanceof Double) {
                if ((Double)value >= (Double)value1  && (Double)value <= (Double)value2)
                    return true;
            }

        }
        return false;
    }


}
