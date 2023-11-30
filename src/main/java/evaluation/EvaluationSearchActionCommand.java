package evaluation;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EvaluationSearchActionCommand implements EvaluationInterface {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String lectureDivide = request.getParameter("lectureDivide") == null ? "전체" : request.getParameter("lectureDivide"); 
        String searchType = request.getParameter("searchType") == null ? "최신순" : request.getParameter("searchType");
        String search = request.getParameter("search") == null ? "" : request.getParameter("search");
        
        request.setAttribute("lectureDivide", lectureDivide);
        request.setAttribute("searchType", searchType);
        request.setAttribute("search", search);
        
        // pageNumber의 기본값을 0으로 초기화
        int pageNumber = 0;
        
        // request.getParameter("pageNumber")이 null 또는 빈 문자열이 아닌 경우에만 parseInt 수행
        String pageNumberString = request.getParameter("pageNumber");
        if (pageNumberString != null && !pageNumberString.isEmpty()) {
            try {
                pageNumber = Integer.parseInt(pageNumberString);
            } catch (NumberFormatException e) {
                System.out.println("pageNumber_type_Error: " + e.getMessage());
            }
        }
        
        EvaluationDAO dao = new EvaluationDAO();
        ArrayList<EvaluationVO> evaluationList = dao.getList(lectureDivide, searchType, search, pageNumber);

        // 평가 목록을 request에 저장
        request.setAttribute("evaluationList", evaluationList);
        
        System.out.println(evaluationList.toString());

        // 다음에 표시할 페이지 번호를 계산하여 request에 저장
        int nextPageNumber = pageNumber + 1;
        request.setAttribute("nextPageNumber", nextPageNumber);
        System.out.println("EvaluationSearchActionCommand_pageNumber = " + nextPageNumber);
      
    }
}
