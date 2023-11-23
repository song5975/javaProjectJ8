package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.SHA256;

public class UserLoginActionCommand implements UserInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String encoding = "UTF-8";
		
		String mid = request.getParameter("userID")==null ? "" : request.getParameter("userID");
		String pwd = request.getParameter("userPassword")==null ? "" : request.getParameter("userPassword");
		
		UserDAO dao = new UserDAO();
		int res = dao.login(mid, pwd);
		
		if (res == 1) {
			HttpSession session = request.getSession();
			session.setAttribute("sUserID", mid);
			request.setAttribute("msg", "로그인 성공");
		    request.setAttribute("url", "index.jsp");
		}
		else {
			request.setAttribute("msg", "로그인 실패");
			request.setAttribute("url", "userLogin.us");
		}
	}

}
