show tables;

create table user (
    userID 				varchar(30) primary key,        -- 회원 아이디(중복불허)
    userPassword 		varchar(100),           		-- 회원 비밀번호(SHA256 암호화 처리)
    userEmail 			varchar(50),               		-- 회원 이메일
    userEmailHash 		varchar(64),           			-- 작성자 이메일 데이터에 HASH를 적용한 데이터(회원가입 시 이메일을 확인)
    userEmailChecked 	boolean default false, 			-- 회원의 이메일 인증 여부를 확인
    address 			varchar(100),                	-- 주소(다음 API 활용)
    level 				int default 1                  	-- 회원등급(0:관리자, 1:학생, 99:탈퇴신청회원)
);

desc user;

select * from user;

-- drop table user;