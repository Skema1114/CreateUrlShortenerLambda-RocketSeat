package com.rocketseat.createUrlShortnerLambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Main implements RequestHandler<Map<String, Object>, Map<String, Object>> {

	private final ObjectMapper objectMapper = new ObjectMapper();

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
		String shortUrlCode = UUID.randomUUID().toString().substring(0, 8);

		Map<String, Object> response = new HashMap<>();
		response.put("code", shortUrlCode);

		return response;
	}
}