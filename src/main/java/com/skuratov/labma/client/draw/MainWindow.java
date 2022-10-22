package com.skuratov.labma.client.draw;

import com.skuratov.labma.client.draw.model.CurveLine;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Class for drawing the graphics window.
 */
public class MainWindow extends JFrame {
    private final List<CurveLine> curves = new ArrayList<>();

    public void addCurves(List<CurveLine> updateCurves) {
        this.curves.addAll(updateCurves);
    }


    /**
     * The method initializes the main elements and creates a jpanel to redraw the shapes.
     * @param width  - window graphics width
     * @param height - window graphics height
     */
    public MainWindow(int width, int height) {
        this.setSize(width, height);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g)  //The JPanel paint method we are overriding.
            {
                float resizeCoeff = (this.getWidth() + this.getHeight()) / 2.0f;
                if (!curves.isEmpty()) {
                    for (CurveLine curveLine : curves) {
                        for (int i = 1; i < curveLine.getPoints().size(); i++) {
                            int x = (int) (curveLine.getPoints().get(i).getX() * resizeCoeff);
                            int y = (int) (curveLine.getPoints().get(i).getY() * resizeCoeff);
                            Color color = curveLine.getPoints().get(i).getColor();
                            g.setColor(color);
                            g.fillOval(x, y, 7, 7);
                        }
                    }
                }
            }
        };
        panel.setPreferredSize(new Dimension(width, height)); //Setting the panel size
        this.getContentPane().add(panel);
        pack();
        setVisible(true);
    }

    public List<CurveLine> getCurves() {
        return curves;
    }
}