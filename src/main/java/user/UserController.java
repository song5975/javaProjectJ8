package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.us")
@SuppressWarnings("serial")
public class UserController extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
		
		UserInterface command = null;
		String viewPage = "/WEB-INF/user";
		
		String com = request.getRequestURI();
		System.out.println(com);
		com = com.substring(com.lastIndexOf("/"),com.lastIndexOf("."));
		
		System.out.println(com);
		
		/*
		HttpSession session = request.getSession();
		int level = session.getAttribute("sLevel")==null ? 999 : (int) session.getAttribute("sLevel");
		*/
		
		// index.jsp로 이동
		if(com.equals("/index")) {
			viewPage = "/include/message.jsp";
		}

		// nav에서 로그인을 누르면 사용자 로그인 페이지로 이동
		if(com.equals("/userLogin")) {
			viewPage += "/userLogin.jsp";
		}

		// userJoin에서 입력 받은 값을 확인하여 가입 성공 혹은 실패
		else if(com.equals("/userRegisterAction")) {
			command = new UserRegisterActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}

		// 사용자 로그인 페이지에서 값을 입력한 뒤 DB의 정보 확인
		if(com.equals("/userLoginAction")) {
			command = new UserLoginActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}
		
		// 로그아웃
		else if(com.equals("/userLogout")) {
			command = new UserLogoutActionCommand();
			command.execute(request, response);
			viewPage += "/userLogout.jsp";
		}
		
		// 회원가입
		else if(com.equals("/userJoin")) {
			viewPage += "/userJoin.jsp";
		}
		
		
		// UserRegisterActionCommand에서 회원 가입 후 이메일 인증
		else if(com.equals("/emailSendAction")) {
			command = new EmailSendActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}
		
		// 이메일 발송 후 사용자가 확인 후에 코드를 확인받는 페이지로 이동
		else if(com.equals("/emailCheck")) {
			viewPage += "/emailCheck.jsp";
		}
		
		
		// 사용자가 인증 이메일을 확인 후 emailCheck.jsp에서 코드를 인증 받은 후
		else if(com.equals("/emailCheckAction")) {
			command = new EmailCheckActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}
		
		
		
		/*
		else if(com.equals("/memberIdCheck")) {
			/* command = new MemberIdCheckCommand();
			command.execute(request, response);
			viewPage += "/memberIdCheck.jsp";
		}
		*/
		
		/*
		else if(com.equals("/memberLoginOk")) {
			 command = new MemberLoginOkCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}
		*/
		
		request.getRequestDispatcher(viewPage).forward(request, response);
	}
}
