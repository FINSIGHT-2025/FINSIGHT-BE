DROP TABLE IF EXISTS `Watch`;
DROP TABLE IF EXISTS `News_Product`;
DROP TABLE IF EXISTS `News_Keyword`;
DROP TABLE IF EXISTS `Keyword`;
DROP TABLE IF EXISTS `News`;
DROP TABLE IF EXISTS `History`;
DROP TABLE IF EXISTS `Holdings`;
DROP TABLE IF EXISTS `Investment_Profile`;
DROP TABLE IF EXISTS `D_Option`;
DROP TABLE IF EXISTS `F_Asset_Allocation`;
DROP TABLE IF EXISTS `F_Stock_Holdings`;
DROP TABLE IF EXISTS `E_Asset_Allocation`;
DROP TABLE IF EXISTS `E_Constituent_Stocks`;
DROP TABLE IF EXISTS `E_Equity_Ratio`;
DROP TABLE IF EXISTS `Etf`;
DROP TABLE IF EXISTS `Deposit`;
DROP TABLE IF EXISTS `Fund`;
DROP TABLE IF EXISTS `User`;

CREATE TABLE `User` (
                        `user_id` VARCHAR(255) NOT NULL COMMENT '아이디',
                        `user_name` VARCHAR(255) NOT NULL COMMENT '사용자 이름',
    -- `user_nickname` VARCHAR(255) NOT NULL COMMENT '닉네임',
                        `user_password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
                        `user_birthday` DATE NOT NULL COMMENT '생년월일',
                        `user_email` VARCHAR(255) NOT NULL COMMENT '이메일',
                        `user_created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '가입일자',
                        `user_role` ENUM('incomplete', 'complete') NOT NULL DEFAULT 'incomplete' COMMENT '역할',
                        PRIMARY KEY (`user_id`)
);

CREATE TABLE `Holdings` (
                            `holdings_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '기본키(AUTO INCREMENT)',
                            `user_id` VARCHAR(255) NOT NULL COMMENT '아이디',
                            `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                            `product_category` ENUM('deposit', 'fund', 'etf') NOT NULL COMMENT '카테고리',
                            `holdings_total_price` DECIMAL(15, 2) NOT NULL COMMENT '총 투자금액',
                            `holdings_total_quantity` INT NOT NULL COMMENT '총 투자수량',
                            `holdings_status` ENUM('holding', 'zero', 'terminated', 'expired') NOT NULL COMMENT '상태',
                            PRIMARY KEY (`holdings_id`),
                            FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON DELETE CASCADE
    -- `product_code`는 다형 참조라 FK 불가 (예금/펀드/ETF 모두를 참조하므로)
);

CREATE TABLE `Investment_Profile` (
                                      `user_id` VARCHAR(255) NOT NULL COMMENT '아이디',
                                      `investment_profile_type` ENUM('stable', 'stableplus', 'neutral', 'aggressive', 'veryaggressive') NOT NULL COMMENT '위험 성향',
                                      `investment_profile_updated_at` DATETIME NOT NULL COMMENT '갱신일자',
                                      PRIMARY KEY (`user_id`),
                                      FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON DELETE CASCADE
);

