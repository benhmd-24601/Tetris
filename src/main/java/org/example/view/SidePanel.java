package org.example.view;

import org.example.config.ConfigManager;
import org.example.config.GameConfig;
import org.example.config.Resolution;
import org.example.controller.GameController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SidePanel extends JPanel {

    private final int panelWidth;
    private final GameController ctrl;

    private final JLabel scoreLabel   = new JLabel("Score: 0");
    private final JLabel timeLabel    = new JLabel("Time: 00:00");
    private final JButton pauseResumeBtn;
    private final JButton restartBtn;
    private final JButton backBtn;

    private long startTime;            // برای محاسبهٔ elapsed
    private long elapsedBeforePause;   // زمان سپری‌شده تا قبل از Pause
    private final Timer clockTimer;

    public SidePanel(GameController ctrl) {
        this.ctrl = ctrl;
        this.startTime = System.currentTimeMillis();
        this.elapsedBeforePause = 0;

        // از اولین رزولوشن کانفیگ، عرض ساید‌پنل را بگیر
        GameConfig cfg = ConfigManager.getInstance().config();
        Resolution res = cfg.getResolutions().get(0);
        this.panelWidth = res.getSidePanelWidth();

        // ==== ظاهر ساید پنل ====
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(panelWidth, 0));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(new Color(50, 50, 80));

        // استایل برچسب امتیاز
        scoreLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        scoreLabel.setForeground(Color.WHITE);

        // استایل برچسب زمان
        timeLabel.setFont(new Font("Consolas", Font.BOLD, 18));
        timeLabel.setForeground(Color.WHITE);

        // دکمهٔ Pause/Resume
        pauseResumeBtn = new JButton("Pause");
        pauseResumeBtn.setFont(new Font("Consolas", Font.BOLD, 14));
        pauseResumeBtn.setMaximumSize(new Dimension(panelWidth - 20, 30));
        pauseResumeBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pauseResumeBtn.addActionListener(this::onPauseResumeClicked);

        // دکمهٔ Restart
        restartBtn = new JButton("Restart");
        restartBtn.setFont(new Font("Consolas", Font.BOLD, 14));
        restartBtn.setMaximumSize(new Dimension(panelWidth - 20, 30));
        restartBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        restartBtn.addActionListener(this::onRestartClicked);

        // دکمهٔ Back (Listener را در TetrisView نصب می‌کنیم)
        backBtn = new JButton("Back");
        backBtn.setFont(new Font("Consolas", Font.BOLD, 14));
        backBtn.setMaximumSize(new Dimension(panelWidth - 20, 30));
        backBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // چینش آیتم‌ها در پنل
        add(scoreLabel);
        add(Box.createVerticalStrut(20));
        add(timeLabel);
        add(Box.createVerticalGlue());

        add(pauseResumeBtn);
        add(Box.createVerticalStrut(10));
        add(restartBtn);
        add(Box.createVerticalStrut(10));
        add(backBtn);
        add(Box.createVerticalGlue());

        // Timer داخلی برای به‌روز کردنِ برچسب زمان هر ثانیه
        clockTimer = new Timer(1000, e -> updateTime());
        clockTimer.start();
    }

    /** وقتی Pause/Resume کلیک شد */
    private void onPauseResumeClicked(ActionEvent e) {
        if ("Pause".equals(pauseResumeBtn.getText())) {
            // ===== Pause کردن =====
            ctrl.pauseToggle();   // مدلِ بازی را Paused می‌کند

            // ذخیرهٔ زمان سپری‌شده تا الان قبل از Stop
            long now = System.currentTimeMillis();
            elapsedBeforePause = now - startTime;

            clockTimer.stop();    // تایمر View را متوقف کن
            pauseResumeBtn.setText("Resume");
        } else {
            // ===== Resume کردن =====
            ctrl.pauseToggle();   // مدلِ بازی را Resume می‌کند

            // چون می‌خواهیم از همون نقطه ادامه دهیم:
            startTime = System.currentTimeMillis() - elapsedBeforePause;
            clockTimer.start();

            pauseResumeBtn.setText("Pause");
        }
    }

    /** وقتی Restart کلیک شد */
    private void onRestartClicked(ActionEvent e) {
        ctrl.start();               // برد و مدلِ بازی را ریست کنید
        resetClock();               // زمانِ View را از اول صفر کند
        scoreLabel.setText("Score: 0");
        pauseResumeBtn.setText("Pause"); // دکمه حتماً Pause شود
    }

    /** هر بار برای نمایش امتیاز تازه فراخوانده می‌شود */
    public void refresh() {
        scoreLabel.setText("Score: " + ctrl.getScore());
    }

    /** هر ثانیه فراخوانده می‌شود تا برچسب زمان آپدیت شود */
    private void updateTime() {
        long elapsedMs = System.currentTimeMillis() - startTime;
        int totalSeconds = (int) (elapsedMs / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        timeLabel.setText(String.format("Time: %02d:%02d", minutes, seconds));
    }

    /**
     * وقتی مجدداً می‌خواهیم شمارش را از صفر آغاز کنیم (مثلاً Restart یا Back)،
     * همه‌چیز را ریست می‌کنیم.
     */
    public void resetClock() {
        clockTimer.stop();
        startTime = System.currentTimeMillis(); // از الان صفر است
        elapsedBeforePause = 0;                 // تاخیرِ قبلی را دور می‌ریزیم
        timeLabel.setText("Time: 00:00");
        clockTimer.start();
    }

    public int getPanelWidth() {
        return panelWidth;
    }

    public JButton getBackButton() {
        return backBtn;
    }

    /**
     * در صورتی که لازم باشد از بیرون حالت دکمه‌ی Pause/Resume را حتماً روی "Pause"
     * قرار دهیم (مثلاً وقتی Back می‌زنیم)، این متد را صدا می‌زنیم.
     */
    public void forcePauseButton() {
        pauseResumeBtn.setText("Pause");
    }
}
