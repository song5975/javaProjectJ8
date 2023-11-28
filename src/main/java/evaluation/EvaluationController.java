package evaluation;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.ev")
@SuppressWarnings("serial")
public class EvaluationController extends HttpServlet{
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        EvaluationInterface command = null;
		String viewPage = "/WEB-INF/evaluation";
		
		String com = request.getRequestURI();
		System.out.println(com);
		com = com.substring(com.lastIndexOf("/"),com.lastIndexOf("."));
		
		System.out.println(com);
		
		// index.jsp로 이동
		/*
		if(com.equals("/index")) {
			viewPage = "/include/message.jsp";
		}
		*/
		
        // index.jsp로 이동
        if (com.equals("/index")) {
            // 최초 페이지 로딩 시 데이터를 가져오도록 수정
            command = new EvaluationSearchActionCommand();
            command.execute(request, response);
            viewPage = "/index.jsp";
        }

		// 사용자가 강의 평가를 작성하고 등록하기 버튼을 눌렀을 때
		if(com.equals("/evaluationRegisterAction")) {
			command = new EvaluationRegisterActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}

		// 사용자가 강의 평가 게시글에 대한 신고하기 버튼을 눌렀을 때
		if(com.equals("/evaluationReportAction")) {
			command = new EvaluationReportActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}

		// 사용자가 강의 평가 게시글에 대한 검색하기 버튼을 눌렀을 때
		if(com.equals("/evaluationSearchAction")) {
			command = new EvaluationSearchActionCommand();
			command.execute(request, response);
			viewPage = "/index.jsp";
		}
		
		request.getRequestDispatcher(viewPage).forward(request, response);
	}
}
