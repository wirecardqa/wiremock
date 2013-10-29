package com.github.tomakehurst.wiremock.client;

import com.github.tomakehurst.wiremock.capture.Capture;

public class CaptureBuilder {
	private Capture capture;

	public CaptureBuilder(Capture capture) {
		this.capture = capture;
	}
	
	public CaptureBuilder withPattern(String pattern) {
		capture.setPattern(pattern);
		return this;
	}
	
	public CaptureBuilder captureGroup(int captureGroup) {
		capture.setCaptureGroup(captureGroup);
		return this;
	}
	
	public String getTarget() {
		return capture.getTarget();
	}

	public void setTarget(String target) {
		capture.setTarget(target);
	}
	
	public Capture getCapture() {
		return capture;
	}
}
