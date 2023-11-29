// registrationValidation.js

function fCheck() {
    let regUserID = /^[a-zA-Z0-9_]{4,30}$/;
    let regUserPassword = /(?=.*[0-9a-zA-Z]).{4,100}$/;

    let userID = document.forms["myform"].elements["userID"].value.trim();
    let userPassword = document.forms["myform"].elements["userPassword"].value;

    let submitFlag = 0;

    if (!regUserID.test(userID)) {
        alert("아이디는 4~30자리의 영문 소/대문자와 숫자, 언더바(_)만 사용가능합니다.");
        document.forms["myform"].elements["userID"].focus();
        return false;
    } else if (!regUserPassword.test(userPassword)) {
        alert("비밀번호는 1개 이상의 문자와 특수문자 조합의 4~100 자리로 작성해주세요.");
        document.forms["myform"].elements["userPassword"].focus();
        return false;
    } else {
        submitFlag = 1;
    }

    // 주소 합치기
    let postcode = document.forms["myform"].elements["postcode"].value + " ";
    let roadAddress = document.forms["myform"].elements["roadAddress"].value + " ";
    let detailAddress = document.forms["myform"].elements["detailAddress"].value + " ";
    let extraAddress = document.forms["myform"].elements["extraAddress"].value + " ";
    let address = postcode + roadAddress + detailAddress + extraAddress;
    document.forms["myform"].elements["address"].value = address.trim();
    
	console.log(address + "Debug_registrationValidation");

    // 전송전에 모든 체크가 끝나면 submitFlag가 1로 되게된다. 이때 값들을 서버로 전송처리한다.
    if (submitFlag === 1) {
        document.forms["myform"].submit();
    } else {
        alert("회원가입 실패! 내용을 확인하세요.");
    }

    return true; // 정상적인 경우 true 반환
}
