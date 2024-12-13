package vttp.batch5.ssf.noticeboard.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vttp.batch5.ssf.noticeboard.services.NoticeService;

@RestController
@RequestMapping
public class HealthController {

    private final NoticeService noticeService;
    

    public HealthController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }


    @GetMapping("/status2")
    public ResponseEntity<Void> getStatus() {
        
        //if healthy can return a key from redis db
        // will give a 200 ok status code
        // if unhealthy cannot get a key from redis db
        //will gibe a 503 status code

        Optional<String> randomKey = noticeService.retrieveRandomKey();

        if (randomKey.isEmpty()) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        }

        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/status")
    public ResponseEntity<Void> getStatus2() {

        try {

            noticeService.getRandomKey();
            return ResponseEntity.status(HttpStatus.OK).build();


        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();

        }
    }
    
}
