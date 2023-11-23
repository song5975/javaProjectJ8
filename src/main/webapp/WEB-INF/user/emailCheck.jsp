<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Email Check</title>
</head>
<body>
    <form action="emailCheckAction.us" method="post">
        <label for="code">인증 코드 입력:</label>
        <input type="text" id="code" name="code" required>
        <input type="submit" value="인증 확인">
    </form>
</body>
</html>
