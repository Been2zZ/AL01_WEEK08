package SegmentedLeastSquares;

import java.io.*;

public class SegmentedLeastSquares {

    static int n = 0;
    static int cost = 0;
    static Point[] P = null;        // Point
    static double[][] err = null;   // error
    static double[] M = null;       // OPT
    static int[] M_segment = null;  // 나누어진 구간
    static double[][] a = null;
    static double[][] b = null;

    public void insert(File F) throws FileNotFoundException {
        /** File read & Array insert */
        FileReader fr = new FileReader(F);
        BufferedReader br = new BufferedReader(fr);

        String[] temp = null;

        try {
            String line = "";
            while ((line = br.readLine()) != null)
                temp = line.split(",");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println(e);
        }

        int size = temp.length;
        double[] Arr = new double[size];

        for(int i = 0; i < Arr.length; i++)
            Arr[i] = Double.parseDouble(temp[i]);

        /** init */
        n = (int)Arr[0];
        cost = (int)Arr[Arr.length - 1];

        err = new double[n + 1][n + 1];
        M = new double[n + 1];
        M_segment = new int[n + 1];
        a = new double[n + 1][n + 1];
        b = new double[n + 1][n + 1];
        P = new Point[n];

        int j = 1;

        /** Point class 객체 배열에 x, y값 생성 */
        for(int i = 0; i < n; i++)
            P[i] = new Point(Arr[j++], Arr[j++]);

        /** 출력문 */
        print();
    }

    /** n개의 점들을 적합한 직선으로 나타내기 위해 사용하는 method */
    public double Segmented_Least_Squares(int n, Point[] P, double cost) {

        /** init */
        M[0] = M_segment[0] = 0;

        for(int j = 1; j <= n; j++)
            for (int i = 1; i <= j; i++)
                ComputeSSE(P, i, j);        // SSE(i, j) 계산

        for(int j = 1; j <= n; j++) {
            double min = Double.POSITIVE_INFINITY;      // min변수에 최대값 저장
            int k = 0;
            for(int i = 1; i <= j; i++) {
                if(err[i][j] + cost + M[i - 1] < min) { // min값과 err + cost + 이전 opt값 비교
                    min = err[i][j] + cost + M[i - 1];  // 최소값 찾아 저장
                    k = i;                              // index 저장 (구간)
                }
            }
            M[j] = min;             // OPT
            M_segment[j] = k;       // 구간
        }

        return M[n];                // 최종 OPT 값 return
    }

    /** SSE 계산하기 위한 method */
    private void ComputeSSE(Point[] P, int i, int j) {
        int temp = j - i + 1;   // n

        /** init */
        double sum_x = 0, sum_y = 0, sum_xx = 0, sum_xy = 0;

        /** 시그마 연산 */
        for(int k = i; k <= j; k++) {
            sum_x += P[k - 1].x;
            sum_y += P[k - 1].y;
            sum_xx += P[k - 1].x * P[k - 1].x;
            sum_xy += P[k - 1].x * P[k - 1].y;
        }

        a[i][j] = (temp * sum_xy - sum_x * sum_y) / (temp * sum_xx - sum_x * sum_x);
        b[i][j] = (sum_y - a[i][j] * sum_x) / temp;

        /** SSE */
        for (int k = i; k <= j; k++)
            err[i][j] += Math.pow(P[k - 1].y - a[i][j] * P[k - 1].x - b[i][j], 2);
    }

    /** 출력문 */
    public void print() {

        /** 최종 optimal solution */
        double result = Segmented_Least_Squares(n, P, cost);
        System.out.println(String.format("Cost of the optimal solution : %.6f \n", result));

        /** 구간, 직선의 방정식, error값 */
        System.out.println("An optimal solution : ");
        for(int j = n, i = M_segment[n]; j > 0; j = i - 1, i = M_segment[j]) {
            System.out.print(String.format("[Segment %d - %-2d] : ", i, j));
            System.out.print(String.format("y = %.6f * x + %.6f", a[i][j], b[i][j]));
            System.out.println(String.format(" -> square error : %.6f", err[i][j]));
        }

    }
}
