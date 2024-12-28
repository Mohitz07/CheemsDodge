import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;   //dimensions in pixels
        int boardHeight = 640;

        JFrame frame = new JFrame("Cheems Dodge");
        //frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CheemsDodge cheemsDodge = new CheemsDodge();    //creating instance
        frame.add(cheemsDodge);
        frame.pack();
        cheemsDodge.requestFocus();
        frame.setVisible(true);
    }
}
