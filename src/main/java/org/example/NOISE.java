package org.example;

public class NOISE {
    private final int length;
    private final double[] noise;
    private MATRIX covmatrix;
    private double[] covvector;
    private int maxlag;
    private double[] ar;
    private final double variance;

    private double covariance(int lag) {
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
    private void do_covmatrix() {
        for (int i = 1; i < maxlag; i++) {
            double cov = covariance(i);

            for (int j = 0; j < maxlag - i; j++) {
                covmatrix.set(j, j + i, cov);
                covmatrix.set(j + i, j, cov);
            }
        }
    }
    private void do_covvector(MATRIX covmatrix) {
        for (int i = 0; i < maxlag - 1; i++) {
            covvector[i] = covmatrix.get(0, i + 1);
        }
        covvector[maxlag - 1] = covariance(maxlag);
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

        variance = covariance(0);
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

        variance = covariance(0);
    }

    public void do_autoregression(int imaxlag) throws MYEXCEPTION {
        maxlag = imaxlag;
        covmatrix = new MATRIX(maxlag, maxlag, 1.);
        covvector = new double[maxlag];

        do_covmatrix();
        covmatrix = get_covmatrix();

        do_covvector(covmatrix);
        covvector = get_covvector();

        MATRIX invcovmatrix = covmatrix.inverse();

        ar = new double[maxlag];
        for (int i = 0; i < maxlag; i++) {
            ar[i] = 0;
            for (int j = 0; j < maxlag; j++) {
                ar[i] += invcovmatrix.get(i, j)*covvector[j];
            }
        }
    }

    public double[] get_covvector() {return covvector;}
    public MATRIX get_covmatrix() {return covmatrix;}
    public double[] get_ar() {return ar;}
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