CREATE TABLE `Fund` (
                        `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                        `product_name` VARCHAR(255) NOT NULL COMMENT '상품명',
                        `product_company_name` VARCHAR(255) NOT NULL COMMENT '금융회사명',
                        `product_risk_grade` TINYINT NOT NULL COMMENT '위험등급',
                        `fund_country` ENUM('domestic', 'foreign') NOT NULL COMMENT '국가',
                        `fund_type` ENUM('equity', 'mixed', 'bond') NOT NULL COMMENT '펀드 유형',
                        `fund_delisting_status` BOOLEAN NOT NULL COMMENT '상장 폐지 여부',
                        `fund_feature` TEXT NULL COMMENT '펀드 특징',
                        `fund_management_strategy` TEXT NULL COMMENT '운용 전략',
                        `fund_fee_total_expense_ratio` VARCHAR(100) NULL COMMENT '수수료(총보수)',
                        `fund_fee_back_end_load` VARCHAR(100) NULL COMMENT '수수료(후취판매)',
                        `fund_fee_redemption` VARCHAR(100) NULL COMMENT '수수료(환매)',
                        `fund_report_investment` TEXT NULL COMMENT '자산운용보고서(URL)',
                        `fund_report_collective_investment_terms_url` TEXT NULL COMMENT '집합투자규약(URL)',
                        `fund_report_investment_prospectus_url` TEXT NULL COMMENT '투자설명서(URL)',
                        `fund_report_simplidfied_prospectus_url` TEXT NULL COMMENT '간이투자설명서(URL)',
                        `fund_fee_front_end_load` VARCHAR(100) NULL COMMENT '수수료(선취판매)',
                        `fund_established_date` VARCHAR(50) NULL COMMENT '설정일',
                        PRIMARY KEY (`product_code`)
);

CREATE TABLE `Etf` (
                       `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                       `product_name` VARCHAR(255) NOT NULL COMMENT '상품명',
                       `product_company_name` VARCHAR(255) NOT NULL COMMENT '금융회사명',
                       `product_risk_grade` TINYINT NOT NULL COMMENT '위험등급',
                       `etf_country` ENUM('domestic', 'foreign') NOT NULL COMMENT '국가',
                       `etf_type` ENUM('equity', 'mixed', 'bond') NOT NULL COMMENT 'ETF 유형',
                       `etf_delisting_status` BOOLEAN NOT NULL COMMENT '상장 폐지 여부',
                       `etf_net_assets` DECIMAL(20, 2) NULL COMMENT '순자산',
                       `etf_fund_characteristics` TEXT NULL COMMENT '펀드 특징',
                       `etf_management_strategy` TEXT NULL COMMENT '운용 전략',
                       `etf_total_expense_ratio` DECIMAL(5, 2) NULL COMMENT '총보수율',
                       `etf_collective_investment_agreement_url` TEXT NULL COMMENT '집합 투자 규약(URL)',
                       `etf_investment_prospectus_url` TEXT NULL COMMENT '투자설명서(URL)',
                       `etf_simplified_prospectus_url` TEXT NULL COMMENT '간이투자설명서(URL)',
                       `etf_benchmark_index` VARCHAR(50) NULL COMMENT '기초지수',
                       `etf_listing_date` DATE NULL COMMENT '상장일',
                       `etf_min_trading_unit` INT NULL COMMENT '최소 거래 단위',
                       `etf_tax_type` VARCHAR(20) NULL COMMENT '과세 유형',
                       PRIMARY KEY (`product_code`)
);

CREATE TABLE `Deposit` (
                           `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                           `product_name` VARCHAR(255) NOT NULL COMMENT '상품명',
                           `product_company_name` VARCHAR(255) NOT NULL COMMENT '금융회사명',
                           `product_risk_grade` TINYINT NOT NULL COMMENT '위험등급',
                           `deposit_join_member` VARCHAR(255) NULL COMMENT '가입대상',
                           `deposit_spcl_cnd` TEXT NULL COMMENT '우대조건',
                           `deposit_mtrt_int` TEXT NULL COMMENT '만기 후 이자율',
                           `deposit_max_limit` VARCHAR(255) NULL COMMENT '최고한도',
                           `deposit_join_way` TEXT NULL COMMENT '가입 방법',
                           `deposit_join_deny` VARCHAR(255) NULL COMMENT '가입제한',
                           `deposit_etc_note` TEXT NULL COMMENT '기타 유의사항',
                           PRIMARY KEY (`product_code`)
);

CREATE TABLE `History` (
                           `history_id` BIGINT NOT NULL COMMENT '기본키',
                           `holdings_id` BIGINT NOT NULL COMMENT '외래키',
                           `history_trade_type` ENUM('buy', 'sell', 'deposit') NOT NULL COMMENT '거래 유형',
                           `history_trade_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '거래일',
                           `history_quantity` INT NOT NULL DEFAULT 1 COMMENT '투자수량',
                           `history_amount` BIGINT NOT NULL COMMENT '투자금액',
                           PRIMARY KEY (`history_id`),
                           FOREIGN KEY (`holdings_id`) REFERENCES `Holdings`(`holdings_id`) ON DELETE CASCADE
);

