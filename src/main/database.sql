DROP TABLE IF EXISTS `Watch`;
DROP TABLE IF EXISTS `News_Product`;
DROP TABLE IF EXISTS `News_Keyword`;
DROP TABLE IF EXISTS `Keyword`;
DROP TABLE IF EXISTS `News`;
DROP TABLE IF EXISTS `History`;
DROP TABLE IF EXISTS `Holdings`;
DROP TABLE IF EXISTS `Investment_Profile`;
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

CREATE TABLE `Holdings`
(
    `holdings_id`             BIGINT                                            NOT NULL AUTO_INCREMENT COMMENT '기본키(AUTO INCREMENT)',
    `user_id`                 VARCHAR(255)                                      NOT NULL COMMENT '아이디',
    `product_code`            VARCHAR(100)                                      NOT NULL COMMENT '상품 코드',
    `product_category`        ENUM ('deposit', 'fund', 'etf')                   NOT NULL COMMENT '카테고리',
    `holdings_total_price`    DECIMAL(15, 2)                                    NOT NULL COMMENT '총 투자금액',
    `holdings_total_quantity` INT                                               NOT NULL COMMENT '총 투자수량',
    `holdings_status`         ENUM ('holding', 'zero', 'terminated', 'expired') NOT NULL COMMENT '상태',
    PRIMARY KEY (`holdings_id`),
    FOREIGN KEY (`user_id`) REFERENCES `User` (`user_id`) ON DELETE CASCADE
    -- `product_code`는 다형 참조라 FK 불가 (예금/펀드/ETF 모두를 참조하므로)
);


CREATE TABLE `Investment_Profile` (
                                      `user_id` VARCHAR(255) NOT NULL COMMENT '아이디',
                                      `investment_profile_type` ENUM('stable', 'stableplus', 'neutral', 'aggressive', 'veryaggressive') NULL COMMENT '위험 성향',
                                      `investment_profile_updated_at` DATETIME NULL COMMENT '갱신일자',
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
                        `fund_asset_allocation` TEXT NULL COMMENT '자산 비중 구성',
                        `fund_stock_holdings` TEXT NULL COMMENT '주식보유비중_종목별',
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
                       `etf_asset_allocation` TEXT NULL COMMENT '자산 비중 구성',
                       `etf_equity_ratio` TEXT NULL COMMENT '주식 보유 비중',
                       `etf_constituent_stocks` TEXT NULL COMMENT '구성 종목',
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
                           `deposit_option` TEXT NULL COMMENT '옵션',
                           PRIMARY KEY (`product_code`)
);

CREATE TABLE `History` (
                           `history_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '기본키',
                           `holdings_id` BIGINT NOT NULL COMMENT '외래키',
                           `history_trade_type` ENUM('buy', 'sell', 'deposit') NOT NULL COMMENT '거래 유형',
                           `history_trade_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '거래일',
                           `history_quantity` INT NOT NULL DEFAULT 1 COMMENT '투자수량',
                           `history_amount` BIGINT NOT NULL COMMENT '투자금액',
                           PRIMARY KEY (`history_id`, `holdings_id`),
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
                        PRIMARY KEY (`news_id`)
);

CREATE TABLE `Keyword` (
                           `keyword_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '키워드 ID',
                           `keyword` VARCHAR(255) NOT NULL COMMENT '키워드',
                           PRIMARY KEY (`keyword_id`)
);

CREATE TABLE `News_Keyword` (
                                `news_keyword_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'KEY(AUTO INCREMENT)',
                                `news_id` VARCHAR(255) NOT NULL COMMENT '뉴스 ID',
                                `keyword_id` BIGINT NOT NULL COMMENT '키워드 ID',
                                PRIMARY KEY (`news_keyword_id`),
                                FOREIGN KEY (`news_id`) REFERENCES `News`(`news_id`) ON DELETE CASCADE,
                                FOREIGN KEY (`keyword_id`) REFERENCES `Keyword`(`keyword_id`) ON DELETE CASCADE
);

CREATE TABLE `News_Product` (
                                `news_product_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'KEY(AUTO INCREMENT)',
                                `product_code` VARCHAR(100) NOT NULL COMMENT '상품 코드',
                                `news_id` VARCHAR(255) NOT NULL COMMENT '뉴스 ID',
                                PRIMARY KEY (`news_product_id`),
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

SELECT * FROM User;