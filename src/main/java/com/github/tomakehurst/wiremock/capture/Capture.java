package com.github.tomakehurst.wiremock.capture;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;
import com.github.tomakehurst.wiremock.http.Request;

@JsonSerialize(include=Inclusion.NON_NULL)
@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="source")
@JsonSubTypes({
    @JsonSubTypes.Type(value=UrlCapture.class, name="URL"),
    @JsonSubTypes.Type(value=HeaderCapture.class, name="HEADER"),
    @JsonSubTypes.Type(value=BodyCapture.class, name="BODY")
})
public abstract class Capture {
	private Pattern compiledPattern = null;

	private String target;
	private String pattern = "(.*)";
    private int captureGroup = 1;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getPattern() {
		return pattern;
	}
	
	public void setPattern(String pattern) {
		this.pattern = pattern;
		compiledPattern = Pattern.compile(pattern);
	}
	
	public int getCaptureGroup() {
		return captureGroup;
	}
	
	public void setCaptureGroup(int captureGroup) {
		this.captureGroup = captureGroup;
	}
	
	public abstract String capture(Request request);

	protected String capture(String value) {
		if (compiledPattern == null) {
			compiledPattern = Pattern.compile(pattern);
		}
		Matcher matcher = compiledPattern.matcher(value);
		if (matcher.find()) {
			return matcher.group(captureGroup);
		}
		return null;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		Capture other = (Capture) obj;
		if (target == null) {
			if (other.target != null) return false;
		} else {
			if (!target.equals(other.target)) return false;
		}
		if (pattern == null) {
			if (other.pattern != null) return false;
		} else {
			if (!pattern.equals(other.pattern)) return false;
		}
		if (captureGroup != other.captureGroup) return false;
		return true;
	}
}
