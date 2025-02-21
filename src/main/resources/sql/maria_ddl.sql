/*maria crud test : create test table */
create table test_tb(
                        tb_idx varchar(50) primary key,
                        nm varchar(50)
) comment '테스트 CRUD 테이블'

/*파일 정보 테이블*/
create table com_file(
     file_id varchar(50) comment '파일ID',
     file_parent_id varchar(50) comment '부모파일ID-Thumnail등 활용',
     file_seq int comment '파일 SEQ',
     file_upld_nm varchar(255) comment '파일업로드명',
     file_orign_nm varchar(255) comment '실제파일명',
     file_path varchar(500) comment '파일경로',
     file_module varchar(50) comment '업로드한 파일 모듈(test_tb)',
     file_ord int comment '파일순서',
     file_size int comment '파일사이즈',
     del_yn varchar(1) default 'N' comment '삭제여부',
     thum_yn varchar(1) default 'N' comment '썸네일여부',
     crt_dt datetime comment '등록일',
     crt_id varchar(50) comment '등록자',
     crt_ip_addr varchar(15) comment '등록자IP',
     upd_dt datetime comment '수정일',
     upd_id varchar(50) comment '수정자',
     upd_ip_addr varchar(15) comment '수정자IP',
     del_dt datetime comment '삭제일',
     del_id varchar(50) comment '삭제자',
     del_ip_addr varchar(15) comment '삭제자IP',
     primary key (file_id, file_seq)
) comment '파일 정보 테이블';


/* 임시 파일 정보 테이블 */
create table com_file_temp (
                               temp_id bigint auto_increment,            -- 임시 테이블의 고유 ID
                               file_id varchar(50),                      -- 파일 ID
                               file_seq int,                             -- 파일 시퀀스
                               file_upld_nm varchar(255),                -- 업로드된 파일 이름
                               file_orign_nm varchar(255),               -- 원본 파일 이름
                               file_path varchar(500),                   -- 파일 저장 경로
                               file_module varchar(50),                  -- 모듈 정보
                               file_ord int,                             -- 파일 순서
                               file_size int,                            -- 파일 크기
                               file_status varchar(20) default 'PENDING',     -- 파일 상태 (PENDING, VALID, INVALID)
                               invalid_reason varchar(300),             --INVALID 사유
                               transfer_dt datetime,                   --이관 시간
                               expire_dt datetime,                      -- 만료 시각
                               crt_dt datetime default current_datetime, -- 생성 일시
                               crt_id varchar(50),                       -- 생성자 ID
                               crt_ip_addr varchar(15),                  -- 생성자 IP 주소
                               upd_dt datetime,                         -- 수정 일시
                               upd_id varchar(50),                       -- 수정자 ID
                               upd_ip_addr varchar(15),                  -- 수정자 IP 주소
                               primary key (temp_id)                     -- 기본 키
);



create table TB_SEQ(
                       NAME varchar(50) primary key comment '시퀀스테이블명',
                       SEQ int comment '시퀀스'
) comment '시퀀스관리테이블'


/*CRUD 2차 테스트 테이블 - 예제 회원*/
create table member_tb(
                          member_id varchar(50)     comment '회원ID'            primary key,
                          nm varchar(50)            comment '회원명',
                          tellNo varchar(15)        comment '전화번호',
                          email varchar(20)         comment '이메일',
                          file_id varchar(50)       comment '첨부파일ID',
                          file_id2 varchar(50)      comment '첨부파일ID2',
                          del_yn varchar(1)                 default 'N',
                          crt_dt datetime           comment '등록일',
                          crt_id varchar(50)        comment '등록자',
                          crt_ip_addr varchar(15)   comment '등록자IP',
                          upd_dt datetime           comment '수정일',
                          upd_id varchar(50)        comment '수정자',
                          upd_ip_addr varchar(15)   comment '수정자IP',
                          del_dt datetime           comment '삭제일',
                          del_id varchar(50)        comment '삭제자ID',
                          del_ip_addr varchar(15)   comment '삭제자IP'
                          )




/*interceptor 로깅용*/
CREATE TABLE com_access_logs (
                                 log_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- H2에서 AUTO_INCREMENT는 IDENTITY 사용
                                 request_time BIGINT NOT NULL,
                                 ip_address VARCHAR(20),
                                 os_info varchar(100),
                                 user_agent VARCHAR(255),
                                 request_url VARCHAR(1000),
                                 query_string TEXT,
                                 request_method VARCHAR(10),
                                 status_code INT,
                                 processing_time BIGINT,
                                 session_id VARCHAR(255),
                                 user_id VARCHAR(255),
                                 headers TEXT, -- 추가 헤더 정보 (옵션)
                                 crt_dt datetime default now()
);