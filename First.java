import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author sj021 (정승준, 2021011906)
 * started date : 2024.11.17
 */
public class First extends JFrame {
    private final HashMap<String, String> userDatabase = new HashMap<>(); // 사용자 정보 저장
    private final ArrayList<String[]> busSchedule = new ArrayList<>(); // CSV 데이터 저장
    private JLabel remainingTimeLabel; // 남은 시간 표시
    private JLabel waitingCountLabel; // 대기 인원 표시
    private int waitingCount = 0;
    private DefaultTableModel tableModel; // 테이블 모델

    /**
     * @changelog
     * <li>2024-12-09 : 실시간 년,월,일,시,분,초 출력 기능 추가 (SeungJun)</li>
     * <li>2024-12-21 : 메뉴바 생성 및 추가(SeungJun)</li>
     * <li>2024-12-22 : 요구사항에 맞춘 UI 구현(SeungJun)</li>
     * <li>2024-12-23 : 로그인 창 및 회원가입 기능 추가<hashmap을 사용하여 입출력기능 사용>(SeungJun)</li>
     * <li>2024-12-24 : 좌석 예약 및 대기 인원 관리 기능 추가 (SeungJun)</li>
     */
    public First() {
        setTitle("청주 대학교 셔틀 버스 관리 시스템");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadBusSchedule(); // CSV 파일 로드

        // 메뉴바 생성
        JMenuBar menuBar = new JMenuBar();

        // 공지사항 메뉴
        JMenu noticeMenu = new JMenu("공지사항");
        JMenuItem emergencyNotice = new JMenuItem("공지사항 확인");
        noticeMenu.add(emergencyNotice);
        menuBar.add(noticeMenu);

        // 로그인 메뉴
        JMenu loginMenu = new JMenu("Log in");
        JMenuItem loginItem = new JMenuItem("로그인");
        JMenuItem signUpItem = new JMenuItem("회원가입");
        loginMenu.add(loginItem);
        loginMenu.add(signUpItem);
        menuBar.add(loginMenu);

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
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel total = new JPanel(new GridLayout(0, 4, 5, 5));
        String[] busStop = {"8", "9", "10"};
        JComboBox<String> stopBox = new JComboBox<>(busStop);
        total.add(new JLabel("시간 선택:"));
        total.add(stopBox);

        remainingTimeLabel = new JLabel("남은 시간: ");
        remainingTimeLabel.setForeground(Color.RED);
        total.add(remainingTimeLabel);

        waitingCountLabel = new JLabel("대기 인원: 0명");
        total.add(waitingCountLabel);

        centerPanel.add(total, BorderLayout.NORTH);

        // 테이블 생성
        String[] columnNames = {"시간", "분", "설정", "대기 인원"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        stopBox.addActionListener(e -> loadTableData((String) stopBox.getSelectedItem()));
        add(centerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private void loadBusSchedule() {
        try (BufferedReader br = new BufferedReader(new FileReader("/mnt/data/통합 문서1.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                busSchedule.add(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadTableData(String selectedHour) {
        tableModel.setRowCount(0);
        for (String[] schedule : busSchedule) {
            if (schedule[0].equals(selectedHour)) {
                tableModel.addRow(new Object[]{schedule[0], schedule[1], "알림 설정", waitingCount});
            }
        }
    }

    public static void main(String[] args) {
        new First();
    }
}
