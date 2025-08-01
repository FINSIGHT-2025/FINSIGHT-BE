package com.finsight.backend.config;

import com.finsight.backend.mapper.KeywordProductMapper;
import com.finsight.backend.mapper.NewsKeywordMapper;
import com.finsight.backend.recommend.NewsClickRecommender;
import com.finsight.backend.service.UserViewLogger;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {
        "com.finsight.backend.service",
        "com.finsight.backend.security",
        "com.finsight.backend.util",
        "com.finsight.backend.recommend",
        "com.finsight.backend.mongo",
        "com.finsight.backend.batch"
})
@PropertySource({"classpath:/application.properties"})
@EnableScheduling
//지정된 패키지 내의 모든 매퍼 인터페이스를 자동으로 스캔하고, MyBatis의 매퍼로 등록
@MapperScan(basePackages = {"com.finsight.backend.mapper"} )
@EnableTransactionManagement //<tx:annotation-driven transaction-manager="transactionManager"/>
public class RootConfig {
    @Value("${jdbc.driver}")
    String driver;
    @Value("${jdbc.url}")
    String url;
    @Value("${jdbc.username}")
    String username;
    @Value("${jdbc.password}")
    String password;

    @Value("${spring.mail.host}")
    private String mailHost;

    @Value("${spring.mail.port}")
    private int mailPort;

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String mailSmtpAuth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String mailSmtpStarttlsEnable;

    @Autowired
    ApplicationContext applicationContext;

    @Bean
    public DataSource getHikariDataSource() {
        HikariConfig hikariConfig = new HikariConfig();
        // HikariCP의 기본 설정을 적용
        hikariConfig.setDriverClassName(driver);
        hikariConfig.setJdbcUrl(url);  // 데이터베이스 URL
        hikariConfig.setUsername(username);  // 사용자명
        hikariConfig.setPassword(password); // 비밀번호

        // 추가적인 HikariCP 설정 가능 (예: 최대 커넥션 풀 크기, 커넥션 타임아웃 등)
//        hikariConfig.setMaximumPoolSize(10); // 최대 10개의 커넥션
        hikariConfig.setMinimumIdle(2);
        hikariConfig.setConnectionTimeout(30000); // 30초 동안 커넥션이 없으면 타임아웃

        // HikariCP 데이터소스 생성
        return new HikariDataSource(hikariConfig);
    }



    @Bean
    public SqlSessionFactory sqlSessionFactory1() throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(getHikariDataSource());

        sessionFactory.setConfigLocation(applicationContext.getResource("classpath:/mybatis-config.xml"));

        return sessionFactory.getObject();
    }


    /**
     * SqlSessionFactory를 직접사용하면 트랜잭션 관리를 직접 구현해야 합니다.
     * SqlSessionTemplate를 사용하면 SqlSessionFactory를 사용하여 생성된 SqlSession을 래핑하며, 트랜잭션 관리와 예외 처리를 자동으로 처리합니다.
     *
     * @param sqlSessionFactory
     * @return
     */
    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager manager = new DataSourceTransactionManager(getHikariDataSource());
        return manager;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailHost);
        mailSender.setPort(mailPort);
        mailSender.setUsername(mailUsername);
        mailSender.setPassword(mailPassword);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", mailSmtpAuth);
        props.put("mail.smtp.starttls.enable", mailSmtpStarttlsEnable);
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public NewsClickRecommender newsClickRecommender(
            MongoTemplate mongoTemplate,
            NewsKeywordMapper newsKeywordMapper,
            KeywordProductMapper keywordProductMapper
    ) {
        return new NewsClickRecommender(mongoTemplate, newsKeywordMapper, keywordProductMapper);
    }

    @Bean
    public UserViewLogger userViewLogger(MongoTemplate mongoTemplate) {
        return new UserViewLogger(mongoTemplate);
    }
}
