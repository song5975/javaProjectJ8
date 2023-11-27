show tables;

create table evaluation (
	evaluationID 		int primary key auto_increment,		-- 평가 아이디(중복불허, 자동으로 증가)
	userID 				varchar(30),						-- 회원 아이디(평가를 작성한 회원 아이디)
	lectureName 		varchar(50),						-- 강의명
	professorName 		varchar(20),						-- 교수명
	lectureYear 		int,								-- 수강년도
	semesterDivide 		varchar(20),						-- 수강학기
	lectureDivide 		varchar(10),						-- 강의구분(전체, 전공, 교양 구분)
	evaluationTitle 	varchar(50),						-- 평가제목
	evaluationContent 	varchar(2048),						-- 평가내용
	totalScore 			varchar(5),							-- 종합평점(A, B, C, D, F / 전체적인 평점)
	creditScore 		varchar(5),							-- 성적평점(A, B, C, D, F / 성적을 잘 주는지 평가)
	comfortableScore 	varchar(5),							-- 널널평점(A, B, C, D, F / 강의 분위기가 편한지 평가)
	lectureScore 		varchar(5),							-- 강의평점(A, B, C, D, F / 강의가 도움이 되는지 평가)
	likeCount 			int									-- 평가 글에 대한 추천 개수
);

desc evaluation;

select * from evaluation;

-- drop table evaluation;