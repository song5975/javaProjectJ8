package likey;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import evaluation.EvaluationVO;
import util.GetConn;

public class LikeyDAO {
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
    
    // 이미 좋아요를 눌렀는지 확인하는 메서드 추가
    public boolean isLiked(String userID, String evaluationID) {
        try {
            sql = "SELECT COUNT(*) FROM likey WHERE userID=? AND evaluationID=?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, evaluationID);
            rs = pstmt.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true; // 이미 좋아요를 누름
            }
        } catch (SQLException e) {
            System.out.println("SQL 오류: " + e.getMessage());
        } finally {
            rsClose();
        }
        return false; // 좋아요를 누르지 않음
    }
    
    // 특정한 게시글에 좋아요를 누르는 함수
    public int like(String userID, String evaluationID, String userIP) {
        try {
            // 중복 확인을 위한 isLiked 메서드를 통해 이미 눌렀는지 확인
            if (isLiked(userID, evaluationID)) {
                return 0; // 이미 누름
            }

            sql = "INSERT INTO likey VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userID);
            pstmt.setString(2, evaluationID);
            pstmt.setString(3, userIP);
            pstmt.executeUpdate();
            return 1; // 성공
        } catch (SQLException e) {
            System.out.println("SQL 오류: " + e.getMessage());
        } finally {
            pstmtClose();
        }
        return -1; // 실패
    }
}
