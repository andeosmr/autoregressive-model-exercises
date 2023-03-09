package org.example;

public class NOISE {
    private final int length;
    private final double[] noise;
    private MATRIX corrmatrix;
    private double[] corrvector;
    private int maxlag;
    private double[] phi;
    private final double variance;

    private double autocorrelation(int lag) {
        if (lag == length) {
            return 0.;
        }

        if (lag < 0) {
            lag = -lag;
        }

        double g = 0;
        for (int t = lag; t < length; t++) {
            g += noise[t]*noise[t - lag]/(length - lag);
        }

        return g;
    }
    private void do_corrmatrix() {
        for (int i = 1; i < maxlag; i++) {
            double cov = autocorrelation(i)/variance;

            for (int j = 0; j < maxlag - i; j++) {
                corrmatrix.set(j, i + j, cov);
                corrmatrix.set(i + j, j, cov);
            }
        }
    }
    private void do_corrvector() {
        for (int i = 0; i < maxlag - 1; i++) {
            corrvector[i] = corrmatrix.get(0, i + 1);
        }
        corrvector[maxlag - 1] = autocorrelation(maxlag)/variance;
    }
    private void do_whitenoise() {
        for (int t = 0; t < length; t++) {
            noise[t] = 2.*Math.random() - 1.;
        }
    }
    private void do_brownnoise() {
        noise[0] = 0;
        for (int t = 1; t < length; t++) {
            noise[t] = noise[t - 1] + 2.*Math.random() - 1.;
        }
    }
    private void do_violetnoise() {
        do_whitenoise();
        for (int t = 0; t < length - 1; t++) {
            noise[t] = noise[t] - noise[t + 1];
        }
    }
    private void do_sinenoise() {
        for (int t = 0; t < length; t++) {
            noise[t] = 100.*Math.sin(2.*Math.PI*t/50.) + 2.*Math.random() - 1.;
        }
    }

    NOISE(COLOR color, int i_length) {
        length = i_length;
        noise = new double[length];

        if (color == COLOR.WHITE) {
            do_whitenoise();
        }
        if (color == COLOR.BROWN) {
            do_brownnoise();
        }
        if (color == COLOR.VIOLET) {
            do_violetnoise();
        }
        if (color == COLOR.SINE) {
            do_sinenoise();
        }

        variance = autocorrelation(0);
    }
    NOISE (double[] i_phi, int i_length) {
        length = i_length;
        noise = new double[length];
        maxlag = i_phi.length;

        phi = new double[maxlag];
        for (int i = 0; i < maxlag; i++) {
            phi[i] = i_phi[i];
        }

        for (int t = 0; t < length; t++) {
            noise[t] = 0.;
        }
        for (int t = 1; t < length; t++) {
            int max = Math.min(maxlag, t);
            for (int i = 0; i < max; i++) {
                //#3
                //uncomment and fill in the dots
                //noise[...] += phi[...]*noise[...]; //look at slide 6 for inspiration
            }
            //noise[...] += 2.*Math.random() - 1.;
        }

        variance = autocorrelation(0);
    }

    public void do_autoregression(int imaxlag) throws MYEXCEPTION {
        maxlag = imaxlag;
        corrmatrix = new MATRIX(maxlag, maxlag, 1.);
        corrvector = new double[maxlag];

        do_corrmatrix();
        corrmatrix = get_corrmatrix();

        do_corrvector();
        corrvector = get_corrvector();

        MATRIX inv_corrmatrix = corrmatrix.give_inverse(); //inverse of correlation matrix

        phi = new double[maxlag]; //the coefficients of the Yule-Walker equations
        for (int i = 0; i < maxlag; i++) {
            phi[i] = 0;
            for (int j = 0; j < maxlag; j++) {
                //#2
                //uncomment and fill in the dots
                //phi[i] += (...).get(i, j)*(...)[j]; //hint: this is a basic matrix multiplication
                //look at slides 7 & 8 for inspiration
            }
        }
    }

    public double[] get_corrvector() {return corrvector;}
    public MATRIX get_corrmatrix() {return corrmatrix;}
    public double[] get_phi() {return phi;}
    public int get_length() {return length;}
    public int get_maxlag() {return maxlag;}

    public double[] get() {
        return noise;
    }

    public enum COLOR {
        WHITE,
        BROWN,
        VIOLET,
        SINE
    }
}