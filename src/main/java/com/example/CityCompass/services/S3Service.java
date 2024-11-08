package com.example.CityCompass.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Objects;

@Service
@Slf4j
public class S3Service {


    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Autowired
    private AmazonS3 s3Client;


    public String createFile(MultipartFile multipartFile){

        String pathName = null;

        if (multipartFile != null && !multipartFile.isEmpty()) {
            File fileObj = convertMultiPartFileToFile(multipartFile);
            pathName = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(bucketName,pathName,fileObj));
            fileObj.delete();
        }

        return pathName;
    }

    private File convertMultiPartFileToFile(MultipartFile imageFile) {
        File convertedFile = new File(Objects.requireNonNull(imageFile.getOriginalFilename()));
        try(FileOutputStream fos = new FileOutputStream(convertedFile)){
            fos.write(imageFile.getBytes());
        }
        catch (IOException e) {
            log.error("Error converting multipart file");
        }
        return convertedFile;
    }
    public void deleteFile(String fileName){
        if(fileName == null) return;
        s3Client.deleteObject(bucketName,fileName);
    }


    public String generatePresignedUrl(String fileName, int expirationInMinutes) {
        if(fileName == null) return null;
        // Set the expiration time for the URL
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (long) expirationInMinutes * 60 * 1000; // Convert minutes to milliseconds
        expiration.setTime(expTimeMillis);

        // Create the request for the pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, fileName)
                        .withMethod(com.amazonaws.HttpMethod.GET) // Change to PUT if uploading
                        .withExpiration(expiration);

        // Generate the pre-signed URL
        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }
}
