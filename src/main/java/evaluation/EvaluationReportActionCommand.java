package evaluation;

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

import user.UserDAO;

public class EvaluationReportActionCommand implements EvaluationInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String encoding = "UTF-8";
		String userID = null;

		// 사용자가 신고한 신고 제목과 내용
		String reportTitle = request.getParameter("reportTitle") == null ? "" : request.getParameter("reportTitle");
		String reportContent = request.getParameter("reportContent") == null ? ""
				: request.getParameter("reportContent");

		// 세션에서 사용자 정보 가져오기
		HttpSession session = request.getSession();
		if (session.getAttribute("sUserID") != null) {
			userID = (String) session.getAttribute("sUserID");
		}

		// 사용자 정보가 없을 경우 로그인 페이지로 이동(이걸 나중에 UUID 토큰을 활용한 CSRF 공격 방어로 교체)
		if (userID == null) {
			request.setAttribute("msg", "로그인을 해주세요.");
			request.setAttribute("url", "userLogin.us");
			request.getRequestDispatcher("/WEB-INF/user/userLogin.jsp").forward(request, response);
			return;
		}

		UserDAO dao = new UserDAO();

		// 이메일 체크
		boolean emailChecked = dao.getUserEmailChecked(userID);

		// 이미 이메일이 인증된 경우
		if (emailChecked) {
			String host = "http://192.168.50.68:9090/javaProjectJ8/";
			String from = "badugi5655@gmail.com";
			String to = dao.getUserEmail(userID);
			String subject = "강의평가 사이트에서 접수된 신고입니다.";
			String content = "신고자 : " + userID + "<br>제목 : " + reportTitle + "<br>내용 : " + reportContent;

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

				// 성공적으로 신고 이메일이 발송되면 index.jsp로 이동
				request.setAttribute("msg", "관리자에게 신고가 정상적으로 발송되었습니다.");
				request.setAttribute("url", "index.jsp");
			} catch (Exception e) {
				e.printStackTrace();
				// 오류 발생 시 메시지 처리 후 사용자를 이전 화면으로 이동
				request.setAttribute("msg", "이메일 발송 중 오류가 발생했습니다.");
				request.setAttribute("url", "index.jsp");
			}
		} else {
			request.setAttribute("msg", "신고하기는 이메일 인증이 완료된 회원만 사용 가능합니다.");
			request.setAttribute("url", "index.jsp");
		}
	}
}
