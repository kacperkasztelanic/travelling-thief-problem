package ttp.utils;

import java.util.Locale;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MatrixUtils {

    private static final String DELIMITER = "  ";

    public static String toReadableString(double[][] matrix, int decimalPlaces) {
        int length = MatrixUtils.numLength(getMaxValue(matrix));
        StringBuilder sb = new StringBuilder();
        String format = "%" + (length + decimalPlaces + 1) + "." + decimalPlaces + "f";
        for (double[] row : matrix) {
            for (double column : row) {
                sb.append(String.format(Locale.US, format, column));
                sb.append(DELIMITER);
            }
            sb.append(System.lineSeparator());
        }
        return sb.toString();
    }

    private static double getMaxValue(double[][] matrix) {
        double f = Double.MIN_VALUE;
        for (double[] row : matrix) {
            for (double column : row) {
                double absoluteValue = Math.abs(column);
                if (absoluteValue > f) {
                    f = absoluteValue;
                }
            }
        }
        return f;
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
