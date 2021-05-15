package ca.sheridancollege.waamande.geocoder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Component;

@Component
public class Geocoder {
	
	private static final String API_KEY = "AIzaSyDh6LjxqvunmwAJv97vHOY2F5BhQpmPnqc";
	
	public String FindPlace(String query) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		
		query = query.replaceAll(" ", "%20");
		query = query.replaceAll(",", "%2C");
		
		String requestUri = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" + "input=" + query + "&inputtype=textquery&fields=formatted_address,name,geometry&key=" + API_KEY;
		HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri)).timeout(Duration.ofMillis(2000)).build();
		HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
		return geocodingResponse.body();
	}
	
	public String NearbySearch(String lat, String lng, String specialty) throws IOException, InterruptedException {
		HttpClient httpClient = HttpClient.newHttpClient();
		
		specialty = specialty.replaceAll(" ", "%20");
		specialty = specialty.replaceAll("/", "%2F");
		specialty = specialty.replaceAll("&", "%26");

		String requestUri = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" + "location=" + lat + "," + lng + "&radius=50000&type=doctor&keyword=" + specialty + "&key=" + API_KEY;
		HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri)).timeout(Duration.ofMillis(2000)).build();
		HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest, HttpResponse.BodyHandlers.ofString());
		return geocodingResponse.body();
	}
	
}
