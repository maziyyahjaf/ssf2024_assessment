package vttp.batch5.ssf.noticeboard.services;

import java.io.StringReader;
import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.batch5.ssf.noticeboard.models.Notice;
import vttp.batch5.ssf.noticeboard.models.NoticeResponse;
import vttp.batch5.ssf.noticeboard.repositories.NoticeRepository;

@Service
public class NoticeService {

	RestTemplate restTemplate = new RestTemplate();

	@Value("${notice.submission.url}")
	private String noticeSubmissionUrl;

	private final NoticeRepository noticeRepository;

	public NoticeService(NoticeRepository noticeRepository) {
		this.noticeRepository = noticeRepository;
	}

	// TODO: Task 3
	// You can change the signature of this method by adding any number of
	// parameters
	// and return any type
	public NoticeResponse postToNoticeServer(Notice notice) throws Exception {

		NoticeResponse nResponse = new NoticeResponse();
		// build the json date from the Notice object
		// convert the Date to Long
		Long postDateLong = notice.getPostDate().getTime();

		// build the array for the categories -> maybe not?
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (String category : notice.getCategories()) {
			arrBuilder.add(category);
		}

		// List<String> categories = notice.getCategories();

		JsonObject jsonObject = Json.createObjectBuilder()
				.add("title", notice.getTitle())
				.add("poster", notice.getPoster())
				.add("postDate", postDateLong)
				.add("categories", arrBuilder.build())
				.add("text", notice.getText())
				.build();

		// build the request entity
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Accept", "application/json");

		RequestEntity<String> req = RequestEntity
				.post(URI.create(noticeSubmissionUrl))
				.headers(headers)
				.body(jsonObject.toString());

		try {

			ResponseEntity<String> response = restTemplate.exchange(req, String.class);
			System.out.println(response);

			if (response.getStatusCode().is2xxSuccessful()) {
				// call the repo method to save the response
				// parse the response using jsonReader
				String responseBody = response.getBody();
				JsonReader jsonReader = Json.createReader(new StringReader(responseBody));
				JsonObject root = jsonReader.readObject();
				String id = root.getString("id");
				Long timestamp = root.getJsonNumber("timestamp").longValue();

				// success notice response
				NoticeResponse noticeResponse = new NoticeResponse(id, timestamp);
				nResponse = noticeResponse;
				noticeRepository.insertNotices(noticeResponse);

			}
		} catch (HttpClientErrorException ex) {

			System.out.println("Response Status: " + ex.getStatusCode());
			System.out.println("Response Headers: " + ex.getResponseHeaders());
			System.out.println("Response Body: " + ex.getResponseBodyAsString());

			// failed notice response
			String errorMessage;

			String responseBody = ex.getResponseBodyAsString().trim();

			try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
				JsonObject errorResponse = jsonReader.readObject();

				errorMessage = errorResponse.getString("message");

			} catch (Exception parseEx) {
				errorMessage = "Error parsing the error response: " + parseEx.getMessage();

			}

			// handle specific 4xx errors
			if (ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
				throw new Exception("Invalid payload: " + errorMessage);
			} else if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
				throw new Exception(ex.getMessage());
			} else {
				throw new Exception("Notice upload failed: " + errorMessage, ex);
			}

		}
		return nResponse;

	}

	public Optional<String> retrieveRandomKey() {
		String randomKey = noticeRepository.retrieveRandomKey();
		if (randomKey == null) {
			return Optional.empty();
		}

		return Optional.of(randomKey);
	}

	public String getRandomKey() throws Exception {
		try {
			String randomKey = noticeRepository.retrieveRandomKey();
			return randomKey;

		} catch (Exception e) {
			throw new Exception();
		}
	}

}
