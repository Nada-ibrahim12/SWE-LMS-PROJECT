package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })

@EnableAdminServer
public class LmsApplication {

//    @Autowired
//    private EmailSenderService emailSenderService;

    public static void main(String[] args) {
        SpringApplication.run(LmsApplication.class, args);
    }

//    @EventListener(ApplicationReadyEvent.class)
//    public void sendEmail() {
//        emailSenderService.sendSimpleMail("ahmed.hegazy1092004.ah@gmail.com",
//                                        "Testtttt",
//                                        "sh8aaalll??");
//    }
}