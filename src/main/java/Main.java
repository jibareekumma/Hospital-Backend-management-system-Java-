

import db.DatabaseConnection;
import ui.MainFrame;

public class Main {

    public static void main(String[] args) {
        DatabaseConnection.initialize();
        MainFrame frame = new MainFrame();
        frame.setVisible(true);
    }
}