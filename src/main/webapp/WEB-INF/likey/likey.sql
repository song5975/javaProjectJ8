show tables;

create table likey (
	userID 			varchar(30),		-- 회원 아이디(접속자)
	evaluationID 	int,				-- 평가 아이디(작성한 글의 ID)
	userIP 			varchar(50),		-- 좋아요를 누른 접속자의 접속IP
	
	PRIMARY KEY (userID, evaluationID)
);

desc likey;

select * from likey;

-- drop table likey;