package org.example.view;

import org.example.config.ConfigManager;
import org.example.config.GameConfig;

import javax.swing.*;
import java.awt.*;

public class SettingsScreen extends JPanel {

    private final JComboBox<Integer> rows;
    private final JComboBox<Integer> cols;
    private final JComboBox<Integer> speed;
    private final JCheckBox rotation;
    private final JButton saveBtn;
    private final JButton backBtn;

    public SettingsScreen(Runnable onSaved, Runnable onBack) {
        /* widgets */
        rows = combo(15, 20, 25, 30);
        cols = combo(8, 10, 12, 15);
        speed = combo(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        rotation = new JCheckBox("Allow Rotation");
        saveBtn = new JButton("Save");
        backBtn = new JButton("Back");

        /* بارگذاری مدل فعلی از ConfigManager */
        GameConfig cfg = ConfigManager.getInstance().config();
        rows.setSelectedItem(cfg.getRows());
        cols.setSelectedItem(cfg.getCols());
        speed.setSelectedItem(cfg.getSpeed());
        rotation.setSelected(cfg.isAllowRotation());

        /* layout */
        setBackground(Color.GRAY);
        buildLayout();

        /* wiring: وقتی Save زده شد */
        saveBtn.addActionListener(e -> {
            // ۱) برو مقدارها رو داخل مدل جاری بریز
            applyTo(cfg);
            // ۲) کانفیگ را بنویس روی دیسک
            ConfigManager.getInstance().save(cfg);
            // ۳) پیام کوچیک موفقیت، بعد onSaved اگر وجود داشت اجرا کن
            JOptionPane.showMessageDialog(this, "Settings saved!", "Tetris",
                    JOptionPane.INFORMATION_MESSAGE);
            if (onSaved != null) onSaved.run();
        });

        /* wiring: وقتی Back زده شد */
        backBtn.addActionListener(e -> {
            if (onBack != null) onBack.run();
        });
    }

    @SafeVarargs
    private static JComboBox<Integer> combo(Integer... values) {
        return new JComboBox<>(values);
    }

    private void buildLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 4, 4, 4);
        c.fill = GridBagConstraints.HORIZONTAL;

        addRow(c, 0, label("Rows:"), rows);
        addRow(c, 1, label("Cols:"), cols);
        addRow(c, 2, label("Speed:"), speed);
        addRow(c, 3, rotation, new JLabel()); // placeholder
        addRow(c, 4, saveBtn, backBtn);
    }

    private JLabel label(String text) {
        return new JLabel(text, SwingConstants.RIGHT);
    }

    private void addRow(GridBagConstraints c, int y, Component left, Component right) {
        c.gridy = y;
        c.weightx = 0.3;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        add(left, c);

        c.weightx = 0.7;
        c.gridx = 1;
        c.anchor = GridBagConstraints.LINE_START;
        add(right, c);
    }

    /** کپی مقادیر فرم توی مدل */
    private void applyTo(GameConfig cfg) {
        cfg.setRows((int) rows.getSelectedItem());
        cfg.setCols((int) cols.getSelectedItem());
        cfg.setSpeed((int) speed.getSelectedItem());
        cfg.setAllowRotation(rotation.isSelected());
    }
}
