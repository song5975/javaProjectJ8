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

            // 사용자 입력 값에 대한 XSS 방지 처리
            setStringSafe(pstmt, 1, vo.getUserID());
            setStringSafe(pstmt, 2, vo.getLectureName());
            setStringSafe(pstmt, 3, vo.getProfessorName());
            setInt(pstmt, 4, vo.getLectureYear());
            setStringSafe(pstmt, 5, vo.getSemesterDivide());
            setStringSafe(pstmt, 6, vo.getLectureDivide());
            setStringSafe(pstmt, 7, vo.getEvaluationTitle());
            setStringSafe(pstmt, 8, vo.getEvaluationContent());
            setStringSafe(pstmt, 9, vo.getTotalScore());
            setStringSafe(pstmt, 10, vo.getCreditScore());
            setStringSafe(pstmt, 11, vo.getComfortableScore());
            setStringSafe(pstmt, 12, vo.getLectureScore());

            return pstmt.executeUpdate(); // 등록 시 1 반환
        } catch (SQLException e) {
            System.out.println("SQL 오류 : " + e.getMessage());
        } finally {
            pstmtClose();
        }
        return -1; // 데이터베이스 오류
    }

    // XSS 방지 처리를 위한 함수
    private void setStringSafe(PreparedStatement pstmt, int index, String value) throws SQLException {
        pstmt.setString(index, value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\r\n", "<br>"));
    }

    // 정수 값을 설정하는 함수
    private void setInt(PreparedStatement pstmt, int index, int value) throws SQLException {
        pstmt.setInt(index, value);
    }


    // index.jsp에서 검색하기 버튼을 눌러서 게시글의 전체(개별) 리스트 가져오기
    /*
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
    */
    
    // index.jsp에서 검색하기 버튼을 눌러서 게시글의 전체(개별) 리스트 가져오기
    /*
    public ArrayList<EvaluationVO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
        if (lectureDivide.equals("전체")) {
            lectureDivide = "";
        }
        ArrayList<EvaluationVO> evaluationList = null; // 강의 평가 글이 담기는 List
        try {
            String lectureNameSearch = "%" + search + "%";
            String professorNameSearch = "%" + search + "%";
            String evaluationTitleSearch = "%" + search + "%";
            String evaluationContentSearch = "%" + search + "%";

            if (searchType.equals("최신순")) {
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND lectureName LIKE ? OR professorName LIKE ? OR evaluationTitle LIKE ? OR evaluationContent LIKE ? ORDER BY evaluationID DESC LIMIT ?, ?";
            } else if (searchType.equals("추천순")) {
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND lectureName LIKE ? OR professorName LIKE ? OR evaluationTitle LIKE ? OR evaluationContent LIKE ? ORDER BY likeCount DESC LIMIT ?, ?";
            }

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + lectureDivide + "%");
            pstmt.setString(2, lectureNameSearch);
            pstmt.setString(3, professorNameSearch);
            pstmt.setString(4, evaluationTitleSearch);
            pstmt.setString(5, evaluationContentSearch);
            pstmt.setInt(6, pageNumber * 3); // 시작 레코드 인덱스
            pstmt.setInt(7, 3); // 가져올 레코드 수

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
    */
    
    // index.jsp에서 검색하기 버튼을 눌러서 게시글의 전체(개별) 리스트 가져오기
    public ArrayList<EvaluationVO> getList(String lectureDivide, String searchType, String search, int pageNumber) {
        if (lectureDivide.equals("전체")) {
            lectureDivide = "";
        }
        ArrayList<EvaluationVO> evaluationList = null; // 강의 평가 글이 담기는 List
        try {
            if (searchType.equals("최신순")) {
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY evaluationID DESC LIMIT ?, ?";
            } else if (searchType.equals("추천순")) {
                sql = "SELECT * FROM evaluation WHERE lectureDivide LIKE ? AND CONCAT(lectureName, professorName, evaluationTitle, evaluationContent) LIKE ? ORDER BY likeCount DESC LIMIT ?, ?";
            }
            
            System.out.println("EvaluationDAO_sql = " + sql);
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + lectureDivide + "%");
            pstmt.setString(2, "%" + search + "%");
            pstmt.setInt(3, pageNumber * 3); 	// 시작 레코드 인덱스
            pstmt.setInt(4, 3); 				// 가져올 레코드 수
            System.out.println("EvaluationDAO_2");
            rs = pstmt.executeQuery();
            System.out.println("EvaluationDAO_3");
            evaluationList = new ArrayList<>();
            while (rs.next()) {
                System.out.println("EvaluationDAO_4");
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

                evaluationList.add(vo);
            }
            System.out.println("EvaluationDAO_5");
        } catch (SQLException e) {
            System.out.println("SQL 오류 : " + e.getMessage());
        } finally {
            rsClose();
        }
        System.out.println("EvaluationDAO_6");
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
