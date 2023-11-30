package user;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.GetConn;

public class UserDAO {
	private Connection conn = GetConn.getConn();
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	private String sql = "";
	
	UserVO vo = null;
	
	// pstmt 객체 반납
	public void pstmtClose() {
		if(pstmt != null) {
			try {
				pstmt.close();
			} catch (SQLException e) {}
		}
	}
	
	// rs 객체 반납
	public void rsClose() {
		if(rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {}
			finally {
				pstmtClose();
			}
		}
	}
	
	// 로그인 처리
	public int login(String userID, String userPassword) {
		try {
			sql = "select userPassword from user where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1;		// 로그인 성공
				}
				else {
					return 0;		// 비밀번호 불일치
				}
			}
			return -1;				// 비회원(회원가입 필요)
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return -2;					// 데이터베이스 오류
	}
	
	// 회원가입
	public int join(UserVO vo) {
		int res = 0;				// 회원가입 실패
		try {
			sql = "insert into user values (?, ?, ?, ?, default, ?, default)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, vo.getUserID());
			pstmt.setString(2, vo.getUserPassword());
			pstmt.setString(3, vo.getUserEmail());
			pstmt.setString(4, vo.getUserEmailHash());
			pstmt.setString(5, vo.getAddress());
			res = pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println("sql오류 : " + e.getMessage());
		} finally {
			pstmtClose();
		}
		return res;					// 회원가입 성공(1 반환)
	}
	
	// 특정 회원의 이메일을 반환해주는 함수
	public String getUserEmail(String userID) {
		try {
			sql = "select userEmail from user where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getString(1);
			}	
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return null;				// 데이터베이스 오류
	}
	
	
	// 현재 사용자의 이메일 검증 유무 확인(이메일 검증 없이 강의평 작성 불가를 위해 필요)
	public boolean getUserEmailChecked(String userID) {
		try {
			sql = "select userEmailChecked from user where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				return rs.getBoolean(1);
			}	
		} catch (SQLException e) {
			System.out.println("SQL 오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return false;				// 데이터베이스 오류
	}
	
	// 현재 사용자의 이메일 검증 시 userEmailChecked를 true로 변환
	public boolean setUserEmailChecked(String userID) {
	    try {
	        sql = "update user set userEmailChecked = true where userID = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);
	        
	        System.out.println("setUserEmailChecked_Debug_userID = " + userID);
	        
	        pstmt.executeUpdate();
	        return true;
	    } catch (SQLException e) {
	        System.out.println("SQL 오류 : " + e.getMessage());
	    } finally {
	        rsClose();
	    }
	    return false;
	}
	
	// 회원 정보 가져오기(세션 처리용)
	public UserVO getUserInfo(String userID) {
		vo = new UserVO();
		try {
			sql = "select * from user where userID = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userID);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				vo.setUserID(rs.getString("userID"));
				vo.setUserPassword(rs.getString("userPassword"));
				vo.setUserEmail(rs.getString("userEmail"));
				vo.setUserEmailHash(rs.getString("userEmailHash"));
				vo.setUserEmailChecked(rs.getBoolean("userEmailChecked"));
				vo.setAddress(rs.getString("address"));
				vo.setLevel(rs.getInt("level"));
			}
		} catch (SQLException e) {
			System.out.println("sql오류 : " + e.getMessage());
		} finally {
			rsClose();
		}
		return vo;
	}

	public int getUserLevel(String userID) {
	    try {
	        sql = "SELECT level FROM user WHERE userID = ?";
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setString(1, userID);

	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            return rs.getInt("level");
	        }
	    } catch (SQLException e) {
	        System.out.println("SQL 오류: " + e.getMessage());
	    } finally {
	        rsClose();
	    }

	    // 존재하지 않는 아이디나 다른 오류인 경우 -1 또는 특정 값을 반환
	    return -1;
	}

	// userID로 level이 관리자인지 확인
	public boolean isAdmin(String userID) {
	    int userLevel = getUserLevel(userID);

	    // level이 0이면 true 반환
	    return userLevel == 0;
	}


}
