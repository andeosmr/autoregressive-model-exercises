package org.example;

import static org.example.FUNCTIONS.checkbetween;

public class MATRIX {
    private final double[][] matrix;
    private final int length;
    private final int width;

    private void checkunit() throws MYEXCEPTION {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j) {
                    if (checkbetween(this.get(i, j), 1., .1)) {
                        throw new MYEXCEPTION("inverting matrix went wrong");
                    }
                } else {
                    if (checkbetween(this.get(i, j), 0., .1)) {
                        throw new MYEXCEPTION("inverting matrix went wrong");
                    }
                }
            }
        }
    }

    MATRIX(int ilength, int iwidth) {
        length = ilength;
        width = iwidth;
        matrix = new double[length][width];
    }
    MATRIX(int ilength, int iwidth, double value) {
        length = ilength;
        width = iwidth;
        matrix = new double[length][width];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                if (i == j) {
                    matrix[i][j] = value;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }
    }
    MATRIX(MATRIX imatrix) {
        length = imatrix.length;
        width = imatrix.width;
        matrix = new double[length][width];

        copy(imatrix);
    }

    public MATRIX inverse() throws MYEXCEPTION {
        MATRIX inv = new MATRIX(length, length, 1.);
        MATRIX unit = new MATRIX(this);

        if (length != width) {
            throw new MYEXCEPTION("cannot invert non-square matrix");
        }

        for (int i = 0; i < length; i++) {
            double diag = unit.get(i, i);

            for (int j = 0; j < length; j++) {
                inv.div(i, j, diag);
                unit.div(i, j, diag);
            }

            for (int j = 0; j < length; j++) {
                if (j != i) {
                    double elim = unit.get(j, i);
                    for (int l = 0; l < length; l++) {
                        inv.add(j, l, -inv.get(i, l)*elim);
                        unit.add(j, l, -unit.get(i, l)*elim);
                    }
                }
            }
        }
        try {
            unit.checkunit();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return inv;
    }

    double get(int row, int col) {
        return matrix[row][col];
    }
    void set(int row, int col, double value) {
        matrix[row][col] = value;
    }
    void add(int row, int col, double value) {
        matrix[row][col] += value;
    }
    void mult(int row, int col, double value) {
        matrix[row][col] *= value;
    }
    void div(int row, int col, double value) throws MYEXCEPTION {
        if (value != 0.) {matrix[row][col] /= value;}
        else {throw new MYEXCEPTION("cannot divide by zero");}
    }
    void copy(MATRIX copymatrix) {
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < width; j++) {
                matrix[i][j] = copymatrix.get(i, j);
            }
        }
    }
}