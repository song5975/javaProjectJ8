package likey;

public class LikeyVO {
	private String userID;
	private int evaluationID;
	private String userIP;
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public int getEvaluationID() {
		return evaluationID;
	}
	public void setEvaluationID(int evaluationID) {
		this.evaluationID = evaluationID;
	}
	public String getUserIP() {
		return userIP;
	}
	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}
	
	@Override
	public String toString() {
		return "LikeyVO [userID=" + userID + ", evaluationID=" + evaluationID + ", userIP=" + userIP + "]";
	}
	
	
}
