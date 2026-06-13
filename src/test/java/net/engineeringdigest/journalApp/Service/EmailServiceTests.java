package net.engineeringdigest.journalApp.Service;

import net.engineeringdigest.journalApp.service.EmailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class EmailServiceTests {

    @Autowired
    private EmailService emailService;

    @Value("${mailTo}")
    private String to;

    @Test
    public void sendMailTest(){
        emailService.sendEmail(to,
                "Testing Java mail sender",
                "Hi, How are you? Test successfull"
        );

    }
}
