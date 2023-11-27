package likey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("*.li")
@SuppressWarnings("serial")
public class LikeyController extends HttpServlet {
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        
        LikeyInterface command = null;
		String viewPage = "/WEB-INF/evaluation";
		
		String com = request.getRequestURI();
		System.out.println(com);
		com = com.substring(com.lastIndexOf("/"),com.lastIndexOf("."));
		
		System.out.println(com);
		
		// index.jsp로 이동
		if(com.equals("/index")) {
			viewPage = "/include/message.jsp";
		}

		// 사용자가 강의 평가 게시글에 대한 추천 버튼을 눌렀을 때
		if(com.equals("/likeAction")) {
			command = new LikeActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}

		// 작성자가 강의 평가 게시글에 대한 삭제 버튼을 눌렀을 때
		if(com.equals("/deleteAction")) {
			command = new DeleteActionCommand();
			command.execute(request, response);
			viewPage = "/include/message.jsp";
		}
		
		request.getRequestDispatcher(viewPage).forward(request, response);
	}
}
