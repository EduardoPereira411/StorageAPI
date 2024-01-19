package com.spring.storage.FileManagement;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String fileDirectory;

    public String getUploadDir() {
        return fileDirectory;
    }

    public void setUploadDir(final String uploadDir) {
        this.fileDirectory = uploadDir;
    }
}
