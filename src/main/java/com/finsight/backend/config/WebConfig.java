package com.finsight.backend.config;

import com.finsight.backend.listener.MySQLCleanupListener;
import com.finsight.backend.security.config.SecurityConfig;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;

/**
 * web.xml문서대신
 */
public class WebConfig extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);

        // 여기서 리스너 수동 등록
        servletContext.addListener(new MySQLCleanupListener());
    }
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
                CorsConfig.class,
                RootConfig.class,
                WebSocketConfig.class,
                SecurityConfig.class,
                BatchConfig.class,
                WebClientConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { ServletConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }

    @Override
    protected Filter[] getServletFilters() {
        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        DelegatingFilterProxy  securityFilterChain = new DelegatingFilterProxy("springSecurityFilterChain");

        return new Filter[] { characterEncodingFilter, securityFilterChain };
    }
   final String LOCATION = "c:/upload";
    final long MAX_FILE_SIZE =1024 * 10;//1024 * 1024 * 10L; //
    final long MAX_REQUEST_SIZE =  1024 * 1024 * 20L;
    final int FILE_SIZE_THRESHOLD = 1024 * 1024 * 5;;

    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setInitParameter("throwExceptionIfNoHandlerFound", "true");
        MultipartConfigElement multipartConfig =
                new MultipartConfigElement(
                        LOCATION,   // 업로드 처리 디렉토리 경로
                        MAX_FILE_SIZE,	// 업로드 가능한 파일 하나의 최대 크기
                        MAX_REQUEST_SIZE,	// 업로드 가능한 전체 최대 크기(여러 파일 업로드 하는 경우)
                        FILE_SIZE_THRESHOLD		// 메모리 파일의 최대 크기(이보다 작으면 실제 메모리에서만 작업)
                );
        registration.setMultipartConfig(multipartConfig);
    }

}