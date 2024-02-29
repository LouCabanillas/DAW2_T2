package com.cibertec.assessment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawTest extends JFrame {

    private List<Polygon> polygons;
    private Polygon square;

    public DrawTest(List<Polygon> polygons, Polygon square) {
        this.polygons = polygons;
        this.square = square;

        setTitle("Draw Polygons");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLUE);
        for (Polygon polygon : polygons) {
            g.drawPolygon(polygon);
        }

        g.setColor(Color.RED);
        g.drawPolygon(square);
    }

    public void addSquare(Polygon square) {
        polygons.add(square);
        repaint();
    }

    public static void main(String[] args) {
        List<Polygon> polygons = new ArrayList<>();

        Polygon polygon1 = createPolygon("[50, 100, 150, 120, 70]", "[25, 75, 50, 100, 90]");
        Polygon polygon2 = createPolygon("[80, 160, 240, 200, 120]", "[40, 120, 80, 160, 144]");
        Polygon polygon3 = createPolygon("[120, 240, 360, 300, 180]", "[60, 180, 120, 240, 216]");
        Polygon polygon4 = createPolygon("[200, 400, 600, 500, 300]", "[100, 300, 200, 400, 360]");
        Polygon polygon5 = createPolygon("[300, 600, 900, 750, 450]", "[150, 450, 300, 600, 540]");

        polygons.add(polygon1);
        polygons.add(polygon2);
        polygons.add(polygon3);
        polygons.add(polygon4);
        polygons.add(polygon5);

        Polygon square = createPolygon("[50, 100, 100, 50]", "[50, 50, 100, 100]");

        SwingUtilities.invokeLater(() -> {
            DrawTest drawPolygons = new DrawTest(polygons, square);
            drawPolygons.addSquare(square);
        });
    }

    private static Polygon createPolygon(String xPointsStr, String yPointsStr) {
        int[] xPoints = convertStringToIntArray(xPointsStr);
        int[] yPoints = convertStringToIntArray(yPointsStr);
        return new Polygon(xPoints, yPoints, xPoints.length);
    }

    private static int[] convertStringToIntArray(String pointsStr) {
        String[] pointsArray = pointsStr.substring(1, pointsStr.length() - 1).split(", ");
        int[] result = new int[pointsArray.length];
        for (int i = 0; i < pointsArray.length; i++) {
            result[i] = Integer.parseInt(pointsArray[i]);
        }
        return result;
    }
}
