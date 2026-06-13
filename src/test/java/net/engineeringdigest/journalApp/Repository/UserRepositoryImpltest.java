package net.engineeringdigest.journalApp.Repository;

import net.engineeringdigest.journalApp.repository.UserRepositoryImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepositoryImpltest {

    @Autowired
    private UserRepositoryImpl userRepositoryImpl;

    @Test
    public void getUserForSATest(){
        Assertions.assertNotNull(userRepositoryImpl.getUserForSA());
    }
}
