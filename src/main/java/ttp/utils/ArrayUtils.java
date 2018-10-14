package ttp.utils;

import java.util.Arrays;
import java.util.Random;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ArrayUtils {

    public static void shuffle(int[] array) {
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
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
}
