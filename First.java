import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
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
     *     <li>2024-12-09 : 실시간 년,월,일,시,분,초 출력 기능 추가 (SeungJun)</li>
     *     <li>2024-12-21 : 메뉴바 생성 및 추가(SeungJun)</li>
     *     <li>2024-12-22 : 요구사항에 맞춘 UI 구현(SeungJun)</li>
     * </ul>
     */
    public First() {
        setTitle("청주 대학교 셔틀 버스 관리 시스템");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 메뉴바 생성
        JMenuBar menuBar = new JMenuBar();

        // 공지사항 메뉴
        JMenu noticeMenu = new JMenu("공지사항");
        JMenuItem emergencyNotice = new JMenuItem("긴급 공지사항 확인");
        noticeMenu.add(emergencyNotice);
        menuBar.add(noticeMenu);

        // 좌석 예약 메뉴
        JMenu reserveMenu = new JMenu("좌석 예약");
        JMenuItem seatReserve = new JMenuItem("좌석 예약하기");
        reserveMenu.add(seatReserve);
        menuBar.add(reserveMenu);

        setJMenuBar(menuBar);

        // 상단 패널: 날짜와 시간
        JPanel topPanel = new JPanel();
        JLabel dateTimeLabel = new JLabel();
        topPanel.add(dateTimeLabel);
        add(topPanel, BorderLayout.NORTH);

        // 날짜와 시간 업데이트 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        });
        timer.start();

        // 중앙 패널: 셔틀버스 정보 및 대기 인원 공유
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        // 셔틀버스 운행 시간
        JButton scheduleButton = new JButton("셔틀버스 운행 시간 확인 및 알람 설정");
        centerPanel.add(scheduleButton);

        // 대기 인원 공유
        JButton waitingShareButton = new JButton("버스 대기 인원 공유");
        centerPanel.add(waitingShareButton);

        // 버스 도착 여부 확인 (채팅 기능 활용)
        JButton busArrivalCheckButton = new JButton("버스 도착 여부 확인 (채팅 기능)");
        centerPanel.add(busArrivalCheckButton);

        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널: 긴급 공지
        JPanel bottomPanel = new JPanel();
        JButton emergencyButton = new JButton("긴급 공지사항 보기");
        bottomPanel.add(emergencyButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new First();
    }
}
