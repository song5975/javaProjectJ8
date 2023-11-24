package evaluation;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EvaluationSearchActionCommand implements EvaluationInterface {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lectureDivide = request.getParameter("lectureDivide") == null ? "" : request.getParameter("lectureDivide");
        String searchType = request.getParameter("searchType") == null ? "" : request.getParameter("searchType");
        String search = request.getParameter("search") == null ? "" : request.getParameter("search");
        int pageNumber = request.getParameter("pageNumber") == null ? 0 : Integer.parseInt(request.getParameter("pageNumber"));

//        int pageNumber = 0; // 기본값으로 초기화
//        
//        if (request.getParameter("pageNumber") != null) {
//            try {
//                pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
//            } catch (Exception e) {
//                System.out.println("pageNumber_type_Error");
//            }
//        }

        EvaluationDAO dao = new EvaluationDAO();
        ArrayList<EvaluationVO> evaluationList = dao.getList(lectureDivide, searchType, search, pageNumber);
        for(EvaluationVO vo :evaluationList) {
        	System.out.println(vo.toString());
        }
        // 평가 목록을 request에 저장
        request.setAttribute("evaluationList", evaluationList);

        // 다음에 표시할 페이지 번호를 계산하여 request에 저장
        int nextPageNumber = pageNumber + 1;
        request.setAttribute("nextPageNumber", nextPageNumber);
        
//		request.setAttribute("msg", "검색 완료");
//	    request.setAttribute("url", "index.jsp");
    }
}
