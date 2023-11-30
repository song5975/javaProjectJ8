package likey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import evaluation.EvaluationDAO;
import user.UserDAO;

public class DeleteActionCommand implements LikeyInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String evaluationID = request.getParameter("evaluationID")==null ? "" : request.getParameter("evaluationID");
		
		HttpSession session = request.getSession();
	    String userID = (String) session.getAttribute("sUserID");

		EvaluationDAO dao = new EvaluationDAO();
		UserDAO Udao = new UserDAO();
		if(userID.equals(dao.getUserID(evaluationID)) || Udao.isAdmin(userID)) {
			int res = dao.delete(evaluationID);
			
			if(res == 1) {
				request.setAttribute("msg", "삭제가 완료되었습니다.");
			    request.setAttribute("url", "index.jsp");
			}
			else {
				request.setAttribute("msg", "데이터베이스 오류로 삭제 실패");
				request.setAttribute("url", "index.jsp");
			}
		}
		else {
			request.setAttribute("msg", "자신이 작성한 글만 삭제할 수 있습니다.");
			request.setAttribute("url", "index.jsp");
		}
	}

}
