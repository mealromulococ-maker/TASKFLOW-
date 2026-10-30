/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

public class PhotoCropDialog extends JDialog {

    private final BufferedImage source;
    private BufferedImage croppedImage;

    private final CropPanel cropPanel = new CropPanel();
    private final JSlider zoomSlider =
            new JSlider(100, 300, 100);

    public PhotoCropDialog(
            Window owner, BufferedImage source) {

        super(owner, "Crop Profile Photo",
                ModalityType.APPLICATION_MODAL);

        this.source = source;

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(430, 520);
        setResizable(false);
        setLocationRelativeTo(owner);

        JButton useButton = new JButton("Use Photo");
        JButton cancelButton = new JButton("Cancel");

        zoomSlider.setMajorTickSpacing(50);
        zoomSlider.setPaintTicks(true);
        zoomSlider.setPaintLabels(true);

        zoomSlider.addChangeListener(e ->
                cropPanel.setZoom(zoomSlider.getValue() / 100.0));

        useButton.addActionListener(e -> {
            croppedImage = cropPanel.createCrop();
            dispose();
        });

        cancelButton.addActionListener(e -> {
            croppedImage = null;
            dispose();
        });

        JPanel controls = new JPanel(new BorderLayout(5, 5));
        controls.add(new JLabel("Zoom"), BorderLayout.WEST);
        controls.add(zoomSlider, BorderLayout.CENTER);

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT));
        buttons.add(cancelButton);
        buttons.add(useButton);

        setLayout(new BorderLayout(10, 10));
        add(cropPanel, BorderLayout.CENTER);
        add(controls, BorderLayout.NORTH);
        add(buttons, BorderLayout.SOUTH);
    }

    public BufferedImage getCroppedImage() {
        return croppedImage;
    }

    private class CropPanel extends JPanel {

        private double zoom = 1.0;
        private double offsetX;
        private double offsetY;

        private int lastX;
        private int lastY;

        CropPanel() {
            setBackground(new Color(35, 35, 35));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    lastX = e.getX();
                    lastY = e.getY();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    offsetX += e.getX() - lastX;
                    offsetY += e.getY() - lastY;

                    lastX = e.getX();
                    lastY = e.getY();

                    repaint();
                }
            });
        }

        void setZoom(double value) {
            zoom = value;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);

            Graphics2D g = (Graphics2D) graphics.create();

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            int size = Math.min(
                    getWidth() - 30,
                    getHeight() - 30);

            int x = (getWidth() - size) / 2;
            int y = (getHeight() - size) / 2;

            double scale = Math.max(
                    (double) size / source.getWidth(),
                    (double) size / source.getHeight()) * zoom;

            int drawWidth =
                    (int) (source.getWidth() * scale);
            int drawHeight =
                    (int) (source.getHeight() * scale);

            int drawX =
                    x + (size - drawWidth) / 2
                    + (int) offsetX;
            int drawY =
                    y + (size - drawHeight) / 2
                    + (int) offsetY;

            Shape circle = new Ellipse2D.Double(
                    x, y, size, size);

            g.setClip(circle);

            g.drawImage(
                    source,
                    drawX,
                    drawY,
                    drawWidth,
                    drawHeight,
                    null);

            g.setClip(null);
            g.setColor(Color.WHITE);
            g.setStroke(new BasicStroke(2));
            g.draw(circle);

            g.dispose();
        }

        BufferedImage createCrop() {
            int size = 400;

            BufferedImage result = new BufferedImage(
                    size, size,
                    BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = result.createGraphics();

            g.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            g.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g.setClip(new Ellipse2D.Double(
                    0, 0, size, size));

            int panelSize = Math.min(
                    getWidth() - 30,
                    getHeight() - 30);

            double scale = Math.max(
                    (double) panelSize / source.getWidth(),
                    (double) panelSize / source.getHeight())
                    * zoom;

            double drawWidth = source.getWidth() * scale;
            double drawHeight = source.getHeight() * scale;

            double x = (getWidth() - panelSize) / 2.0;
            double y = (getHeight() - panelSize) / 2.0;

            double drawX =
                    x + (panelSize - drawWidth) / 2.0
                    + offsetX;
            double drawY =
                    y + (panelSize - drawHeight) / 2.0
                    + offsetY;

            double factor = (double) size / panelSize;

            int finalWidth =
                    (int) Math.round(drawWidth * factor);
            int finalHeight =
                    (int) Math.round(drawHeight * factor);

            int finalX =
                    (int) Math.round((drawX - x) * factor);
            int finalY =
                    (int) Math.round((drawY - y) * factor);

            g.drawImage(
                    source,
                    finalX,
                    finalY,
                    finalWidth,
                    finalHeight,
                    null);

            g.dispose();

            return result;
        }
    }
}

