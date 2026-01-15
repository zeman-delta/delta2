package rasterizers;

import models.Line;
import models.LineCanvas;

public class LineCanvasRasterizer {

    private Rasterizer lineRasterizer;
    private Rasterizer dottedLineRasterizer;

    public LineCanvasRasterizer(Rasterizer lineRasterizer, Rasterizer dottedLineRasterizer) {
        this.lineRasterizer = lineRasterizer;
        this.dottedLineRasterizer = dottedLineRasterizer;
    }

    public void rasterizeCanvas(LineCanvas lineCanvas) {
        for (Line line : lineCanvas.getLines()) {
            if (line.isDotted()) {
                dottedLineRasterizer.rasterize(line);
            } else {
                lineRasterizer.rasterize(line);
            }
        }
    }

}
