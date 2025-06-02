    package org.example;


    import javax.swing.SwingUtilities;
    import org.example.view.TetrisView;

    public class TetrisGame {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(TetrisView::new);
        }
    }