CREATE TABLE `News` (
                        `news_id` VARCHAR(255) NOT NULL COMMENT '뉴스 ID',
                        `news_title` VARCHAR(255) NOT NULL COMMENT '제목',
                        `news_summary` TEXT NOT NULL COMMENT '요약',
                        `news_content_url` TEXT NOT NULL COMMENT '기사 URL',
                        `news_published_at` DATETIME NOT NULL COMMENT '게시 일시',
                        `news_score` DOUBLE NOT NULL COMMENT '정확도',
                        `news_sentiment` ENUM('positive', 'negative', 'neutral') NOT NULL COMMENT '감성 (긍정/부정/중립)',
                        `news_publisher` VARCHAR(255) COMMENT '신문사',
                        PRIMARY KEY (`news_id`),
                        UNIQUE KEY `uq_news_title` (`news_title`)
);

CREATE TABLE `Keyword` (
                           `keyword_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '키워드 ID',
                           `keyword` VARCHAR(255) NOT NULL COMMENT '키워드',
                           PRIMARY KEY (`keyword_id`),
                           UNIQUE KEY `uq_keyword` (`keyword`)
);

CREATE TABLE `News_Keyword` (
                                `news_keyword_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'KEY(AUTO INCREMENT)',
                                `news_id` VARCHAR(255) NOT NULL COMMENT '뉴스 ID',
                                `keyword_id` BIGINT NOT NULL COMMENT '키워드 ID',
                                PRIMARY KEY (`news_keyword_id`),
                                UNIQUE KEY uq_news_keyword (news_id, keyword_id),
                                FOREIGN KEY (`news_id`) REFERENCES `News`(`news_id`) ON DELETE CASCADE,
                                FOREIGN KEY (`keyword_id`) REFERENCES `Keyword`(`keyword_id`) ON DELETE CASCADE
);

CREATE TABLE `News_Product` (
                                `news_product_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'KEY(AUTO INCREMENT)',
                                `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                `news_id` VARCHAR(255) NOT NULL COMMENT '뉴스 ID',
                                `news_product_category` ENUM('deposit', 'fund', 'etf') NOT NULL COMMENT '뉴스 관련 상품 종류',
                                PRIMARY KEY (`news_product_id`),
                                UNIQUE KEY uq_news_product (news_id, product_code),
                                FOREIGN KEY (`news_id`) REFERENCES `News`(`news_id`) ON DELETE CASCADE
    -- `product_code`는 다형 참조이므로 FK 생략
);

CREATE TABLE `Watch` (
                         `watchlist_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '위시 아이디',
                         `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                         `user_id` VARCHAR(255) NOT NULL COMMENT '아이디',
                         `product_category` ENUM('deposit', 'fund', 'etf') NOT NULL COMMENT '카테고리',
                         PRIMARY KEY (`watchlist_id`),
                         FOREIGN KEY (`user_id`) REFERENCES `User`(`user_id`) ON DELETE CASCADE
    -- `product_code` 역시 다형 참조라 FK 생략
);


CREATE TABLE `D_Option` (
                            `d_option_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                            `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                            `d_option_dcls_month` VARCHAR(10) COMMENT '공시 제출일',
                            `d_option_fin_co_no` VARCHAR(15) COMMENT '금융 회사 코드',
                            `d_option_intr_rate_type` VARCHAR(5) COMMENT '저축 금리 유형',
                            `d_option_intr_rate_type_nm` VARCHAR(20) COMMENT '저축 금리 유형명',
                            `d_option_save_trm` VARCHAR(2) COMMENT '저축 기간',
                            `d_option_intr_rate` DECIMAL(4,2) COMMENT '기본 금리',
                            `d_option_intr_rate2` DECIMAL(4,2) COMMENT '최고 금리',
                            FOREIGN KEY (`product_code`) REFERENCES `Deposit`(`product_code`) ON DELETE CASCADE
);

