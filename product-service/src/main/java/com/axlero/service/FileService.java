package com.axlero.service;

import com.axlero.payload.FileData;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    public FileData uploadFile(MultipartFile file, String path) throws IOException;

    void deletePath(String path);

    Resource loadFile(String path);
}
