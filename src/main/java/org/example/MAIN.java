package org.example;
import java.awt.*;

public class MAIN {
    private static void create_jpg(double[] data, int length, String name, Boolean normalized) {
        IMAGE arimage = new IMAGE(1000, 1000, Color.white);
        arimage.draw(true, normalized, Color.black, data, length);
        arimage.create(name);
    }
    private static void drawimages(NOISE noise) {
        create_jpg(noise.get_phi(), noise.get_maxlag(), "phi", false);

        create_jpg(noise.get_corrvector(), noise.get_maxlag(), "corrvector", false);

        create_jpg(noise.get(), noise.get_length(), "noise", true);
    }
    public static void main(String[] args) throws MYEXCEPTION {
        NOISE noise = new NOISE(NOISE.COLOR.BROWN, 10000);

        noise.do_autoregression(100);

        drawimages(noise);

        NOISE next_noise = new NOISE(noise.get_phi(), 10000);

        create_jpg(next_noise.get(), next_noise.get_length(), "next_noise", true);
    }
}