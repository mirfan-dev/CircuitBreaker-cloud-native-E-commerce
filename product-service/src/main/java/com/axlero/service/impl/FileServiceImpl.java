package com.axlero.service.impl;

import com.axlero.exception.ResourceNotFoundException;
import com.axlero.payload.FileData;
import com.axlero.service.FileService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public FileData uploadFile(MultipartFile file, String path) throws IOException {

        if (file.isEmpty()) {
            throw new ResourceNotFoundException("Image file is empty");
        }

        List<String> fileType = List.of("image/jpeg", "image/png", "image/jpg");
        if (!fileType.contains(file.getContentType())) {
            throw new ResourceNotFoundException("Only jpeg, png and jpg are allowed");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new ResourceNotFoundException("Invalid file name");
        }

        String extension = originalName.substring(originalName.lastIndexOf(".") + 1).toLowerCase();
        List<String> fileExtension = List.of("jpeg", "png", "jpg");
        if (!fileExtension.contains(extension)) {
            throw new ResourceNotFoundException("Invalid image extension");
        }

        Path filePath = Paths.get(path);
        Path folderPath = filePath.getParent();

        logger.info("Upload directory: {}", folderPath);

        if (folderPath != null && !Files.exists(folderPath)) {
            Files.createDirectories(folderPath);
        }

        String fileName = System.currentTimeMillis() + "." + extension;
        Path targetPath = (folderPath != null) ? folderPath.resolve(fileName) : Paths.get(fileName);

        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return new FileData(fileName, targetPath.toString());
    }

    @Override
    public void deletePath(String path) {
        try {
            Path filePath = Paths.get(path);
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                logger.info("File deleted successfully: {}", path);
            } else {
                logger.warn("File not found for deletion: {}", path);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file: " + path, e);
        }
    }



    @Override
    public Resource loadFile(String path) {
        return null;
    }
}
