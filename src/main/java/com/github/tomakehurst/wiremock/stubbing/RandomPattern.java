package com.github.tomakehurst.wiremock.stubbing;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

@JsonSerialize(include=Inclusion.ALWAYS)
@JsonPropertyOrder({ "target", "pattern" })
public class RandomPattern {
    private String target;
    private String pattern;
    
    private Random rng = new SecureRandom();
    
    // just to make JSON happy
    public RandomPattern() { }
    
    public RandomPattern(String target, String pattern) {
        this.target = target;
        this.pattern = pattern;
    }

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
    }

    private final static Map<Character, String> characterSets = new HashMap<Character, String>();
    
    static {
        characterSets.put('a', "abcdefghijklmnopqrstuvwxyz");
        characterSets.put('A', "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        characterSets.put('z', "0123456789abcdefghijklmnopqrstuvwxyz");
        characterSets.put('Z', "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        characterSets.put('x', "0123456789abcdef");
        characterSets.put('X', "0123456789ABCDEF");
        characterSets.put('0', "0123456789");
    }
    
    public String generateRandomValue() {
        StringBuilder value = new StringBuilder();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            if (characterSets.containsKey(c)) {
                value.append(generateRandomCharacter(characterSets.get(c), rng));
            } else {
                value.append(c);
            }
        }
        return value.toString();
    }
    
    private char generateRandomCharacter(String characterSet, Random rng) {
        return characterSet.charAt(rng.nextInt(characterSet.length()));
    }
    
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        RandomPattern other = (RandomPattern) obj;
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
        return true;
    }
}
