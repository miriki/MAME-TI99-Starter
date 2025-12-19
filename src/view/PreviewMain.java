package view; //myapp.preview;   // oder einfach dein bestehendes Package

import javax.swing.SwingUtilities;

// import view.MainAppFrame;

public class PreviewMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainAppFrame frame = new MainAppFrame();
            frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
