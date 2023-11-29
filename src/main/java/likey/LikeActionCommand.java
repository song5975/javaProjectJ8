package likey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import evaluation.EvaluationDAO;

public class LikeActionCommand implements LikeyInterface {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 강의를 평가한 게시글의 ID를 가져오기
		String evaluationID = request.getParameter("evaluationID")==null ? "" : request.getParameter("evaluationID");
		System.out.println(evaluationID);
		
		// 접속한 회원의 세션에서 아이디 가져오기
		HttpSession session = request.getSession();
	    String userID = (String) session.getAttribute("sUserID");
	    System.out.println(userID);
	    
        // 클라이언트의 IP 주소 가져오기
        String userIP = request.getRemoteAddr();
        System.out.println(userIP);

		// 이미 좋아요를 눌렀는지 확인
        LikeyDAO Ldao = new LikeyDAO();
        if (Ldao.isLiked(userID, evaluationID)) {
            request.setAttribute("msg", "이미 추천을 누른 글입니다.");
            request.setAttribute("url", "index.jsp");
            return;
        }

        EvaluationDAO Edao = new EvaluationDAO();
        int res = Ldao.like(userID, evaluationID, userIP);
        System.out.println(res);
        if (res == 1) {
            res = Edao.like(evaluationID);
            if (res == 1) {
                request.setAttribute("msg", "추천이 완료되었습니다.");
                request.setAttribute("url", "index.jsp");
            } else {
                request.setAttribute("msg", "데이터베이스 오류로 추천 실패");
                request.setAttribute("url", "index.jsp");
            }
        } else if (res == 0) {
            request.setAttribute("msg", "이미 추천을 누른 글입니다.");
            request.setAttribute("url", "index.jsp");
        } else {
            request.setAttribute("msg", "데이터베이스 오류로 추천 실패");
            request.setAttribute("url", "index.jsp");
        }
	}
}
