package ttp.utils;

import java.util.Arrays;
import java.util.Random;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtils {

    private static final Random RANDOM = new Random();

    public static void shuffle(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = RANDOM.nextInt(i + 1);
            if (index != i) {
                int temp = array[index];
                array[index] = array[i];
                array[i] = temp;
            }
        }
    }

    public static int[] shuffledCopy(int[] array) {
        int[] result = Arrays.copyOf(array, array.length);
        shuffle(result);
        return result;
    }

    public static void swap(int i, int j, int[] array) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
