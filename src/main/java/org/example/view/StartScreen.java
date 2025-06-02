package org.example.view;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class StartScreen extends JPanel {
    private final JButton startBtn    = new JButton("Start Game");
    private final JButton settingsBtn = new JButton("Settings");
    private final JButton exitBtn     = new JButton("Exit");

    public StartScreen() {
        // background color
        setBackground(Color.BLACK);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // title
        JLabel title = new JLabel("TETRIS");
        title.setFont(new Font("Consolas", Font.BOLD, 72));
        title.setForeground(new Color(0, 255, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        // spacer before and after
        add(Box.createVerticalGlue());
        add(title);
        add(Box.createVerticalStrut(50));

        // style and add all buttons
        for (JButton btn : Arrays.asList(startBtn, settingsBtn, exitBtn)) {
            styleMenuButton(btn);
            add(btn);
            add(Box.createVerticalStrut(20));
        }

        add(Box.createVerticalGlue());
    }

    private void styleMenuButton(JButton b) {
        b.setFont(new Font("Arial", Font.BOLD, 24));
        b.setBackground(new Color(30, 30, 30));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(220, 50));
        b.setPreferredSize(new Dimension(220, 50));
        b.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        // optional rollover
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(60, 60, 60));
            }
            @Override public void mouseExited(java.awt.event.MouseEvent e) {
                b.setBackground(new Color(30, 30, 30));
            }
        });
    }

    public JButton getStartButton()    { return startBtn; }
    public JButton getSettingsButton() { return settingsBtn; }
    public JButton getExitButton()     { return exitBtn; }
}
