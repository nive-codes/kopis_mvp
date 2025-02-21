/*h2 crud test : create test table */
create table test_tb(
                        tb_idx varchar(50) primary key,
                        nm varchar(50)
);




/*data  pk */
create table TB_SEQ(
                       NAME varchar(50) primary key ,
                       SEQ NUMBER
)

/*파일 정보 테이블*/
create table com_file(
                         file_id varchar(50) ,
                         file_parent_id varchar(50),
                         file_seq int ,
                         file_upld_nm varchar(255) ,
                         file_orign_nm varchar(255) ,
                         file_path varchar(500) ,
                         file_module varchar(50),
                         file_ord int ,
                         file_size int,
                         del_yn varchar(1) default 'N' ,
                         thum_yn varchar(1) default 'N',
                         crt_dt timestamp ,
                         crt_id varchar(50) ,
                         crt_ip_addr varchar(15) ,
                         upd_dt timestamp ,
                         upd_id varchar(50) ,
                         upd_ip_addr varchar(15) ,
                         del_dt timestamp ,
                         del_id varchar(50) ,
                         del_ip_addr varchar(15) ,
                         primary key (file_id, file_seq)
)


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
                       transfer_dt timestamp,                   --이관일시
                       expire_dt timestamp,                      -- 만료 시간 (파일 처리 유효 기간)
                       crt_dt timestamp default current_timestamp, -- 생성 일시
                       crt_id varchar(50),                       -- 생성자 ID
                       crt_ip_addr varchar(15),                  -- 생성자 IP 주소
                       upd_dt timestamp,                         -- 수정 일시
                       upd_id varchar(50),                       -- 수정자 ID
                       upd_ip_addr varchar(15),                  -- 수정자 IP 주소
                       primary key (temp_id)                     -- 기본 키
);


/*CRUD 2차 테스트 테이블 - 예제 회원*/
create table member_tb(
    member_id varchar(50) primary key,
    nm varchar(50),
    tellNo varchar(15),
    email varchar(20),
    file_id varchar(50),
    file_id2 varchar(50),
    del_yn varchar(1) default 'N',
    crt_dt timestamp ,
    crt_id varchar(50) ,
    crt_ip_addr varchar(15) ,
    upd_dt timestamp ,
    upd_id varchar(50) ,
    upd_ip_addr varchar(15) ,
    del_dt timestamp ,
    del_id varchar(50) ,
    del_ip_addr varchar(15)
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
  crt_dt TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);