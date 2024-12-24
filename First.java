import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 청주대학교 셔틀버스 관리 시스템
 *
 * 주요 기능:
 * - 셔틀버스 시간표 관리
 * - 로그인 및 회원가입 기능
 * - 공지사항 확인 기능
 * - 남은 시간 및 대기 인원 관리
 * - CSV 파일을 이용한 데이터 로드
 *
 * @author sj021
 * @version 2024.12.24
 */
public class First extends JFrame {
    // 사용자 계정을 저장하는 HashMap: 아이디 -> 비밀번호
    private final HashMap<String, String> userDatabase = new HashMap<>();

    // 버스 스케줄 데이터를 저장할 HashMap: 시간 -> ArrayList(분)
    private final HashMap<String, ArrayList<String>> busScheduleMap = new HashMap<>();

    private JLabel remainingTimeLabel; // 남은 시간을 표시하는 라벨
    private JLabel waitingCountLabel; // 대기 인원 수를 표시하는 라벨
    private int waitingCount = 0; // 대기 인원 수
    private DefaultTableModel tableModel; // JTable의 데이터 모델

    /**
     * 프로그램 초기화 및 GUI 구성
     */
    public First() {
        setTitle("청주 대학교 셔틀 버스 관리 시스템");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        loadBusSchedule(); // CSV 파일 로드 및 데이터 초기화

        // 메뉴바 생성
        JMenuBar menuBar = new JMenuBar();

        // 공지사항 메뉴
        JMenu noticeMenu = new JMenu("공지사항");
        JMenuItem emergencyNotice = new JMenuItem("공지사항 확인");
        emergencyNotice.addActionListener(e -> openNoticeURL("https://www.cju.ac.kr/www/selectBbsNttList.do?bbsNo=881&key=4577"));
        noticeMenu.add(emergencyNotice);
        menuBar.add(noticeMenu);

        // 로그인 메뉴
        JMenu loginMenu = new JMenu("Log in");
        JMenuItem loginItem = new JMenuItem("로그인");
        loginItem.addActionListener(e -> showLoginDialog());
        JMenuItem signUpItem = new JMenuItem("회원가입");
        signUpItem.addActionListener(e -> showSignUpDialog());
        loginMenu.add(loginItem);
        loginMenu.add(signUpItem);
        menuBar.add(loginMenu);

        setJMenuBar(menuBar);

        // 상단 패널: 현재 날짜와 시간
        JPanel topPanel = new JPanel();
        JLabel dateTimeLabel = new JLabel();
        topPanel.add(dateTimeLabel);
        add(topPanel, BorderLayout.NORTH);

        // 날짜와 시간 업데이트 (1초마다 갱신)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초");
        Timer timer = new Timer(1000, e -> {
            LocalDateTime now = LocalDateTime.now();
            dateTimeLabel.setText(now.format(formatter));
        });
        timer.start();

        // 중앙 패널: 셔틀버스 정보 및 대기 인원 관리
        JPanel centerPanel = new JPanel(new BorderLayout());

        JPanel total = new JPanel(new GridLayout(0, 4, 5, 5));
        String[] busStop = {"8시", "9시", "10시", "11시", "12시", "13시", "14시", "15시", "16시", "17시", "18시"};
        JComboBox<String> stopBox = new JComboBox<>(busStop);
        total.add(new JLabel("시간 선택:"));
        total.add(stopBox);

        remainingTimeLabel = new JLabel("남은 시간: ");
        remainingTimeLabel.setForeground(Color.RED);
        total.add(remainingTimeLabel);

        waitingCountLabel = new JLabel("대기 인원: 0명");
        total.add(waitingCountLabel);

        centerPanel.add(total, BorderLayout.NORTH);

        // 테이블 생성 및 추가
        String[] columnNames = {"시간", "설정", "대기 인원"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        stopBox.addActionListener(e -> loadTableData((String) stopBox.getSelectedItem()));
        add(centerPanel, BorderLayout.CENTER);

        // 하단 패널: 공지사항 버튼
        JPanel bottomPanel = new JPanel();
        JButton noticeButton = new JButton("공지사항 보기");
        noticeButton.addActionListener(e -> openNoticeURL("https://www.cju.ac.kr/www/selectBbsNttList.do?bbsNo=881&key=4577"));
        bottomPanel.add(noticeButton);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * CSV 파일을 읽어 HashMap에 데이터를 로드합니다.
     * 키는 시간(예: "8시"), 값은 해당 시간의 분(예: ["10분", "20분"]).
     */
    private void loadBusSchedule() {
        String filePath = "C:\\Users\\sj021\\IdeaProjects\\FinalTest_Java2\\버스 스케줄.csv"; // 파일 경로 설정
        File file = new File(filePath);

        if (!file.exists()) {
            JOptionPane.showMessageDialog(this, "CSV 파일을 찾을 수 없습니다: " + filePath, "파일 오류", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                busScheduleMap.putIfAbsent(data[0], new ArrayList<>());
                busScheduleMap.get(data[0]).add(data[1]);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "CSV 파일 읽기 중 오류가 발생했습니다.", "파일 오류", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * 선택된 시간대의 버스 스케줄을 JTable에 로드합니다.
     *
     * @param selectedHour 사용자가 선택한 시간대 (예: "8시")
     */
    private void loadTableData(String selectedHour) {
        tableModel.setRowCount(0); // 기존 데이터를 초기화
        ArrayList<String> schedules = busScheduleMap.get(selectedHour);
        if (schedules != null) {
            for (String minute : schedules) {
                tableModel.addRow(new Object[]{selectedHour + " " + minute, "알림 설정", waitingCount});
            }
        }
    }

    /**
     * 지정된 URL을 브라우저에서 엽니다.
     *
     * @param url 열고자 하는 URL
     */
    private void openNoticeURL(String url) {
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "URL을 열 수 없습니다: " + url, "오류", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * 로그인 다이얼로그를 표시합니다.
     */
    private void showLoginDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField idField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "로그인", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.containsKey(id) && userDatabase.get(id).equals(password)) {
                JOptionPane.showMessageDialog(this, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "로그인 실패! 아이디 또는 비밀번호를 확인하세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * 회원가입 다이얼로그를 표시합니다.
     */
    private void showSignUpDialog() {
        JPanel panel = new JPanel(new GridLayout(2, 2));
        JTextField idField = new JTextField();
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "회원가입", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String id = idField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.containsKey(id)) {
                JOptionPane.showMessageDialog(this, "이미 존재하는 아이디입니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
            } else {
                userDatabase.put(id, password);
                JOptionPane.showMessageDialog(this, "회원가입 성공!", "회원가입", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * 프로그램 시작점
     *
     * @param args 명령줄 인자 (사용하지 않음)
     */
    public static void main(String[] args) {
        new First();
    }
}
