package org.example.view;

import org.example.controller.GameController;
import org.example.model.Board;
import org.example.model.Tetromino;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private final GameController ctrl;
    private final int cell;


    public GamePanel(GameController ctrl, int cellSize) {

        this.ctrl  = ctrl;
        this.cell  = cellSize;
        setBackground(Color.BLACK);

    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
                ctrl.getBoard().getCols() * cell,
                ctrl.getBoard().getRows() * cell
        );
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1) Draw the blocks already in the board:
        drawPlacedBlocks(g);

        // 2) Draw the falling tetromino:
        drawCurrentPiece(g);

        // 3) Overlay a crisp, single‐color grid:
        drawGrid(g);

        // 4) (optional) Game Over banner:
        if (ctrl.isGameOver()) {
            drawGameOver(g);
        }

    }

    private void drawPlacedBlocks(Graphics g) {
        Board board = ctrl.getBoard();
        g.setColor(Color.DARK_GRAY.brighter());
        for (int y = 0; y < board.getRows(); y++) {
            for (int x = 0; x < board.getCols(); x++) {
                if (board.getGrid()[y][x]) {
                    // fill exactly one cell-by-cell square
                    g.fillRect(x * cell, y * cell, cell, cell);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        Tetromino t = ctrl.getCurrent();
        if (t == null) return;

        // pick a color per shape
        Color col = switch (t.getType()) {
            case I -> Color.CYAN;
            case J -> Color.BLUE;
            case L -> Color.ORANGE;
            case O -> Color.YELLOW;
            case S -> Color.GREEN;
            case T -> Color.MAGENTA;
            case Z -> Color.RED;
            default -> Color.WHITE;
        };
        g.setColor(col);

        int[][] mat = t.getMatrix();
        var p = t.getPosition();
        for (int r = 0; r < mat.length; r++) {
            for (int c = 0; c < mat[r].length; c++) {
                if (mat[r][c] == 1) {
                    // fill each cell individually
                    g.fillRect(
                            (p.getX() + c) * cell,
                            (p.getY() + r) * cell,
                            cell, cell
                    );
                }
            }
        }
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.GRAY);  // one solid, visible color
        int R = ctrl.getBoard().getRows();
        int C = ctrl.getBoard().getCols();
        // vertical lines
        for (int x = 0; x <= C; x++) {
            g.drawLine(x * cell, 0, x * cell, R * cell);
        }
        // horizontal lines
        for (int y = 0; y <= R; y++) {
            g.drawLine(0, y * cell, C * cell, y * cell);
        }
    }

    private void drawGameOver(Graphics g) {
        String msg = "GAME OVER";
        g.setFont(new Font("Consolas", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        int w = fm.stringWidth(msg);
        g.setColor(new Color(255, 0, 0, 180));
        g.drawString(msg, (getWidth() - w) / 2, getHeight() / 2);
    }

}
