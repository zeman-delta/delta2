package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;

public class TrivialRasterizer implements Rasterizer {

    private Color defaultColor = Color.RED;
    private Raster raster;

    public TrivialRasterizer(Color defaultColor, Raster raster) {
        this.defaultColor = defaultColor;
        this.raster = raster;
    }

    @Override
    public void setColor(Color color) {
        defaultColor = color;
    }

    @Override
    public void setRaster(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        double k = calculateK(line);
        double q = calculateQ(line.getP1(), k);

        // TODO ošetřit mimo hranice rastru
        // TODO ošetřit vykreslování VŠEMI SMĚRY (zkuste i úplně svyslou čáru)

        if (k < 1) {
            // TODO prohodit body pokud je potřeba
            if (line.getP1().getX() > line.getP2().getX()) {
                Point pointPomoc = line.getP1();
                line.setP1(line.getP2());
                line.setP2(pointPomoc);
            }

            for (int x = line.getP1().x; x <= line.getP2().getX(); x++) {
                int y = (int) Math.round(k * x + q);

                raster.setPixel(x, y, defaultColor.getRGB());
            }
        } else {
            // TODO prohodit body pokud je potřeba

            for (int y = line.getP1().y; y <= line.getP2().y; y++) {
                int x = (int) Math.round((y - q) / k);

                raster.setPixel(x, y, defaultColor.getRGB());
            }
        }
    }


    private double calculateK (Line line) {
        return (line.getP2().getY() - line.getP1().getY())
                / (double) (line.getP2().getX() - line.getP1().getX());
    }


    private double calculateQ (Point p, double k) {
        return p.getY() - k * p.getX();
    }

}
