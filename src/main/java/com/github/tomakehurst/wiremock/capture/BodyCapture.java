package com.github.tomakehurst.wiremock.capture;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.github.tomakehurst.wiremock.http.Request;

@JsonSerialize(include=Inclusion.NON_NULL)
public class BodyCapture extends Capture {
	@Override
	public String capture(Request request) {
		return capture(request.getBodyAsString());
	}
}
