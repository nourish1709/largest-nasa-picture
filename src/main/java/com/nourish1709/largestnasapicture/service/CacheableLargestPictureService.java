package com.nourish1709.largestnasapicture.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Comparator;
import java.util.List;

@Service("cacheableLargestPictureService")
@Primary
@RequiredArgsConstructor
public class CacheableLargestPictureService implements LargestPictureService {

    private final RestTemplate restTemplate;

    @Override
    @Cacheable("largestPictureSource")
    public String findLargestPictureLocation(int sol) {
        final ResponseEntity<JsonNode> json = restTemplate.getForEntity(UriComponentsBuilder.fromHttpUrl(BASE_LINK)
                .queryParam("api_key", "DEMO_KEY")
                .queryParam("sol", sol)
                .toUriString(), JsonNode.class);
        final List<String> imgSources = json.getBody()
                .get("photos").findValuesAsText("img_src");
        return imgSources.parallelStream()
                .max(Comparator.comparingLong(imgSrc ->
                        restTemplate.headForHeaders(imgSrc).getContentLength()))
                .orElseThrow(RuntimeException::new);
    }

    @CacheEvict("largestPictureSource")
    @Scheduled(cron = "0 0 0 * * *")
    public void cacheEvict() {

    }
}
