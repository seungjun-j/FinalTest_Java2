import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * @author sj021 (정승준, 2021011906)
 * started date : 2024.11.17
 */
public class First extends JFrame {
    private final HashMap<String, String> userDatabase = new HashMap<>(); // 사용자 정보 저장

    /**
     * @changelog
     * <li>2024-12-09 : 실시간 년,월,일,시,분,초 출력 기능 추가 (SeungJun)</li>
     * <li>2024-12-21 : 메뉴바 생성 및 추가(SeungJun)</li>
     * <li>2024-12-22 : 요구사항에 맞춘 UI 구현(SeungJun)</li>
     * <li>2024-12-23 : 로그인 창 및 회원가입 기능 추가<hashmap을 사용하여 입출력기능 사용>(SeungJun)</li>
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
        JMenuItem emergencyNotice = new JMenuItem("공지사항 확인");
        noticeMenu.add(emergencyNotice);
        menuBar.add(noticeMenu);

        // 좌석 예약 메뉴
        JMenu reserveMenu = new JMenu("좌석 예약");
        JMenuItem seatReserve = new JMenuItem("좌석 예약하기");
        reserveMenu.add(seatReserve);
        menuBar.add(reserveMenu);

        // 로그인 메뉴
        JMenu loginMenu = new JMenu("Log in");
        JMenuItem loginItem = new JMenuItem("로그인");
        JMenuItem signUpItem = new JMenuItem("회원가입");
        loginMenu.add(loginItem);
        loginMenu.add(signUpItem);
        menuBar.add(loginMenu);

        setJMenuBar(menuBar);

        // 로그인 클릭 시 동작 정의
        loginItem.addActionListener(e -> showLoginDialog());
        signUpItem.addActionListener(e -> showSignUpDialog());

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

    // 로그인 창을 표시하는 메서드
    private void showLoginDialog() {
        JDialog loginDialog = new JDialog(this, "로그인", true);
        loginDialog.setSize(300, 200);
        loginDialog.setLayout(new GridLayout(3, 2, 10, 10));
        loginDialog.setLocationRelativeTo(this);

        // 입력 필드
        JLabel usernameLabel = new JLabel("아이디:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        // 버튼
        JButton loginButton = new JButton("로그인");
        JButton cancelButton = new JButton("취소");

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.containsKey(username) && userDatabase.get(username).equals(password)) {
                JOptionPane.showMessageDialog(loginDialog, "로그인 성공!", "로그인", JOptionPane.INFORMATION_MESSAGE);
                loginDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(loginDialog, "회원정보가 없습니다. 회원가입을 진행해주세요.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> loginDialog.dispose());

        // 구성 요소 추가
        loginDialog.add(usernameLabel);
        loginDialog.add(usernameField);
        loginDialog.add(passwordLabel);
        loginDialog.add(passwordField);
        loginDialog.add(loginButton);
        loginDialog.add(cancelButton);

        loginDialog.setVisible(true);
    }

    // 회원가입 창을 표시하는 메서드
    private void showSignUpDialog() {
        JDialog signUpDialog = new JDialog(this, "회원가입", true);
        signUpDialog.setSize(300, 200);
        signUpDialog.setLayout(new GridLayout(3, 2, 10, 10));
        signUpDialog.setLocationRelativeTo(this);

        // 입력 필드
        JLabel usernameLabel = new JLabel("아이디:");
        JTextField usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("비밀번호:");
        JPasswordField passwordField = new JPasswordField();

        // 버튼
        JButton signUpButton = new JButton("회원가입");
        JButton cancelButton = new JButton("취소");

        signUpButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (userDatabase.containsKey(username)) {
                JOptionPane.showMessageDialog(signUpDialog, "이미 존재하는 아이디입니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
            } else {
                userDatabase.put(username, password);
                JOptionPane.showMessageDialog(signUpDialog, "회원가입 성공!", "회원가입", JOptionPane.INFORMATION_MESSAGE);
                signUpDialog.dispose();
            }
        });

        cancelButton.addActionListener(e -> signUpDialog.dispose());

        // 구성 요소 추가
        signUpDialog.add(usernameLabel);
        signUpDialog.add(usernameField);
        signUpDialog.add(passwordLabel);
        signUpDialog.add(passwordField);
        signUpDialog.add(signUpButton);
        signUpDialog.add(cancelButton);

        signUpDialog.setVisible(true);
    }

    public static void main(String[] args) {
        new First();
    }
}
