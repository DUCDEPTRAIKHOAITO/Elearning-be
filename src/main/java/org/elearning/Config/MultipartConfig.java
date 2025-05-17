package org.elearning.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

@Configuration
public class MultipartConfig {

    /**
     * Bean này cho phép Spring Boot nhận và giải mã multipart/form-data.
     * Nếu không có, RequestParam("file") sẽ không tìm thấy phần file.
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}