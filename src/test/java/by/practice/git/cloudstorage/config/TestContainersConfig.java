package by.practice.git.cloudstorage.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.FixedHostPortGenericContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class TestContainersConfig {
    @Bean
    @ServiceConnection
    @SuppressWarnings("resource")
    public PostgreSQLContainer<?> postgreSQLContainer() {
        return new PostgreSQLContainer<>("postgres")
                .withDatabaseName("cloud_storage_test")
                .withUsername("test_user")
                .withPassword("test_password");

    }

//    @Bean
//    @ServiceConnection
//    public MinIOContainer minioContainer() {
//        MinIOContainer container = new MinIOContainer("test-minio");
//        container.withUserName("testaccesskey");
//        container.withPassword("testsecretkey");
//        container.withCommand("server /data --console-address :9090");
//        container.withExposedPorts(9000, 9000);
//        container.start();
//        return container;
//    }



//    @Bean
//    public GenericContainer<?> minioContainer() {
//        GenericContainer<?> container = new FixedHostPortGenericContainer<>("minio/minio")
//                .withCommand("server /data --console-address :9090")
//                .withEnv("MINIO_ROOT_USER", "testaccesskey")
//                .withEnv("MINIO_ROOT_PASSWORD", "testsecretkey")
//                .withFixedExposedPort(9000, 9000)
//                .withFixedExposedPort(9090, 9090);
//        container.start();
//        return container;
//    }
}
