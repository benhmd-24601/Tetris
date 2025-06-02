    package org.example.view;

    import org.example.config.ConfigManager;
    import org.example.config.GameConfig;
    import org.example.config.Resolution;
    import org.example.controller.GameController;

    import javax.swing.*;
    import java.awt.*;
    import java.awt.event.KeyAdapter;
    import java.awt.event.KeyEvent;

    public class TetrisView extends JFrame {
        private final CardLayout cards = new CardLayout();
        private final JPanel root = new JPanel(cards);

        private GamePanel game;
        private SidePanel side;

        public TetrisView() {
            setTitle("Clean Tetris");
            setDefaultCloseOperation(EXIT_ON_CLOSE);

            // ۱) بارگذاری کانفیگ و انتخاب یک رزولوشن (ایندکس 0)
            GameConfig cfg = ConfigManager.getInstance().config();
            Resolution res = cfg.getResolutions().get(0);

            int rows = cfg.getRows();
            int cols = cfg.getCols();
            int maxW = res.getWidth();
            int maxH = res.getHeight();
            int sideW = res.getSidePanelWidth();

            // ۲) محاسبۀ cell
            int availW = maxW - sideW;
            int cell = Math.min(availW / cols, maxH / rows);
            if (cell < 10) cell = 10;

            // ۳) ایجاد Controller
            GameController ctrl = new GameController(() -> {
                game.repaint();
                side.refresh();
            });

            // ۴) ساخت GamePanel و SidePanel
            game = new GamePanel(ctrl, cell);
            side = new SidePanel(ctrl);

            // ۵) در یک JPanel با BorderLayout کنار هم قرارشان بده
            JPanel gameUI = new JPanel(new BorderLayout());
            gameUI.add(game, BorderLayout.CENTER);
            gameUI.add(side, BorderLayout.EAST);

            // ۶) کارت‌های کالد-اوت
            StartScreen start = new StartScreen();
            SettingsScreen settings = new SettingsScreen(
                    () -> {
                        dispose();
                        SwingUtilities.invokeLater(TetrisView::new);
                    },
                    () -> cards.show(root, "start")
            );

            root.add(start,    "start");
            root.add(settings, "settings");
            root.add(gameUI,   "game");

            setContentPane(root);

            // در سیم‌کشیِ Start:
            start.getStartButton().addActionListener(e -> {
                ctrl.start();
                side.resetClock();
                side.refresh();
                cards.show(root, "game");
                game.requestFocusInWindow();
                pack();
                setLocationRelativeTo(null);
            });

// در سیم‌کشیِ Back:
            side.getBackButton().addActionListener(e -> {
                ctrl.start();            // ریست مدل
                side.resetClock();       // ریست ساعت View
                side.refresh();          // ریست امتیاز View
                side.forcePauseButton(); // دکمه را روی "Pause" می‌گذارد
                cards.show(root, "start");
                pack();
                setLocationRelativeTo(null);
            });

            start.getSettingsButton().addActionListener(e ->
                    cards.show(root, "settings")
            );

            start.getExitButton().addActionListener(e -> {
                dispose();

            });


            // ۸) Key bindings برای GamePanel
            game.setFocusable(true);
            game.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:  ctrl.moveLeft();  break;
                        case KeyEvent.VK_RIGHT: ctrl.moveRight(); break;
                        case KeyEvent.VK_UP:    ctrl.rotate();    break;
                        case KeyEvent.VK_DOWN : ctrl.softDrop();  break;
                    }
                }
            });

            // ۹) نمایش اولیه: کریدیت صفحهٔ استارت، سپس pack()
            cards.show(root, "start");
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
            setResizable(false);
        }
    }
