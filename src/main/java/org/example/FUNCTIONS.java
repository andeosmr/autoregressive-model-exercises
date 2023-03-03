package org.example;

public class FUNCTIONS {
    public static Boolean checkbetween(double value, double around, double range) {
        return (value > around + range || value < around - range);
    }
}