CREATE TABLE `F_Asset_Allocation` (
                                      `f_asset_allocation_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                                      `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                      `f_asset_allocation_type` VARCHAR(20) COMMENT '자산 유형',
                                      `f_asset_allocation_per` DECIMAL(5,2) COMMENT '비중',
                                      `f_asset_allocation_valuation` DECIMAL(7,2) COMMENT '평가액',
                                      FOREIGN KEY (`product_code`) REFERENCES `Fund`(`product_code`) ON DELETE CASCADE
);

CREATE TABLE `F_Stock_Holdings` (
                                    `f_stock_holdings_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                                    `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                    `f_stock_holdings_name` VARCHAR(255) COMMENT '종목명',
                                    `f_stock_holdings_country` VARCHAR(50) COMMENT '국가',
                                    `f_stock_holdings_per` DECIMAL(5,2) COMMENT '비중',
                                    FOREIGN KEY (`product_code`) REFERENCES `Fund`(`product_code`) ON DELETE CASCADE
);

CREATE TABLE `E_Asset_Allocation` (
                                      `e_asset_allocation_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                                      `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                      `e_asset_allocation_type` VARCHAR(20) COMMENT '자산 유형',
                                      `e_asset_allocation_per` DECIMAL(5,2) COMMENT '비중',
                                      `e_asset_allocation_valuation` DECIMAL(7,2) COMMENT '평가액',
                                      FOREIGN KEY (`product_code`) REFERENCES `Etf`(`product_code`) ON DELETE CASCADE
);

CREATE TABLE `E_Equity_Ratio` (
                                  `e_equity_ratio_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                                  `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                  `e_equity_ratio_name` VARCHAR(255) COMMENT '종목명',
                                  `e_equity_ratio_country` VARCHAR(50) COMMENT '국가',
                                  `e_equity_ratio_per` DECIMAL(5,2) COMMENT '비중',
                                  FOREIGN KEY (`product_code`) REFERENCES `Etf`(`product_code`) ON DELETE CASCADE
);

CREATE TABLE `E_Constituent_Stocks` (
                                        `e_constituent_stocks_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '기본키(AUTO INCREMENT)',
                                        `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                        `e_constituent_stocks_name` VARCHAR(255) COMMENT '종목명',
                                        `e_constituent_per` DECIMAL(5,2) COMMENT '비중',
                                        FOREIGN KEY (`product_code`) REFERENCES `Etf`(`product_code`) ON DELETE CASCADE
);




-- Spring Batch 관련
DROP TABLE IF EXISTS BATCH_STEP_EXECUTION_CONTEXT;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION_CONTEXT;
DROP TABLE IF EXISTS BATCH_STEP_EXECUTION;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION_PARAMS;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION;
DROP TABLE IF EXISTS BATCH_JOB_INSTANCE;

DROP TABLE IF EXISTS BATCH_STEP_EXECUTION_SEQ;
DROP TABLE IF EXISTS BATCH_JOB_EXECUTION_SEQ;
DROP TABLE IF EXISTS BATCH_JOB_SEQ;


