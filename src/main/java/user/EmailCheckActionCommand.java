package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EmailCheckActionCommand implements UserInterface {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String code = request.getParameter("code");
        UserDAO dao = new UserDAO();
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("sUserID");
        String ramdomNumber = (String) session.getAttribute("sRandomNumber");

        if (userID == null) {
            request.setAttribute("msg", "회원ID가 정확하지 않습니다. 다시 로그인 해주세요.");
            request.setAttribute("url", "userLogin.jsp");
        }

        boolean rightCode = (code.equals(ramdomNumber));

        if (rightCode) {
        	boolean res = dao.setUserEmailChecked(userID);
        	if(res) {
        		request.setAttribute("msg", "이메일 인증에 성공하였습니다.");
        		request.setAttribute("url", "userLogin.us");
        	}
        	else {
                request.setAttribute("msg", "데이터베이스 오류");
                request.setAttribute("url", "userJoin.us");
        	}
        } 
        else {
            request.setAttribute("msg", "이메일 인증에 실패하였습니다. 다시 시도 해주세요.");
            request.setAttribute("url", "emailCheck.us");
        }
    }
    
}
