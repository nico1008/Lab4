import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

public class FractalExplorer{
    private int size;
    private JImageDisplay display;
    private FractalGenerator fractalGenerator;
    private Rectangle2D.Double range;

    public FractalExplorer(int size){
        this.size = size;
        this.range = new Rectangle2D.Double();
        this.fractalGenerator = new Mandelbrot();
        this.display = new JImageDisplay(size, size);
    }

    public void createAndShowGUI(){
        JFrame frame = new JFrame("Fractal Explorer");
        Button buttonReset = new Button("Reset Display");
        ActionListener actionListener = new buttonResetClick();
        MouseListener mouseListener = new displayMouseClick();
        buttonReset.addActionListener(actionListener);
        frame.addMouseListener(mouseListener);
        frame.getContentPane().add(display, BorderLayout.CENTER);
        frame.getContentPane().add(buttonReset, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void drawFractal(){
        double xCoord, yCoord;
        for (int x = 0; x < size; x++){
            for (int y = 0; y < size; y++){
                xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, x);
                yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, y);
                int iterations = fractalGenerator.numIterations(xCoord, yCoord);
                int rgbColor;
                if (iterations == -1){
                    rgbColor = 0;
                }
                else{
                    float hue = 0.7f + (float) iterations / 200f;
                    rgbColor = Color.HSBtoRGB(hue, 1f, 1f);
                }
                display.drawPixel(x, y, rgbColor);
            }
            display.repaint();
        }
        display.repaint();
    }
    private class buttonResetClick implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            fractalGenerator.getInitialRange(range);
            drawFractal();
        }
    }

    private class displayMouseClick implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            double xCoord = FractalGenerator.getCoord(range.x, range.x + range.width, size, e.getX());
            double yCoord = FractalGenerator.getCoord(range.y, range.y + range.height, size, e.getY());
            fractalGenerator.recenterAndZoomRange(range, xCoord, yCoord,0.5);
            drawFractal();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        FractalExplorer fractalExplorer = new FractalExplorer(800);
        fractalExplorer.createAndShowGUI();
        fractalExplorer.fractalGenerator.getInitialRange(fractalExplorer.range);
        fractalExplorer.drawFractal();
    }

}
