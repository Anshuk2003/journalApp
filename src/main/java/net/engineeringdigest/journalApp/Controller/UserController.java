package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.externalAPIRespone.WeatherAPIResponse;
import net.engineeringdigest.journalApp.service.UserService;
import net.engineeringdigest.journalApp.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User userInDB=userService.findByUserName(userName);
        userInDB.setUsername(user.getUsername());
        userInDB.setPassword(user.getPassword());
        userService.saveNewUser(userInDB);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        userService.deleteByName(authentication.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    public ResponseEntity<?> greetings(){
        Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
        WeatherAPIResponse weatherAPIResponse=weatherService.getWeather("Pune");
        String greeting="";
        if(weatherAPIResponse!=null){
            greeting=", Weather feels like "+weatherAPIResponse.getCurrent().getFeelslike();
        }
        return new ResponseEntity<>("Hi"+ authentication.getName()+ greeting,HttpStatus.OK);
    }
}
