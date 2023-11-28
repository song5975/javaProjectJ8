<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Email Check</title>
    <jsp:include page="/include/bs4.jsp" />
    <style>
        body {
            height: 100vh;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .table-container {
            max-width: 600px;
            width: 100%;
        }

        .table-bordered th,
        .table-bordered td {
            border: 3px solid #dee2e6;
        }

        .table-bordered th {
            background-color: #f8f9fa;
        }

        label {
            font-size: 20px;
            font-weight: bold;
            display: block;
            margin-bottom: 10px;
        }
        
        
        .form-label {
            text-align: center;
            padding-top: 30px;
        }
        
    </style>
</head>
<body>
    <form action="emailCheckAction.us" method="post">
	    <div class="table-container">
	        <table class="table table-bordered">
	            <thead>
	                <tr>
	                    <th colspan="2" class="text-center">Email Check</th>
	                </tr>
	            </thead>
	            <tbody>
	                <tr>
	                    <td class="form-label">
	                        <label for="code">인증 코드 입력</label>
	                    </td>
	                    <td>
	                        <input type="text" id="code" name="code" class="form-control" required>
	                    </td>
	                </tr>
	                <tr>
	                    <td colspan="2" class="text-center">
	                        <input type="submit" value="인증 확인" class="btn btn-primary">
	                    </td>
	                </tr>
	            </tbody>
	        </table>
	    </div>
    </form>
</body>
</html>
