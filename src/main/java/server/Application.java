package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        RequestProcessor.startAlgorithm();
        SpringApplication.run(Application.class, args);
    }
}