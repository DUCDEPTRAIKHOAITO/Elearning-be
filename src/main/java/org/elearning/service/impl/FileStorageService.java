package org.elearning.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    @Value("${app.file.storage-location}")
    private String storageLocation;

    private Path storagePath;

    @PostConstruct
    public void init() {
        // Chuyển storageLocation thành Path tuyệt đối và chuẩn hoá
        storagePath = Paths.get(storageLocation).toAbsolutePath().normalize();
        try {
            // Tạo thư mục nếu chưa tồn tại (cả các thư mục cha)
            Files.createDirectories(storagePath);
        } catch (IOException e) {
            throw new RuntimeException("Không thể tạo thư mục lưu file: " + storagePath, e);
        }
    }

    public String store(MultipartFile file) {
        // 1. Lấy tên file gốc & dọn dẹp đường dẫn
        String original = StringUtils.cleanPath(file.getOriginalFilename());

        // 2. Tách extension (nếu có)
        String extension = "";
        int dot = original.lastIndexOf('.');
        if (dot > 0) {
            extension = original.substring(dot);  // bao gồm dấu chấm
        }

        // 3. Sinh tên file mới duy nhất (UUID + extension)
        String filename = UUID.randomUUID().toString() + extension;

        try {
            // 4. Tạo Path đến file đích
            Path target = storagePath.resolve(filename);
            // 5. Copy dữ liệu từ MultipartFile vào file đích
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            // 6. Trả về tên file để controller dùng build URL
            return filename;
        } catch (IOException e) {
            throw new RuntimeException("Lưu file thất bại: " + filename, e);
        }
    }
}
