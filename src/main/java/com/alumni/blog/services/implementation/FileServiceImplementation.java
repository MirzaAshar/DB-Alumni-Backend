package com.alumni.blog.services.implementation;

import com.alumni.blog.services.FileService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
@Getter
@Setter
@Service
public class FileServiceImplementation implements FileService {

    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        // Get the original filename
        String name = file.getOriginalFilename();

        // Validate filename
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("File name is invalid!");
        }

        // Generate a random UUID and concatenate with the file extension
        String randomID = UUID.randomUUID().toString();
        String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));

        // Create the file path
        String filePath = path + File.separator + fileName1;

        // Ensure the directory exists
        File f = new File(path);
        if (!f.exists()) {
            f.mkdir();
        }

        // Copy the file to the target location
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Return the generated file name
        return fileName1;
    }


    @Override
    public InputStream getResource(String path, String fileName) throws FileNotFoundException {
      String fullPath=path+File.separator+fileName;
     return new FileInputStream(fullPath);
    }
}
