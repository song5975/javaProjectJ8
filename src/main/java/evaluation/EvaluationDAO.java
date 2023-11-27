package evaluation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import util.GetConn;

public class EvaluationDAO {
    private Connection conn = GetConn.getConn();
    private PreparedStatement pstmt = null;
    private ResultSet rs = null;

    private String sql = "";

    EvaluationVO vo = null;

    // pstmt 객체 반납
    public void pstmtClose() {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
            }
        }
    }

    // rs 객체 반납
    public void rsClose() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            } finally {
                pstmtClose();
            }
        }
    }

    // 강의평가 글쓰기
    public int write(EvaluationVO vo) {
        try {
            sql = "INSERT INTO evaluation VALUES (default, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, vo.getUserID());
            pstmt.setString(2, vo.getLectureName());
            pstmt.setString(3, vo.getProfessorName());
            pstmt.setInt(4, vo.getLectureYear());
            pstmt.setString(5, vo.getSemesterDivide());
            pstmt.setString(6, vo.getLectureDivide());
            pstmt.setString(7, vo.getEvaluationTitle());
            pstmt.setString(8, vo.getEvaluationContent());
            pstmt.setString(9, vo.getTotalScore());
            pstmt.setString(10, vo.getCreditScore());
            pstmt.setString(11, vo.getComfortableScore());
            pstmt.setString(12, vo.getLectureScore());
            return pstmt.executeUpdate(); // 등록 시 1 반환
        } catch (SQLException e) {
            System.out.println("SQL 오류 : " + e.getMessage());
        } finally {
            pstmtClose();
        }
        return -1; // 데이터베이스 오류
    }

    // index.jsp에서 검색하기 버튼을 눌러서 게시글의 전체(개별) 리스트 가져오기
    public ArrayList<EvaluationVO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
        if (lectureDivide.equals("전체")) {
            lectureDivide = "";
        }
        ArrayList<EvaluationVO> evaluationList = null; // 강의 평가 글이 담기는 List
        try {
            if (searchType.equals("최신순")) {
                // 변경된 부분: LIMIT에 사용되는 두 번째 파라미터(레코드 수)를 바인딩할 수 있도록 수정
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY evaluationID DESC LIMIT ?, ?";
            } else if (searchType.equals("추천순")) {
                // 변경된 부분: LIMIT에 사용되는 두 번째 파라미터(레코드 수)를 바인딩할 수 있도록 수정
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY likeCount DESC LIMIT ?, ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + lectureDivide + "%");
            pstmt.setString(2, "%" + search + "%");
            // 변경된 부분: LIMIT에 사용되는 두 개의 파라미터(시작 레코드 인덱스, 레코드 수)를 바인딩
            pstmt.setInt(3, pageNumber * 3); // 시작 레코드 인덱스
            pstmt.setInt(4, 3); // 가져올 레코드 수

            rs = pstmt.executeQuery();

            evaluationList = new ArrayList<>();
            while (rs.next()) {
                // EvaluationVO 객체 생성 및 설정
                EvaluationVO vo = new EvaluationVO();
                vo.setEvaluationID(rs.getInt("evaluationID"));
                vo.setUserID(rs.getString("userID"));
                vo.setLectureName(rs.getString("lectureName"));
                vo.setProfessorName(rs.getString("professorName"));
                vo.setLectureYear(rs.getInt("lectureYear"));
                vo.setSemesterDivide(rs.getString("semesterDivide"));
                vo.setLectureDivide(rs.getString("lectureDivide"));
                vo.setEvaluationTitle(rs.getString("evaluationTitle"));
                vo.setEvaluationContent(rs.getString("evaluationContent"));
                vo.setTotalScore(rs.getString("totalScore"));
                vo.setCreditScore(rs.getString("creditScore"));
                vo.setComfortableScore(rs.getString("comfortableScore"));
                vo.setLectureScore(rs.getString("lectureScore"));
                vo.setLikeCount(rs.getInt("likeCount"));

                // List에 추가
                evaluationList.add(vo);
            }
        } catch (SQLException e) {
            System.out.println("SQL 오류 : " + e.getMessage());
        } finally {
            rsClose();
        }
        return evaluationList;
    }
    
    // evaluationID로 userID 가져오기
    public String getUserID(String evaluationID) {
		try {
			sql = "select userID from evaluation where evaluationID = ?";
			pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, Integer.parseInt(evaluationID));
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}	
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return null;				// 존재하지 않는 아이디
    }

    // 강의평가 추천 구현하기
    public int like(String evaluationID) {
	    try {
	        sql = "update evaluation set likeCount = likeCount + 1 where evaluationID = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, Integer.parseInt(evaluationID));
	        return pstmt.executeUpdate();
	    } catch (SQLException e) {
	        System.out.println("SQL 오류 : " + e.getMessage());
	    } finally {
	        rsClose();
	    }
	    return -1;
    }

    // 강의평가 삭제 구현하기
    public int delete(String evaluationID) {
    	try {
    		sql = "delete from evaluation where evaluationID = ?";
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, Integer.parseInt(evaluationID));
    		return pstmt.executeUpdate();
    	} catch (SQLException e) {
    		System.out.println("SQL 오류 : " + e.getMessage());
    	} finally {
    		rsClose();
    	}
    	return -1;
    }

}
