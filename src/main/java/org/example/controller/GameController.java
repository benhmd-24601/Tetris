package org.example.controller;

import org.example.config.ConfigManager;
import org.example.model.Board;
import org.example.model.Tetromino;

import javax.swing.*;
import java.awt.event.ActionEvent;


public class GameController {
    private final Board board;
    private Tetromino current;
    private final Timer timer;
    private final Runnable onTickCallback;

    private boolean paused;
    private int score;

    // برای محاسبۀ سقوط:
    private long lastDropTime;     // timestamp آخرین سقوط (به میلی‌ثانیه)
    private int dropInterval;      // هر چند ms یک بار سقوط کنیم (بنا بر speed)

    public GameController(Runnable onTickCallback) {
        this.board = new Board();
        this.onTickCallback = onTickCallback;
        this.score = 0;
        this.paused = true;  // تا وقتی start نزده، نباید تیک بزنیم

        // ۱) خواندن سرعت از کانفیگ (حالا به‌عنوان سقوط‌-در-ثانیه)
        int speed = ConfigManager.getInstance().config().getSpeed();
        // اگر سرعتِ 0 یا منفی باشد:
        if (speed <= 0) speed = 1;

        // ۲) محاسبۀ DropInterval بر حسب میلی‌ثانیه
        //    یعنی مثلاً اگر speed=5 → dropInterval = 1000/5 = 200ms
        this.dropInterval = 1000 / speed;

        // ۳) یک تایمر با تأخیر ثابت، مثلاً 50ms (تقریباً 20FPS)
        int refreshRate = 50; // می‌توان 16ms (~60FPS) هم گذاشت، ولی 50 کافی است
        this.timer = new Timer(refreshRate, this::onTick);

        this.lastDropTime = System.currentTimeMillis();
    }

    public void start() {
        score = 0;
        clearBoard();
        spawnPiece();
        paused = false;
        lastDropTime = System.currentTimeMillis(); // شروعِ شمارش سقوط
        timer.start();
    }

    public void pauseToggle() {
        paused = !paused;
        if (paused) timer.stop();
        else {
            // وقتی از pause درمی‌آییم، تاریخچه را reset کنیم تا بلافاصله نیفتند
            lastDropTime = System.currentTimeMillis();
            timer.start();
        }
    }

    private void onTick(ActionEvent e) {
        if (paused) return;

        long now = System.currentTimeMillis();
        // اگر از آخرین سقوطِ قطعه، به اندازه dropInterval یا بیش‌تر گذشته:
        if (now - lastDropTime >= dropInterval) {
            // ۱) سقوط قطعه یک خانه
            current.moveDown();
            if (collides()) {
                current.moveUp();
                board.place(current);
                int lines = board.clearFullLines();
                score += switch (lines) {
                    case 1 -> 100;
                    case 2 -> 300;
                    case 3 -> 500;
                    case 4 -> 800;
                    default -> 0;
                };
                spawnPiece();
                if (isGameOver()) {
                    timer.stop();
                }
            }
            // ۲) به‌روزرسانی timestamp آخرین سقوط
            lastDropTime = now;
        }

        // ۳) repaint کلی صفحه (سریع بودنِ Refresh Rate را تضمین می‌کند)
        onTickCallback.run();
    }

    // بقیهٔ متدها بدون تغییرند:

    private boolean collides() {
        var m = current.getMatrix();
        var p = current.getPosition();
        for (int r = 0; r < m.length; r++) {
            for (int c = 0; c < m[r].length; c++) {
                if (m[r][c] == 1 &&
                        board.isOccupied(p.getX() + c, p.getY() + r)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isGameOver() {
        return collides();
    }

    private void clearBoard() {
        var grid = board.getGrid();
        for (int y = 0; y < grid.length; y++)
            for (int x = 0; x < grid[y].length; x++)
                grid[y][x] = false;
    }

    private void spawnPiece() {
        current = Tetromino.spawnRandom(board.getCols());
    }

    // UI calls:
    public void moveLeft() {
        current.moveLeft();
        if (collides()) current.moveRight();
    }
    public void moveRight() {
        current.moveRight();
        if (collides()) current.moveLeft();
    }
    public void rotate() {
        if (!ConfigManager.getInstance().config().isAllowRotation()) return;
        current.rotate();
        if (collides()) {
            // rollback (سه بار rotate معکوس)
            for (int i = 0; i < 3; i++) current.rotate();
        }
    }
    public void softDrop() {
        if (paused) return; // اگر بازی Paused باشه، هیچ کاری نکن

        // ۱) یک سلول به پایین حرکت بده
        current.moveDown();

        // ۲) اگر برخورد کرد، برگرد بالا و قطعه را جای‌گذاری کن
        if (collides()) {
            current.moveUp();
            board.place(current);

            int lines = board.clearFullLines();
            score += switch (lines) {
                case 1 -> 100;
                case 2 -> 300;
                case 3 -> 500;
                case 4 -> 800;
                default -> 0;
            };

            spawnPiece();
            if (isGameOver()) {
                timer.stop();
            }
        }

        // ۳) در هر صورت، View را رفرش کن
        onTickCallback.run();
    }

    // queries from view:
    public Board getBoard()       { return board; }
    public Tetromino getCurrent() { return current; }
    public int getScore()         { return score; }
}
