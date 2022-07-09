package com.nourish1709.largestnasapicture;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.LOCATION;

@Service
public class LargestPictureService {

    private static final String LINK =
            "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=%d&api_key=DEMO_KEY";

    public String findLargestPictureLocation(int sol) {
        record Photos(Collection<Photo> photos) {
            record Photo(@JsonProperty("img_src") String imgSrc) {
            }

        }

        final URI uri = UriComponentsBuilder.fromHttpUrl(LINK)
                .queryParam("api_key", "DEMO_KEY")
                .queryParam("sol", sol)
                .build()
                .toUri();
        final RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Photos> getPhotosResponse = restTemplate
                .getForEntity(uri, Photos.class);
        int statusCode = getPhotosResponse.getStatusCodeValue();

        while (statusCode >= 300) {
            final String redirectLocation = getPhotosResponse.getHeaders()
                    .getFirst(LOCATION);
            getPhotosResponse = restTemplate.getForEntity(
                    Objects.requireNonNull(redirectLocation), Photos.class);
            statusCode = getPhotosResponse.getStatusCodeValue();
        }

        final Optional<Photos.Photo> maxPhoto = Objects.requireNonNull(getPhotosResponse.getBody()).photos
                .parallelStream()
                .max(Comparator.comparingLong(photo -> restTemplate.headForHeaders(photo.imgSrc)
                        .getContentLength()));
        return maxPhoto.orElseThrow(RuntimeException::new)
                .imgSrc;

    }
}
