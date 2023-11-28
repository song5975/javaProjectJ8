package user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import util.SHA256;

public class UserRegisterActionCommand implements UserInterface {

	@SuppressWarnings("static-access")
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String encoding = "UTF-8";
		
		String mid = request.getParameter("userID")==null ? "" : request.getParameter("userID");
		String pwd = request.getParameter("userPassword")==null ? "" : request.getParameter("userPassword");
		String email = request.getParameter("userEmail")==null ? "" : request.getParameter("userEmail");
		String address = request.getParameter("address")==null ? "" : request.getParameter("address");
		
		// Back End 체크.....(DB에 저장된 자료들 중에서 Null값과 길이에 대한 체크.... 중복체크(아이디/닉네임)...
		
		// 아이디/닉네임 중복체크
		UserDAO dao = new UserDAO();
		
		UserVO vo = dao.getUserInfo(mid);
		if(vo.getUserID() != null) {
			request.setAttribute("msg", "이미 사용중인 아이디 입니다.");
			request.setAttribute("url", "userJoin.us");
			return;
		}
		
		// 비밀번호 암호화처리(SHA256)
		SHA256 security = new SHA256();
		pwd = security.getSHA256(pwd);

		
		// 이메일 암호화처리(SHA256)
		security = new SHA256();
		String hashEmail = security.getSHA256(email);
		
		// 체크가 모드 끝난 자료들을 VO에 담아서 DB에 저장시켜준다.
		vo = new UserVO();
		vo.setUserID(mid);
		vo.setUserPassword(pwd);
		vo.setUserEmail(email);
		vo.setUserEmailHash(hashEmail);
		vo.setAddress(address);
		
		
		int res = dao.join(vo);
		
		if (res == 1) {
		    request.setAttribute("msg", "회원에 가입되셨습니다.\\n다양한 기능을 원하시면 이메일 인증을 해주세요.");
		    request.setAttribute("url", "emailSendAction.us");

		    HttpSession session = request.getSession();
		    session.setAttribute("sUserID", mid);
		}
		else {
			request.setAttribute("msg", "회원가입 실패");
			request.setAttribute("url", "userJoin.us");
		}
	}

}
