package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GetConn {
	private static Connection conn = null;
	
	private String driver = "com.mysql.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/javaProject8";
	private String user = "root";
	private String password = "1234";
	
	@SuppressWarnings("unused")
	private static final GetConn instance = new GetConn();
	
	private GetConn() {
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException e) {
			System.out.println("드라이버 검색 실패");
		} catch (SQLException e) {
			System.out.println("데이터베이스 연동 실패");
		}
	}
	
	// GetConn객체를 외부에서 연결해서 사용해주기 위한 메소드를 public 선언자로 만들어준다.
	public static Connection getConn() {
		return conn;
	}

}
