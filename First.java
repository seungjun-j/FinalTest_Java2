import javax.swing.*;

/**
 * @author sj021 (정승준, 2021011906)
 * started date : 2024.11.17
 *
 */
public class First extends JFrame {

    First(){
        setTitle("청주 대학교 셔틀 버스");
        setSize(500,500);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }
    public static void main(String[] args) {
    new First();
    }
}
