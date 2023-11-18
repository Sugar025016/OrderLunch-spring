package com.order_lunch.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GeocodingService {
    @Value("${YOUR_API_KEY}")
    String YOUR_API_KEY;
    private final RestTemplate restTemplate;

    public GeocodingService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Coordinates geocodeAddress(String address) {
        try {
            String apiUrl = String.format(
                    "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "%s&key=" + YOUR_API_KEY,
                    encodeURIComponent(address));

            GeocodingResponse response = restTemplate.getForObject(apiUrl, GeocodingResponse.class);

            if (response != null && response.getResults().length > 0) {
                Coordinates location = response.getResults()[0].getGeometry().getLocation();
                System.out.println(location.getLat());
                System.out.println(location.getLng());
                return location;
            } else {
                System.out.println("Address not found!");
            }
        } catch (Exception e) {
            System.err.println("Error geocoding address: " + e.getMessage());
        }

        return null;
    }

    // Your model classes for mapping the response
    private static class GeocodingResponse {
        private GeocodingResult[] results;

        public GeocodingResult[] getResults() {
            return results;
        }
    }

    private static class GeocodingResult {
        private Geometry geometry;

        public Geometry getGeometry() {
            return geometry;
        }
    }

    private static class Geometry {
        private Coordinates location;

        public Coordinates getLocation() {
            return location;
        }
    }

    private static class Coordinates {
        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    private String encodeURIComponent(String s) {
        try {
            return URLEncoder.encode(s, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Error encoding URI component", e);
        }
    }
}
