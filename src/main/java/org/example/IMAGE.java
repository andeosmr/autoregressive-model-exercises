package org.example;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class IMAGE {
    private final int horizontal;
    private final int vertical;
    private final Graphics g;
    private final BufferedImage image;
    private double[] data;
    private int length;
    private double max;

    private void max() {
        max = data[0];

        for (int i = 1; i < length; i++) {
            if (data[i]*data[i] > max*max) {
                max = data[i];
            }
        }

        if (max < 0) {
            max = -max;
        }
    }
    private void normalize() {
        max();

        for (int i = 0; i < length; i++) {
            data[i] /= max;
        }
    }

    IMAGE(int ihorizontal, int ivertical, Color color) {
        horizontal = ihorizontal;
        vertical = ivertical;

        image = new BufferedImage(horizontal, vertical, BufferedImage.TYPE_INT_RGB);
        g = image.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, horizontal, vertical);
    }

    public void draw(Boolean axis, Boolean norm, Color color, double[] idata, int ilength) {
        length = ilength;
        data = new double[length];

        System.arraycopy(idata, 0, data, 0, length);

        if (norm) {
            normalize();
        }

        if (axis) {
            g.setColor(Color.black);
            for (int i = 0; i <= 21; i++) {
                if (i == 11) {g.setColor(Color.red);}
                g.fillRect(0, 1 + vertical - (int)(i*(vertical/1.1)*.05), 10, 3);
                if (i == 11) {g.setColor(Color.black);}
            }
        }

        g.setColor(color);
        for (int i = 0; i < length; i++) {
            g.fillRect(i*horizontal/length, vertical/2 - (int)((vertical/1.1)/2.*data[i]),  7,  7);
        }
    }

    public void create(String name) {
        try {
            ImageIO.write(image, "jpg", new File(name + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}