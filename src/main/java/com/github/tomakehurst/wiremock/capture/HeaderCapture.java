package com.github.tomakehurst.wiremock.capture;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.github.tomakehurst.wiremock.http.Request;

@JsonSerialize(include=Inclusion.NON_NULL)
public class HeaderCapture extends Capture {
	private String key;

	public HeaderCapture(String key) {
		this.key = key;
	}
	
	public HeaderCapture() { }
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Override
	public String capture(Request request) {
		return capture(request.getHeader(key));
	}
	
	@Override
	public boolean hasEssentialData() {
	    if (key == null || key.isEmpty()) return false;
	    return super.hasEssentialData();
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		HeaderCapture other = (HeaderCapture) obj;
		if (key == null) {
			if (other.key != null) return false;
		} else {
			if (!key.equals(other.key)) return false;
		}
		return super.equals(obj);
	}
}
