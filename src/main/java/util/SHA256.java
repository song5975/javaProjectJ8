package util;

import java.security.MessageDigest;

// 해시 함수를 사용하여 문자열을 해싱하는 유틸리티 클래스
public class SHA256 {
	
	public static String getSHA256(String input) {
		// 문자열을 저장하기 위한 StringBuffer 객체를 생성
		StringBuffer result = new StringBuffer();
		try {
			// SHA-256 해시 알고리즘을 사용하기 위해 MessageDigest 객체를 생성
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] salt = "Hello! This is Salt.".getBytes();
			// digest 객체를 초기화
			digest.reset();
			// digest에 솔트를 추가
			digest.update(salt);
			// 입력 문자열을 UTF-8로 인코딩하여 바이트 배열로 변환, 이를 digest에 추가하여 해시값을 얻습니다.
			byte[] chars = digest.digest(input.getBytes("UTF-8"));
			// 현재 바이트를 16진수 문자열로 변환
			for(int i = 0; i< chars.length; i++) {
				String hex = Integer.toHexString(0xff & chars[i]);
				if(hex.length() == 1) result.append("0");
				result.append(hex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
