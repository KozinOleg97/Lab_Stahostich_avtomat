package com.company;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static double[][] globalMainMatr;
    public static double[] globalMainVecor;

    public static int myN = 0;


    private static int currentIndex = -1;

    private static Integer next(String numbers[]) {
        ++currentIndex;
        while (currentIndex < numbers.length
                && numbers[currentIndex].equals(""))
            ++currentIndex;
        return currentIndex < numbers.length ? Integer
                .parseInt(numbers[currentIndex]) : null;
    }

    private static Double nextDouble(String numbers[]) {
        ++currentIndex;
        while (currentIndex < numbers.length
                && numbers[currentIndex].equals(""))
            ++currentIndex;
        return currentIndex < numbers.length ? Double
                .parseDouble(numbers[currentIndex]) : null;
    }


    public static double[][] ReadMatr() throws IOException {
        FileInputStream inFile = new FileInputStream(
                "v1.txt");

        byte[] str = new byte[inFile.available()];

        inFile.read(str);
        String text = new String(str);

        String[] numbers = text.split(" |\r\n");
        int i, j;
        int n = next(numbers), m = next(numbers);
        double matr[][] = new double[n][m];

        for (i = 0; i < n; ++i)
            for (j = 0; j < m; ++j)
                matr[i][j] = nextDouble(numbers);

        /*for (i = 0; i < n; ++i, System.out.println())
            for (j = 0; j < m; ++j)
                System.out.print(matr[i][j] + " ");*/

        matrPrint(matr);

        return matr;
    }


    public static double[][] multiplyByMatrix(double[][] m1, double[][] m2) {
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if (m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        double[][] mResult = new double[mRRowLength][mRColLength];
        for (int i = 0; i < mRRowLength; i++) {         // rows from m1
            for (int j = 0; j < mRColLength; j++) {     // columns from m2
                for (int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += m1[i][k] * m2[k][j];
                }
            }
        }
        return mResult;
    }


    public static double[] multiplyByVector(double[] vector, double[][] matrix) {


        int n = matrix[0].length;//столбцы
        int m = matrix.length;//строки

        double x = 0;
        double[] res = new double[vector.length];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                x += vector[i] * matrix[j][i];
            }
            res[i] = x;
            x = 0.0;
        }
        return res;
    }

    public static double[] CalcMath(int steps, double[][] mainMatr, double[] vect) {
        double[][] matt = mainMatr;
        //matrPrnt(vect);

        for (int i = 1; i < steps; i++) {
            matt = multiplyByMatrix(matt, mainMatr);
        }

        double[] res = multiplyByVector(vect, matt);

        return res;
    }


    public static String matrPrnt2(double[] matr) {
        //System.out.println();
        String str = "", resStr = "[";
        int to = 7;
        for (int i = 0; i < matr.length; i++) {
            str = new DecimalFormat("0.000").format(matr[i]);
            /*if (str.length() < to) {
                to=str.length()-1;
            }*/
            /*str = str.substring(0, to);*/
            resStr += (str + "; ");
            //to = 7;
        }
        resStr= resStr.trim();
        resStr+="]";
        return resStr;
    }

    public static void matrPrnt(double[] matr) {
        System.out.println(Arrays.toString(matr));
    }

    public static void matrPrint(double[][] matr) {
        for (int i = 0; i < matr.length; i++) {
            System.out.println(Arrays.toString(matr[i]));
        }

    }


    public static double[][] automat(double[] vector, double[][] matrVer, int steps) {
        double[] vect = vector.clone();
        double[][] stepsStat = new double[steps][vect.length];
        for (int repeats = 0; repeats < steps; repeats++) {

            System.out.println("-------Итерация " + (repeats + 1) + " ---------");

            double rz = 0;
            int Min = 1;
            int Max = 99;
            double dice = Min + (int) (Math.random() * ((Max - Min) + 1));
            dice /= 100;
            double[] diceField = new double[vect.length + 1];


            for (int i = 0; i < vect.length; i++) {
                rz += vect[i];
                diceField[i + 1] = rz;
            }
            rz = 1;

            int numb = 0;
            double znak = 0;

            for (int i = 0; i < vect.length; i++) {
                if (Math.abs(dice - diceField[i]) < rz) {
                    numb = i;
                    rz = Math.abs(dice - diceField[i]);
                    znak = dice - diceField[i];
                }
            }
            if (znak < 0) {
                numb--;
            }

            for (int i = 0; i < vect.length; i++) {
                if (repeats > 0) {
                    stepsStat[repeats][i] = stepsStat[repeats - 1][i];
                }
            }
            stepsStat[repeats][numb]++;


            //System.out.println("Пути: ");
            matrPrnt(vect);

            System.out.println("Выбрано направление - " + (numb + 1));

            for (int i = 0; i < vect.length; i++) {
                vect[i] = globalMainMatr[numb][i];
            }

        }

        return stepsStat;
    }


    public static void showAutomatStats(double[] automatStats, int steps) {
        System.out.println("Результат автомата: ");

        for (int i = 0; i < automatStats.length; i++) {
            automatStats[i] /= steps;
        }
        matrPrnt(automatStats);
    }

    public static void showMathStats(double[] mathStats) {
        System.out.println("Результат моделирования: ");

        matrPrnt(mathStats);
    }

    public static double[][] model(double[] vector, double[][] matr, int steps) {
        double[] resVect = null;
        double[][] fullRes = new double[steps][vector.length];
        for (int i = 0; i < steps; i++) {
            //System.out.println("-------Итерация " + (i + 1) + " ---------");
            resVect = CalcMath(i+1, matr, vector);
            //matrPrnt(resVect);


            fullRes[i] = resVect;
        }
        return fullRes;
    }

    public static void main(String args[]) throws IOException {

        System.out.println("Введи кол-во шагов");
        Scanner in = new Scanner(System.in);
        int steps = in.nextInt();


        double[] vectNormForm = new double[]{0.333333, 0.333333, 0.333333};
        double[] resVect = new double[vectNormForm.length];

        double mainMatr[][] = ReadMatr();


        globalMainMatr = mainMatr.clone();
        globalMainVecor = vectNormForm.clone();


        double[][] modelStats = model(vectNormForm, mainMatr, steps);
        double[][] automatStats = automat(vectNormForm, mainMatr, steps);


        //showAutomatStats(automatStats, steps);
        //showMathStats(modelStats);

        for (int reps = 0; reps < steps; reps++) {
            for (int i = 0; i < globalMainVecor.length; i++) {
                automatStats[reps][i] /= reps + 1;
            }
        }
        for (int i = 0; i < steps; i++) {
            System.out.println((i+1) + ") " + matrPrnt2(automatStats[i]) + " - " + matrPrnt2(modelStats[i]));
            /*for (int j = 0; j < automatStats.length; j++) {


                //System.out.printf( automatStats[i][j] + " ");
            }*/
            //System.out.println("");
        }
    }
}

