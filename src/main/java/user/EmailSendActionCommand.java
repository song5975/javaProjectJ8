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

        // 세션에서 사용자ID 가져오기
        HttpSession session = request.getSession();
        if (session.getAttribute("sUserID") != null) {
            userID = (String) session.getAttribute("sUserID");
        }

        // 사용자 정보가 없을 경우 로그인 페이지로 이동
        if (userID == null) {
            request.setAttribute("msg", "로그인을 해주세요.");
            request.setAttribute("url", "userLogin.us");
            return;
        }

        // 이메일 체크
        boolean emailChecked = dao.getUserEmailChecked(userID);

        // 이미 이메일이 인증된 경우
        if (emailChecked) {
            request.setAttribute("msg", "이미 인증된 회원입니다.");
            request.setAttribute("url", "index.jsp");
            return;
        }

        // 이메일 발송을 위해 필요한 정보들을 설정
        String from = "badugi5655@gmail.com";
        String to = dao.getUserEmail(userID);
        String subject = "강의평가를 위한 이메일 인증 메일입니다.";
        
        // 6자리 난수 생성(인증번호) / Random 클래스를 사용하여 0 이상 999999 이하의 랜덤한 정수를 생성
        String randomNumber = String.format("%06d", new java.util.Random().nextInt(1000000));
        
        // 세션에 난수 저장
        session.setAttribute("sRandomNumber", randomNumber);
        
        String content = "다음 코드를 복사하여 웹페이지에서 인증을 진행하세요 ==> " + randomNumber;
        
        // 이메일 발송을 위한 JavaMail API 설정 / (SMTP :(Simple Mail Transfer Protocol))
        // Properties 클래스, Java에서 키(key)와 값(value) 쌍으로 이루어진 컬렉션을 관리하기 위한 클래스
        // Properties 클래스는 컬렉션 프레임워크의 일부로, HashTable을 확장하여 만든 클래스
        Properties p = new Properties();
        // SMTP 서버 인증에 사용될 사용자 이름을 설정, 이메일 발송을 위해 사용되는 계정의 이메일 주소(관리자)
        p.put("mail.smtp.user", from);
        // SMTP 서버의 호스트 주소를 설정합니다. Gmail의 SMTP 서버 주소인 "smtp.gmail.com"을 사용
        p.put("mail.smtp.host", "smtp.gmail.com");
        // SMTP 서버의 포트 번호, TLS(Transport Layer Security)을 사용하는 경우 587번 포트
        p.put("mail.smtp.port", "587");
        // TLS 암호화를 사용하여 보안 연결을 활성화할지 여부를 설정
        p.put("mail.smtp.starttls.enable", "true");
        // SMTP 서버에 대한 사용자 인증을 활성화할지 여부를 설정
        p.put("mail.smtp.auth", "true");
        // 디버깅 모드를 활성화할지 여부를 설정
        p.put("mail.smtp.debug", "true");
        // SSL/TLS 프로토콜의 버전을 설정( 이름이 "ssl.protocols"이라고 되어 있지만, 실제로는 SSL 및 TLS 프로토콜 버전 모두를 관리하는 속성)
        p.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        // Properties 객체는 메일 서버 호스트, 포트, 인증 설정 등 메일 전송에 필요한 환경 설정이 담겨있다.
        
        /* 	
         	* SMTP 포트
        	- 587 (TLS를 사용하는 경우)
        	- 465 (SSL을 사용하는 경우)
        	- TLS(Transport Layer Security)와 SSL(Secure Sockets Layer)은 모두 보안 소켓 계층 프로토콜로, 
        	  데이터 통신을 보호하기 위해 사용. (SSL은 초기의 SMTP 포트, SSL의 후속 버전이 TLS)
        	- 현재 TLS를 사용하는 것이 보안 및 호환성 측면에서 권장된다.
        */

        // Authenticator 는 추상 클래스
        try {
            Authenticator auth = new Authenticator() {
            	// getPasswordAuthentication 메서드는 Authenticator에 속한 추상 메서드
            	@Override
                protected PasswordAuthentication getPasswordAuthentication() {
                	// 사용자가 이 메서드를 구현하여 사용자의 이메일 계정 정보(PasswordAuthentication 객체)를 반환
                    return new PasswordAuthentication(from, "otebgwwroaykwdve"); // 생성한 앱 비밀번호
                }
            };
            
            /*
        	Authenticator 클래스의 getPasswordAuthentication 메서드에서 반환되는 
        	PasswordAuthentication 객체에는 사용자의 이메일 계정과 해당 이메일 계정에 대한 앱 비밀번호가 포함
        	Authenticator 객체는 사용자의 이메일 주소와 비밀번호 등 메일 서버에 인증 정보를 제공하는 역할
             */
            
            
            // 이 세션은 이메일을 발송하기 위한 전반적인 환경 설정을 가지고 있음
            Session ses = Session.getInstance(p, auth);
            // 세션의 디버그 모드를 활성화
            ses.setDebug(true);
            
            /*
            Properties 객체 p와 Authenticator 객체 auth를 사용하여 세션을 생성
            다른 변수를 사용해도 가능하지만 Session을 생성하는 이유는 Session은 메일 발송을 위한 전반적인 환경 설정을 제공하는데, 
            이는 메일 서버와의 통신, 디버깅 옵션, 인증 등을 다루고 있어서 이러한 기능을 활용하기 위해 세션으로 생성합니다.
             */
            
            
            // ses 세션에 담은 정보 기반으로 MimeMessage 객체를 생성
            
            /*
            MimeMessage는 JavaMail API에서 제공하는 클래스로 메일의 주제, 송신자, 수신자 등의 정보를 포함한 헤더를 관리하며,
            텍스트, HTML, 그림, 첨부 파일 등 다양한 형태의 이메일을 처리할 때 사용한다.
             
             */
            
            // MimeMessage는 MIME(Multipurpose Internet Mail Extensions) 형식의 이메일 메시지를 나타냅니다.
            MimeMessage msg = new MimeMessage(ses);
            // 이메일의 제목을 설정
            msg.setSubject(subject);
            // 발신자의 이메일 주소를 설정(관리자)
            Address fromAddr = new InternetAddress(from);
            // 메시지의 발신자를 설정
            msg.setFrom(fromAddr);
            // 수신자의 이메일 주소를 설정(사용자)
            Address toAddr = new InternetAddress(to);
            // 메시지에 수신자를 추가
            msg.addRecipient(Message.RecipientType.TO, toAddr);
            // HTML 형식의 내용을 사용하고, 문자 인코딩은 UTF-8로 지정
            msg.setContent(content, "text/html; charset=UTF-8");
            // 설정된 메시지를 이메일 서버를 통해 전송
            Transport.send(msg);

            // 성공적으로 이메일이 발송되면 사용자가 코드를 입력할 수 있는 화면으로 이동
            request.setAttribute("msg", "이메일이 성공적으로 발송되었습니다. 이메일 인증을 해주세요.");
            request.setAttribute("url", "emailCheck.us");
        } catch (Exception e) {
            e.printStackTrace();
            // 오류 발생 시 alert 처리 후 사용자를 이전 화면으로 이동
            request.setAttribute("msg", "이메일 발송 중 오류가 발생했습니다.");
            request.setAttribute("url", "userJoin.us");
        }
        
    }
}
