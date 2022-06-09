package com.skuratov.labma.client.draw;

import com.skuratov.labma.client.draw.model.CurveLine;

import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

/**
 * Class for drawing the graphics window.
 */
public class MainWindow extends JFrame {
    private ArrayList<CurveLine> curveLineList;

    /**
     * The method initializes the main elements and creates a jpanel to redraw the shapes.
     * @param width - window graphics width
     * @param height - window graphics height
     */
    public MainWindow(int width, int height) {
        this.setSize(width, height);
        curveLineList = new ArrayList<>();

        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g)  //The JPanel paint method we are overriding.
            {
                float coeffSize = (this.getWidth() + this.getHeight()) / 2.0f;
                if (!curveLineList.isEmpty()) {
                    for (CurveLine curveLine : curveLineList) {
                        for (int i = 1; i < curveLine.getListPoints().size(); i++) {
                            int x = (int) (curveLine.getListPoints().get(i).getX() * coeffSize);
                            int y = (int) (curveLine.getListPoints().get(i).getY() * coeffSize);
                            Color color = curveLine.getListPoints().get(i).getColor();
                            g.setColor(color);
                            g.fillOval(x,y,7,7);
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

    public void setCurveLineList(ArrayList<CurveLine> curveLineList) {
        this.curveLineList = curveLineList;
    }
}