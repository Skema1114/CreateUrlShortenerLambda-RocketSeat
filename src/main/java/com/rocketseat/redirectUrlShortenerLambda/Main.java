package com.rocketseat.redirectUrlShortenerLambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocketseat.shared.models.UrlData;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static com.rocketseat.shared.configs.Configs.BUCKET_NAME;
import static com.rocketseat.shared.configs.Configs.BUCKET_TYPE;

public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	private final ObjectMapper objectMapper = new ObjectMapper();
	private final S3Client s3Client = S3Client.builder().build();

	@Override
	public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {

		String pathParameters = (String) input.get("rawPath");
		String shortUrlCode = pathParameters.replace("/", "");

		if (shortUrlCode == null || shortUrlCode.isEmpty()) {
			throw new IllegalArgumentException("Invalid input: 'shortUrlCode' is required.");
		}

		GetObjectRequest getObjectRequest = GetObjectRequest.builder()
				.bucket(BUCKET_NAME)
				.key(shortUrlCode + BUCKET_TYPE)
				.build();

		InputStream s3ObjectStream;

		try {
			s3ObjectStream = s3Client.getObject(getObjectRequest);
		} catch (Exception exception) {
			throw new RuntimeException("Error fetching URL data from S3: " + exception.getMessage(), exception);
		}

		UrlData urlData;

		try {
			urlData = objectMapper.readValue(s3ObjectStream, UrlData.class);
		} catch (Exception exception) {
			throw new RuntimeException("Error deserializing URL data from S3: " + exception.getMessage(), exception);
		}

		long currentTimeInSeconds = System.currentTimeMillis() / 1000;

		Map<String, Object> response = new HashMap<>();

		// URL has expired
		if (urlData.getExpirationTime() < currentTimeInSeconds) {
			response.put("statusCode", 410);
			response.put("body", "This URL has expired.");

			return response;
		}

		// URL is still valid
		response.put("statusCode", 302);

		Map<String, String> headers = new HashMap<>();
		headers.put("Location", urlData.getOriginalUrl());

		response.put("headers", headers);

		return response;
	}
}
