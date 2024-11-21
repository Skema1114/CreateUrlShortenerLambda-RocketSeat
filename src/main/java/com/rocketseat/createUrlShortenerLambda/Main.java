package com.rocketseat.createUrlShortenerLambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final S3Client s3Client = S3Client.builder().build();

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

		String body = input.get("body").toString();

		Map<String, String> bodyMap;
		try {
			bodyMap = objectMapper.readValue(body, Map.class);
		} catch (Exception exception) {
			throw new RuntimeException("Error parsing JSON body: " + exception.getMessage(), exception);
		}

		String originalUrl = bodyMap.get("originalUrl");
		String expirationTime = bodyMap.get("expirationTime");
		long expirationTimeInSeconds = Long.parseLong(expirationTime);

		String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

		UrlData urlData = new UrlData(originalUrl, expirationTimeInSeconds);

		try {
			String urlDataJson = objectMapper.writeValueAsString(urlData);

			PutObjectRequest requestObj = PutObjectRequest.builder()
					.bucket("url-shortener-storage-rocketseat-lambda-bucket")
					.key(shortUrlCode + ".json")
					.build();

			s3Client.putObject(requestObj, RequestBody.fromString(urlDataJson));
		} catch (Exception exception) {
			throw new RuntimeException("Error saving data to S3: " + exception.getMessage(), exception);
		}

		Map<String, Object> response = new HashMap<>();
		response.put("code", shortUrlCode);

		return response;
	}
}