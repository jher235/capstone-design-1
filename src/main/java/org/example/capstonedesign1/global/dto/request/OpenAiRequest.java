package org.example.capstonedesign1.global.dto.request;

import java.util.List;

import org.example.capstonedesign1.global.dto.response.Message;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OpenAiRequest {
	private String model;
	private List<Message> messages;
	@JsonProperty("max_tokens")
	private int maxTokens;
	private double temperature;
}
