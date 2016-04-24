package com.example.marplex.schoolbook.utilities;

/**
 * Created by marco on 4/24/16.
 */
public class MathUtils {
    public static double rintRound(double number, int decimalDigits){
        double temp = Math.pow(10, decimalDigits);
        return Math.rint(number * temp) / temp;
    }
}
