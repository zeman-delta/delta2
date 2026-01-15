import models.Line;
import models.LineCanvas;
import models.Point;
import rasterizers.LineCanvasRasterizer;
import rasterizers.Rasterizer;
import rasterizers.TrivialRasterizer;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;

public class App {

    private final JPanel panel;
    private final Raster raster;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;
    private Point point;
    private Rasterizer rasterizer;
    private LineCanvas lineCanvas;
    private LineCanvasRasterizer lineCanvasRasterizer;
    private boolean dottedMode = false;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(800, 600).start());
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    public App(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("Delta : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        rasterizer = new TrivialRasterizer(Color.CYAN, raster);

        createAdapters();
        panel.addMouseMotionListener(mouseAdapter);
        panel.addMouseListener(mouseAdapter);
        panel.addKeyListener(keyAdapter);

        panel.requestFocus();
        panel.requestFocusInWindow();

        lineCanvas = new LineCanvas();
        // TODO create dotted rasterizer
        lineCanvasRasterizer = new LineCanvasRasterizer(rasterizer, rasterizer);
    }


    private void createAdapters() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, dottedMode);

                raster.clear();

                lineCanvasRasterizer.rasterizeCanvas(lineCanvas);
                rasterizer.rasterize(line);

                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, dottedMode);

                lineCanvas.addLine(line);

                raster.clear();
                lineCanvasRasterizer.rasterizeCanvas(lineCanvas);
                panel.repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                point = new Point(e.getX(), e.getY());

                System.out.println(point);
            }
        };

        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    dottedMode = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    dottedMode = false;
                }
            }
        };
    }

}
