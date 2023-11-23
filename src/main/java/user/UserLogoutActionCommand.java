package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserLogoutActionCommand implements UserInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 현재 세션을 가져옴
		HttpSession session = request.getSession(false);

		// 세션이 존재하면 세션을 무효화(삭제)
		if (session != null) {
			session.invalidate();
			
		}
		
	}
}
