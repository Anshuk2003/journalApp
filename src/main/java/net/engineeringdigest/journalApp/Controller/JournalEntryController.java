package net.engineeringdigest.journalApp.Controller;

import net.engineeringdigest.journalApp.DTO.ApiResponse;
import net.engineeringdigest.journalApp.entity.JournalEntry;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.service.JournalEntryService;
import net.engineeringdigest.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
    User user  =userService.findByUserName(userName);
    List<JournalEntry> all= user.getJournalEntries();

    HttpHeaders headers=new HttpHeaders();
    headers.add("X-API-Version", "1.0");
    headers.add("X-Response-Time", String.valueOf(System.currentTimeMillis()));

    if(all!=null && !all.isEmpty()){

        ApiResponse<List<JournalEntry>> response = ApiResponse.<List<JournalEntry>>builder()
                .title("Journal Entries Retrieved")
                .message("Success")
                .status(HttpStatus.OK.value())
                .timestamp(LocalDateTime.now())
                .data(all)
                .build();

        return new ResponseEntity<>(response,headers,HttpStatus.OK);
    }
        ApiResponse<List<JournalEntry>> response = ApiResponse.<List<JournalEntry>>builder()
                .title("No Journal Entries Found")
                .message("No journal entries exist for the current user")
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .data(Collections.emptyList())
                .build();

        return new ResponseEntity<>(response, headers, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry){
       try {
           Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
           String userName=authentication.getName();
           journalEntryService.saveEntry(myEntry, userName);
           return new ResponseEntity<>(myEntry,HttpStatus.CREATED);
       }
       catch (Exception e){
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
       }
    }

    @GetMapping("/id/{myid}")
    public ResponseEntity<?> getJournalEntryById(@PathVariable ObjectId myid){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(myid)).collect(Collectors.toList());

        if(!collect.isEmpty()) {
            Optional<JournalEntry> Entry = journalEntryService.findById(myid);
            if (Entry.isPresent()) {
                return new ResponseEntity<>(Entry.get(), HttpStatus.OK);
            }
        }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/id/{myid}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId myid){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        boolean removed=journalEntryService.deleteById(myid,userName);
        if(removed) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/id/{id}")
    public  ResponseEntity<?> updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry newentry){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String userName=authentication.getName();
        User user=userService.findByUserName(userName);
        List<JournalEntry> collect=user.getJournalEntries().stream().filter(x->x.getId().equals(id)).collect(Collectors.toList());

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                JournalEntry old = journalEntry.get();
                old.setTitle(newentry.getTitle() != null && !newentry.getTitle().equals("") ? newentry.getTitle() : old.getTitle());
                old.setContent(newentry.getContent() != null && !newentry.getContent().equals("") ? newentry.getContent() : old.getContent());
                journalEntryService.saveEntry(old);
                return new ResponseEntity<>(old, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
