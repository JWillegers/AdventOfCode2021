package Day0;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PartA {
    private int nOfLines = 10; //TODO CHANGE
    private BufferedReader reader;
    private String[] array = new String[nOfLines];

    public static void main(String[] args) {
        PartA part = new PartA();
        part.setup();
        part.solution();
    }

    public void setup() {
        try {
            reader = new BufferedReader(new FileReader("src/Day/input.txt")); //TODO change filepath
            for (int i = 0; i < nOfLines; i++) {
                array[i] = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void solution() {
        for (int i = 0; i < nOfLines; i++) {

        }

        System.out.println();
    }
}
