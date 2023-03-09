package org.example;

public class NOISE {
    private final int length;
    private final double[] noise;
    private MATRIX corrmatrix;
    private double[] corrvector;
    private int maxlag;
    private double[] phi;
    private final double variance;

    private double correlation(int lag) {
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
        if (lag != 0) {
            g /= variance;
        }

        return g;
    }
    private void do_corrmatrix() {
        for (int i = 1; i < maxlag; i++) {
            double cov = correlation(i);

            for (int j = 0; j < maxlag - i; j++) {
                corrmatrix.set(j, j + i, cov);
                corrmatrix.set(j + i, j, cov);
            }
        }
    }
    private void do_corrvector(MATRIX corrmatrix) {
        for (int i = 0; i < maxlag - 1; i++) {
            corrvector[i] = corrmatrix.get(0, i + 1);
        }
        corrvector[maxlag - 1] = correlation(maxlag);
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

    NOISE(COLOR color, int ilength) {
        length = ilength;
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

        variance = correlation(0);
    }
    NOISE (double[] iar, int ilength) {
        length = ilength;
        noise = new double[length];
        maxlag = iar.length;

        for (int t = 0; t < length; t++) {
            noise[t] = 0.;
        }
        for (int t = 1; t < length; t++) {
            int max = Math.min(maxlag, t);
            for (int i = 0; i < max; i++) {
                noise[t] += iar[i]*noise[t - i - 1];
            }
            noise[t] += 2.*Math.random() - 1.;
        }

        variance = correlation(0);
    }

    public void do_autoregression(int imaxlag) throws MYEXCEPTION {
        maxlag = imaxlag;
        corrmatrix = new MATRIX(maxlag, maxlag, 1.);
        corrvector = new double[maxlag];

        do_corrmatrix();
        corrmatrix = get_corrmatrix();

        do_corrvector(corrmatrix);
        corrvector = get_corrvector();

        MATRIX invcorrmatrix = corrmatrix.inverse();

        phi = new double[maxlag];
        for (int i = 0; i < maxlag; i++) {
            phi[i] = 0;
            for (int j = 0; j < maxlag; j++) {
                phi[i] += invcorrmatrix.get(i, j)*corrvector[j];
            }
        }
    }

    public double[] get_corrvector() {return corrvector;}
    public MATRIX get_corrmatrix() {return corrmatrix;}
    public double[] get_phi() {return phi;}
    public int get_length() {return length;}
    public int get_maxlag() {return maxlag;}

    double[] get() {
        return noise;
    }

    enum COLOR {
        WHITE,
        BROWN,
        VIOLET,
        SINE
    }
}