package SegmentedLeastSquares;

import java.io.*;

public class TestSegmentedLeastSquares {
    public static void main(String[] args) throws FileNotFoundException  {
        SegmentedLeastSquares SLS = new SegmentedLeastSquares();

        File file = new File("data08.txt");

        SLS.insert(file);
    }
}
