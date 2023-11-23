show tables;

create table likey (
	userID 			varchar(30),		-- 회원 아이디(작성자)
	evaluationID 	int,				-- 평가 아이디
	userIP 			varchar(50)			-- 좋아요를 누른 사용자의 접속IP
);

desc likey;

select * from likey;

-- drop table likey;