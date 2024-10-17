package disaster_visualizer.components;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Map extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        // double scaleX = panelWidth / (MercatorProjection.mercatorX(180) - MercatorProjection.mercatorX(-180));
        // double scaleY = panelHeight / (MercatorProjection.mercatorY(85) - MercatorProjection.mercatorY(-85));

        // Path2D path = new Path2D.Double();
        // boolean firstPoint = true;

        // for (double[] coord : countryCoordinates) {
        //     double x = MercatorProjection.mercatorX(coord[0]) * scaleX;
        //     double y = panelHeight - MercatorProjection.mercatorY(coord[1]) * scaleY; // Flip Y for correct orientation

        //     if (firstPoint) {
        //         path.moveTo(x, y);
        //         firstPoint = false;
        //     } else {
        //         path.lineTo(x, y);
        //     }
        // }

        // // Close the shape and draw it
        // path.closePath();
        // g2d.draw(path);
    }

}
