package com.github.tomakehurst.wiremock.capture;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

public class Replacer {
	private static final String DEFAULT_DELIMITER_1 = "${";
	private static final String DEFAULT_DELIMITER_2 = "}";
	
	private final String delimiter1;
	private final String delimiter2;
	private final ImmutableMap<String, String> variables;
	
	public Replacer(Map<String, String> variables) {
		this(variables, DEFAULT_DELIMITER_1, DEFAULT_DELIMITER_2);
	}
	
	public Replacer(Map<String, String> variables, List<String> delimiters) {
		String delimiter1 = DEFAULT_DELIMITER_1;
		String delimiter2 = DEFAULT_DELIMITER_2;
		
		if (delimiters != null) {
			if (delimiters.size() == 2) {
				delimiter1 = delimiters.get(0);
				delimiter2 = delimiters.get(1);
			} else {
				throw new RuntimeException("delimiters must have exactly 2 entries");
			}
		}
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		this.variables = builder.putAll(variables).build();
		this.delimiter1 = delimiter1;
		this.delimiter2 = delimiter2;
	}
	
	public Replacer(Map<String, String> variables, String delimiter1, String delimiter2) {
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		this.variables = builder.putAll(variables).build();
		this.delimiter1 = delimiter1;
		this.delimiter2 = delimiter2;
	}
	
	public String replacePlaceholders(final String input) {
		if (input == null) return null;
		if (!hasVariables()) return input;
		
		StringBuilder result = new StringBuilder(input);
		for (String key : variables.keySet()) {
			String placeholder = delimiter1 + key + delimiter2;
			String value = variables.get(key);
			int index = result.indexOf(placeholder);
			while (index >= 0) {
				result.replace(index, index+placeholder.length(), value);
				index = result.indexOf(placeholder);
			}
		}
		return result.toString();
	}
	
	public List<String> replacePlaceholders(final List<String> inputList) {
		if (inputList == null) return null;
		if (!hasVariables()) return inputList;
		
		List<String> result = new ArrayList<String>();
		for (String input : inputList) {
			result.add(replacePlaceholders(input));
		}
		return result;
	}
	
	public boolean hasVariables() {
		return variables != null && variables.size() > 0;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Replacer other = (Replacer) obj;
		if (delimiter1 == null) {
			if (other.delimiter1 != null) {
				return false;
			}
		} else if (!delimiter1.equals(other.delimiter1)) {
			return false;
		}
		if (delimiter2 == null) {
			if (other.delimiter2 != null) {
				return false;
			}
		} else if (!delimiter2.equals(other.delimiter2)) {
			return false;
		}
		if (variables == null) {
			if (other.variables != null) {
				return false;
			}
		} else if (!variables.equals(other.variables)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((delimiter1 == null) ? 0 : delimiter1.hashCode());
		result = prime * result + ((delimiter2 == null) ? 0 : delimiter2.hashCode());
		result = prime * result + ((variables == null) ? 0 : variables.hashCode());
		return result;
	}
}
