package com.spring.storage.FileManagement;

import com.spring.storage.Exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path filePathLocation;

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties) {
        this.filePathLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(filePathLocation);
        } catch (final Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFile(final String prefix, final MultipartFile file) {
        final String fileName = prefix + "_" + determineFileName(file);

        // Copy file to the target location (Replacing existing file with the same name)
        try {
            final Path targetLocation = filePathLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (final IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private String determineFileName(final MultipartFile file) {
        return UUID.randomUUID().toString() + "." + getExtension(file.getOriginalFilename()).orElse("");
    }

    public Optional<String> getExtension(final String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public Resource loadFileAsResource(final String fileName) {
        try {
            final Path filePath = filePathLocation.resolve(fileName).normalize();
            final Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new NotFoundException("File not found " + fileName);
        } catch (final MalformedURLException ex) {
            throw new NotFoundException("File not found " + fileName, ex);
        }
    }

    public void deleteFile(final String fileName) {
        try {
            final Path filePath = filePathLocation.resolve(fileName).normalize();
            Files.deleteIfExists(filePath);
        } catch (final IOException ex) {
            throw new FileStorageException("Could not delete file " + fileName + ". Please try again!", ex);
        }
    }
}
