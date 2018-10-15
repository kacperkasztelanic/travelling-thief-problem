package ttp.utils;

import java.util.Locale;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MatrixUtils {

    private static final String DELIMETER = "  ";

    public static String toReadableString(double[][] matrix, int decimalPlaces) {
        double f = Double.MIN_VALUE;
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                double absoluteValue = Math.abs(matrix[row][col]);
                if (absoluteValue > f) {
                    f = absoluteValue;
                }
            }
        }
        int length = MatrixUtils.numLength(f);
        StringBuilder sb = new StringBuilder();
        String format = "%" + (length + decimalPlaces + 1) + "." + decimalPlaces + "f";
        for (int row = 0; row < matrix.length; row++) {
            for (int col = 0; col < matrix[row].length; col++) {
                sb.append(String.format(Locale.US, format, matrix[row][col]));
                sb.append(DELIMETER);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static int numLength(double f) {
        int n = (int) f;
        if (n == 0) {
            return 1;
        }
        int length;
        n = Math.abs(n);
        for (length = 0; n > 0; length++) {
            n /= 10;
        }
        return length;
    }
}
