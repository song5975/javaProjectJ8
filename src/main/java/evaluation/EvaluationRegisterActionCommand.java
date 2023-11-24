package evaluation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class EvaluationRegisterActionCommand implements EvaluationInterface {
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String encoding = "UTF-8";
		
		String lectureName = request.getParameter("lectureName")==null ? "" : request.getParameter("lectureName");
		String professorName = request.getParameter("professorName")==null ? "" : request.getParameter("professorName");
		int lectureYear = 0; // 기본값으로 초기화
		if(request.getParameter("lectureYear") != null) {
		    try {
		        lectureYear = Integer.parseInt(request.getParameter("lectureYear"));
		    } catch (Exception e) {
		        System.out.println("lectureYear_type_Error");
		    }
		}
		String semesterDivide = request.getParameter("semesterDivide")==null ? "" : request.getParameter("semesterDivide");
		String lectureDivide = request.getParameter("lectureDivide")==null ? "" : request.getParameter("lectureDivide");
		String evaluationTitle = request.getParameter("evaluationTitle")==null ? "" : request.getParameter("evaluationTitle");
		String evaluationContent = request.getParameter("evaluationContent")==null ? "" : request.getParameter("evaluationContent");
		String totalScore = request.getParameter("totalScore")==null ? "" : request.getParameter("totalScore");
		String creditScore = request.getParameter("creditScore")==null ? "" : request.getParameter("creditScore");
		String comfortableScore = request.getParameter("comfortableScore")==null ? "" : request.getParameter("comfortableScore");
		String lectureScore = request.getParameter("lectureScore")==null ? "" : request.getParameter("lectureScore");
		
		
		// Back End 체크.....(DB에 저장된 자료들 중에서 Null값 등...더 추가해서 내용 작성.../front와 중복 체크)
		if(lectureName == null || professorName == null || lectureYear == 0 || semesterDivide == null || 
				lectureDivide == null || evaluationTitle == null || evaluationContent == null || totalScore == null ||
				creditScore == null || comfortableScore == null || lectureScore == null ||
				evaluationTitle.equals("") || evaluationContent.equals("")) {
			request.setAttribute("msg", "평가등록 실패");
			request.setAttribute("url", "index.jsp");
		}
		
	    HttpSession session = request.getSession();
	    String userID = (String) session.getAttribute("sUserID");
	    
		// vo에 저장
		EvaluationVO vo = new EvaluationVO();
		vo.setUserID(userID);
		vo.setLectureName(lectureName);
		vo.setProfessorName(professorName);
		vo.setLectureYear(lectureYear);
		vo.setSemesterDivide(semesterDivide);
		vo.setLectureDivide(lectureDivide);
		vo.setEvaluationTitle(evaluationTitle);
		vo.setEvaluationContent(evaluationContent);
		vo.setTotalScore(totalScore);
		vo.setCreditScore(creditScore);
		vo.setComfortableScore(comfortableScore);
		vo.setLectureScore(lectureScore);
		
		// DB에 등록
		EvaluationDAO dao = new EvaluationDAO();
		int res = dao.write(vo);
		
		if (res == 1) {
		    request.setAttribute("msg", "평가가 등록되었습니다.");
		    request.setAttribute("url", "index.jsp");
		}
		else {
			request.setAttribute("msg", "평가등록 실패");
			request.setAttribute("url", "index.jsp");
		}
	}
}
