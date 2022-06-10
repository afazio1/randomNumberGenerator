import java.io.IOException;
import java.nio.file.*;
import java.util.BitSet;
import java.util.Arrays;

public class RandomNumber {
    private String filename;
    private Path path;
    private byte[] data;
    private byte[] formattedData;
    private BitSet bitset;
    private static int offset;

    public RandomNumber(String filename) throws IOException {
        offset = 0;
        this.filename = filename;
        path = Paths.get("/Users/alexafazio/IdeaProjects/RandomNumberGenerator/audio/" + filename);
        data = Files.readAllBytes(path);
        bitset = getFormattedData(data);
        printRandomNumbers(1000000, 0, 10);

    }
    // turns a byte array (8 bits per byte) into a bit array (0 and 1)
    private BitSet getFormattedData(byte[] data) {
        formattedData = new byte[data.length - 44];
        int j = 0;
        for (int i = 0; i < data.length; i++) {
            if (i > 43) {
                formattedData[j] = data[i];
                j++;
            }
        }
        return BitSet.valueOf(formattedData);
    }

    // Uses array of bits to get a random number integer (min, max]

    private int getRandomNumber(int min, int max, BitSet randBits) {
        double range = max - min;
        int numBitsToTake = (int)Math.floor((Math.log(range) / Math.log(2)) + 1);

        int value;
        int j = offset;

        do {
            String bitString = "";

            for (int i = 0; i < numBitsToTake; i++) {
                if (j < randBits.length()) {
                    bitString += randBits.get(j) ? "1" : "0";
                    j++;
                    offset++;
                }
            }
            value = Integer.parseInt(bitString, 2);

        }
        while (value > range);

        return (value + min);
    }
    // executes the getRandomNumber method x times and prints the total counts of each number
    private void printRandomNumbers(int iterations, int min, int max) {
        int[] counts = new int[max - min + 1];
        for (int i = 0; i < iterations; i++) {
            int randomNum = getRandomNumber(min, max, bitset);
            counts[randomNum] += 1;
        }
        System.out.println(Arrays.toString(counts));
    }
}
