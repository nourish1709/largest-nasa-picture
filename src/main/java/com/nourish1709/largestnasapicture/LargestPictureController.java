package com.nourish1709.largestnasapicture;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequiredArgsConstructor
public class LargestPictureController {

    private final LargestPictureService largestPictureService;

    @GetMapping("/pictures/{sol}/largest")
    @SneakyThrows
    public ResponseEntity<Void> findLargestPhoto(@PathVariable int sol) {
        final String largestPictureLocation = largestPictureService.findLargestPictureLocation(sol);
        return ResponseEntity.status(HttpStatus.PERMANENT_REDIRECT)
                .location(URI.create(largestPictureLocation))
                .build();
    }
}
