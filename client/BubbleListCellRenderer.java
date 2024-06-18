package client;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class BubbleListCellRenderer extends JLabel implements ListCellRenderer<Object> {
    public BubbleListCellRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
            boolean cellHasFocus) {
        setText(value.toString());

        // テキストを丸い枠で囲むための設定
        setBorder(BorderFactory.createMatteBorder(10, 10, 10, 10, Color.WHITE));
        setOpaque(true);
        setBackground(Color.LIGHT_GRAY);
        setForeground(Color.BLACK);

        if (isSelected) {
            setBackground(Color.BLUE);
            setForeground(Color.WHITE);
        } else {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.BLACK);
        }

        return this;
    }
}
