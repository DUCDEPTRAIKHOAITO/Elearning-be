package org.elearning.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Cấu hình để expose thư mục lưu ảnh (storageLocation) qua endpoint /files/**.
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${app.file.storage-location}")
    private String storageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Khi client GET /files/{filename}, Spring sẽ trả file ở storageLocation/{filename}
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + storageLocation + "/");
    }
}
