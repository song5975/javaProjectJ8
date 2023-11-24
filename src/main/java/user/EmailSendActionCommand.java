package user;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmailSendActionCommand implements UserInterface {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String encoding = "UTF-8";

        UserDAO dao = new UserDAO();
        String userID = null;

        // 세션에서 사용자 정보 가져오기
        HttpSession session = request.getSession();
        if (session.getAttribute("sUserID") != null) {
            userID = (String) session.getAttribute("sUserID");
            
        }

        // 사용자 정보가 없을 경우 로그인 페이지로 이동
        if (userID == null) {
            request.setAttribute("msg", "로그인을 해주세요.");
            request.setAttribute("url", "userLogin.us");
            request.getRequestDispatcher("/WEB-INF/user/userLogin.jsp").forward(request, response);
            return;
        }

        // 이메일 체크
        boolean emailChecked = dao.getUserEmailChecked(userID);

        // 이미 이메일이 인증된 경우
        if (emailChecked) {
            request.setAttribute("msg", "이미 인증된 회원입니다.");
            request.setAttribute("url", "index.jsp");
            request.getRequestDispatcher("/WEB-INF/include/message.jsp").forward(request, response);
            return;
        }

        String host = "http://192.168.50.68:9090/javaProjectJ8/";
        String from = "badugi5655@gmail.com";
        String to = dao.getUserEmail(userID);
        String subject = "강의평가를 위한 이메일 인증 메일입니다.";
        
        // 6자리 난수 생성
        String randomNumber = String.format("%06d", new java.util.Random().nextInt(1000000));
        
        // 세션에 난수 저장
        session.setAttribute("sRandomNumber", randomNumber);
        
        String content = "다음 코드를 복사하여 웹페이지에서 인증을 진행하세요 ==> " + randomNumber;
        
        Properties p = new Properties();
        p.put("mail.smtp.user", from);
        p.put("mail.smtp.host", "smtp.gmail.com");
        p.put("mail.smtp.port", "587");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.debug", "true");
        p.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            Authenticator auth = new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(from, "daplpepeofkfamie"); // 생성한 앱 비밀번호로 대체
                }
            };
            Session ses = Session.getInstance(p, auth);
            ses.setDebug(true);
            MimeMessage msg = new MimeMessage(ses);
            msg.setSubject(subject);
            Address fromAddr = new InternetAddress(from);
            msg.setFrom(fromAddr);
            Address toAddr = new InternetAddress(to);
            msg.addRecipient(Message.RecipientType.TO, toAddr);
            msg.setContent(content, "text/html; charset=UTF-8");
            Transport.send(msg);

            // 성공적으로 이메일이 발송되면 사용자가 코드를 입력할 수 있는 화면으로 이동
            request.setAttribute("msg", "이메일이 성공적으로 발송되었습니다. 이메일 인증을 해주세요.");
            request.setAttribute("url", "emailCheck.us");
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 alert 처리 후 사용자를 이전 화면으로 이동
            request.setAttribute("msg", "이메일 발송 중 오류가 발생했습니다.");
            request.setAttribute("url", "이전 페이지 URL"); // 이전 페이지의 URL로 수정
        }
    }
}
