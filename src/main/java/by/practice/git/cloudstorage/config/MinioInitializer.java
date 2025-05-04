package by.practice.git.cloudstorage.config;

import by.practice.git.cloudstorage.exception.MinioBucketInitializationException;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class MinioInitializer {
    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    public MinioInitializer(MinioClient minioClient, MinioProperties minioProperties) {
        this.minioClient = minioClient;
        this.minioProperties = minioProperties;
    }

    @PostConstruct
    public void init() {
        String bucketName = minioProperties.getBucket();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }
        } catch (Exception ex) {
            throw new MinioBucketInitializationException();
        }
    }
}
