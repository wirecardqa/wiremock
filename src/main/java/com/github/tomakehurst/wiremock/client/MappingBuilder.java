/*
 * Copyright (C) 2011 Thomas Akehurst
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.tomakehurst.wiremock.client;

import java.util.ArrayList;
import java.util.List;

import com.github.tomakehurst.wiremock.capture.Capture;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import com.github.tomakehurst.wiremock.matching.RequestPattern;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;

public class MappingBuilder {
	
	private RequestPatternBuilder requestPatternBuilder;
	private ResponseDefinitionBuilder responseDefBuilder;
	private Integer priority;
	private String scenarioName;
	private String requiredScenarioState;
	private String newScenarioState;
	private List<CaptureBuilder> captureBuilders = null;
	private String delimiter1 = null;
	private String delimiter2 = null;
	
	public MappingBuilder(RequestMethod method, UrlMatchingStrategy urlMatchingStrategy) {
		requestPatternBuilder = new RequestPatternBuilder(method, urlMatchingStrategy);
	}

	public MappingBuilder willReturn(ResponseDefinitionBuilder responseDefBuilder) {
		this.responseDefBuilder = responseDefBuilder;
		return this;
	}
	
	public MappingBuilder atPriority(Integer priority) {
		this.priority = priority;
		return this;
	}
	
	public MappingBuilder withHeader(String key, ValueMatchingStrategy headerMatchingStrategy) {
		requestPatternBuilder.withHeader(key, headerMatchingStrategy);
		return this;
	}
	
	public MappingBuilder withRequestBody(ValueMatchingStrategy bodyMatchingStrategy) {
		requestPatternBuilder.withRequestBody(bodyMatchingStrategy);
		return this;
	}
	
	public MappingBuilder inScenario(String scenarioName) {
		this.scenarioName = scenarioName;
		return this;
	}
	
	public MappingBuilder whenScenarioStateIs(String stateName) {
		this.requiredScenarioState = stateName;
		return this;
	}
	
	public MappingBuilder willSetStateTo(String stateName) {
		this.newScenarioState = stateName;
		return this;
	}
	
	public MappingBuilder willCapture(String variableName, CaptureBuilder captureBuilder) {
		captureBuilder.setTarget(variableName);
		if (captureBuilders == null) {
			captureBuilders = new ArrayList<CaptureBuilder>();
		}
		captureBuilders.add(captureBuilder);
		return this;
	}
	
	public MappingBuilder withPlaceholderDelimiters(String delimiter1, String delimiter2) {
		this.delimiter1 = delimiter1;
		this.delimiter2 = delimiter2;
		return this;
	}
	
	public StubMapping build() {
		RequestPattern requestPattern = requestPatternBuilder.build();
		ResponseDefinition response = responseDefBuilder.build();
		StubMapping mapping = new StubMapping(requestPattern, response);
		mapping.setPriority(priority);
		mapping.setScenarioName(scenarioName);
		mapping.setRequiredScenarioState(requiredScenarioState);
		mapping.setNewScenarioState(newScenarioState);
		if (captureBuilders != null) {
			List<Capture> captures = new ArrayList<Capture>();
			for (CaptureBuilder captureBuilder : captureBuilders) {
				captures.add(captureBuilder.getCapture());
			}
			mapping.setCaptures(captures);
		}
		if (delimiter1 != null && delimiter2 != null) {
			List<String> delimiters = new ArrayList<String>();
			delimiters.add(delimiter1);
			delimiters.add(delimiter2);
			mapping.setPlaceHolderDelimiters(delimiters);
		}
		return mapping;
	}
}
