package client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.AbstractBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;

public class BubbleListCellRenderer extends JLabel implements ListCellRenderer<Object> {
    private final int width;
    private final int horizontalMargin;

    public BubbleListCellRenderer(int width, int horizontalMargin) {
        setOpaque(true);
        this.width = width;
        this.horizontalMargin = horizontalMargin;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        setText(value.toString());

        // Create a compound border with rounded edges and horizontal margin
        CompoundBorder compoundBorder = new CompoundBorder(
                new RoundBorder(10, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10) // top, left, bottom, right padding
        );
        setBorder(compoundBorder);

        setOpaque(true);

        if (isSelected) {
            setBackground(Color.CYAN);
            setForeground(Color.BLACK);
        } else {
            setBackground(Color.WHITE);
            setForeground(Color.BLACK);
        }

        // Set preferred size with horizontal margin
        setPreferredSize(new Dimension(width - (2 * horizontalMargin), getPreferredSize().height));

        return this;
    }

    private static class RoundBorder extends AbstractBorder {
        private final int radius;
        private final Color color;

        public RoundBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(color);
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius;
            return insets;
        }
    }
}