CREATE TABLE BATCH_JOB_INSTANCE  (
                                     JOB_INSTANCE_ID BIGINT  NOT NULL PRIMARY KEY ,
                                     VERSION BIGINT ,
                                     JOB_NAME VARCHAR(100) NOT NULL,
                                     JOB_KEY VARCHAR(32) NOT NULL,
                                     constraint JOB_INST_UN unique (JOB_NAME, JOB_KEY)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION  (
                                      JOB_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                      VERSION BIGINT  ,
                                      JOB_INSTANCE_ID BIGINT NOT NULL,
                                      CREATE_TIME DATETIME(6) NOT NULL,
                                      START_TIME DATETIME(6) DEFAULT NULL ,
                                      END_TIME DATETIME(6) DEFAULT NULL ,
                                      STATUS VARCHAR(10) ,
                                      EXIT_CODE VARCHAR(2500) ,
                                      EXIT_MESSAGE VARCHAR(2500) ,
                                      LAST_UPDATED DATETIME(6),
                                      JOB_CONFIGURATION_LOCATION VARCHAR(2500) NULL,
                                      constraint JOB_INST_EXEC_FK foreign key (JOB_INSTANCE_ID)
                                          references BATCH_JOB_INSTANCE(JOB_INSTANCE_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_PARAMS  (
                                             JOB_EXECUTION_ID BIGINT NOT NULL ,
                                             TYPE_CD VARCHAR(6) NOT NULL ,
                                             KEY_NAME VARCHAR(100) NOT NULL ,
                                             STRING_VAL VARCHAR(250) ,
                                             DATE_VAL DATETIME(6) DEFAULT NULL ,
                                             LONG_VAL BIGINT ,
                                             DOUBLE_VAL DOUBLE PRECISION ,
                                             IDENTIFYING CHAR(1) NOT NULL ,
                                             constraint JOB_EXEC_PARAMS_FK foreign key (JOB_EXECUTION_ID)
                                                 references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION  (
                                       STEP_EXECUTION_ID BIGINT  NOT NULL PRIMARY KEY ,
                                       VERSION BIGINT NOT NULL,
                                       STEP_NAME VARCHAR(100) NOT NULL,
                                       JOB_EXECUTION_ID BIGINT NOT NULL,
                                       START_TIME DATETIME(6) NOT NULL ,
                                       END_TIME DATETIME(6) DEFAULT NULL ,
                                       STATUS VARCHAR(10) ,
                                       COMMIT_COUNT BIGINT ,
                                       READ_COUNT BIGINT ,
                                       FILTER_COUNT BIGINT ,
                                       WRITE_COUNT BIGINT ,
                                       READ_SKIP_COUNT BIGINT ,
                                       WRITE_SKIP_COUNT BIGINT ,
                                       PROCESS_SKIP_COUNT BIGINT ,
                                       ROLLBACK_COUNT BIGINT ,
                                       EXIT_CODE VARCHAR(2500) ,
                                       EXIT_MESSAGE VARCHAR(2500) ,
                                       LAST_UPDATED DATETIME(6),
                                       constraint JOB_EXEC_STEP_FK foreign key (JOB_EXECUTION_ID)
                                           references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_CONTEXT  (
                                               STEP_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                               SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                               SERIALIZED_CONTEXT TEXT ,
                                               constraint STEP_EXEC_CTX_FK foreign key (STEP_EXECUTION_ID)
                                                   references BATCH_STEP_EXECUTION(STEP_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_JOB_EXECUTION_CONTEXT  (
                                              JOB_EXECUTION_ID BIGINT NOT NULL PRIMARY KEY,
                                              SHORT_CONTEXT VARCHAR(2500) NOT NULL,
                                              SERIALIZED_CONTEXT TEXT ,
                                              constraint JOB_EXEC_CTX_FK foreign key (JOB_EXECUTION_ID)
                                                  references BATCH_JOB_EXECUTION(JOB_EXECUTION_ID)
) ENGINE=InnoDB;

CREATE TABLE BATCH_STEP_EXECUTION_SEQ (
                                          ID BIGINT NOT NULL,
                                          UNIQUE_KEY CHAR(1) NOT NULL,
                                          constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_STEP_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_STEP_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_EXECUTION_SEQ (
                                         ID BIGINT NOT NULL,
                                         UNIQUE_KEY CHAR(1) NOT NULL,
                                         constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_EXECUTION_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_EXECUTION_SEQ);

CREATE TABLE BATCH_JOB_SEQ (
                               ID BIGINT NOT NULL,
                               UNIQUE_KEY CHAR(1) NOT NULL,
                               constraint UNIQUE_KEY_UN unique (UNIQUE_KEY)
) ENGINE=InnoDB;

INSERT INTO BATCH_JOB_SEQ (ID, UNIQUE_KEY) select * from (select 0 as ID, '0' as UNIQUE_KEY) as tmp where not exists(select * from BATCH_JOB_SEQ);
