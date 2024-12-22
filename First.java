import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author sj021 (정승준, 2021011906)
 * started date : 2024.11.17
 *
 */
public class First extends JFrame {
    /**
     * @changelog
     * <ul>
     *     <li>2024-12-09 : 실시간 년,월,일,시,분,초 출력할 수 있도록 생성 (SeungJun)</li>
     *     <li>2024-12-21 : 메뉴바 생성 및 추가(SeungJun)</li>
     *     <li>2024-12-22 :</li>
     *
     * </ul>
     * @see<a href = "https://hianna.tistory.com/607"></a>
     */
    First(){
        setTitle("청주 대학교 셔틀 버스");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        //메뉴바 만들기
        JMenuBar menuBar = new JMenuBar();
        //메뉴바에 공지사항 추가
        JMenu notice = new JMenu("공지사항");
        menuBar.add(notice);
        //메뉴바에 좌석 예약 추가
        JMenu reserve = new JMenu("좌석 예약");
        menuBar.add(reserve);

        setJMenuBar(menuBar);



        // 패널 생성
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        // 날짜와 시간 표시용 JLabel 생성
        JLabel dateTimeLabel = new JLabel();
        panel.add(dateTimeLabel);

        // JFrame에 패널 추가
        add(panel);

        // 날짜와 시간 포맷 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");

        // Timer로 날짜와 시간 업데이트
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            String dateTime = now.format(formatter);
            dateTimeLabel.setText(dateTime);
        });

        // Timer 시작
        timer.start();
    }
    public static void main(String[] args) {
    new First();
    }
}
