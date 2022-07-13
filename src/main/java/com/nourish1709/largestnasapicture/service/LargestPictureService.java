package com.nourish1709.largestnasapicture.service;

public interface LargestPictureService {

    String BASE_LINK =
            "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos";
    String LINK =
            "https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?sol=%d&api_key=DEMO_KEY";

    String findLargestPictureLocation(int sol);
}
