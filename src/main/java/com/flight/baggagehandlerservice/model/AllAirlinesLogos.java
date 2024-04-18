package com.flight.baggagehandlerservice.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class AllAirlinesLogos {

	private List<Logo> logotypes;

	@SuppressWarnings("unchecked")
	@JsonProperty("result")
	private void extractLogotypes(Map<String, Object> result) {
		List<Logo> logos = new ArrayList<>();
		Map<String, Object> responseObject = (Map<String, Object>) result.get("response");
		Map<String, Object> airlinesObject = (Map<String, Object>) responseObject.get("airlines");
		List<Map<String, Object>> logotypesObjectList = (List<Map<String, Object>>) airlinesObject.get("logotypes");
		logotypesObjectList.forEach(map -> {
			Map<String, String> file = (Map<String, String>) map.get("file");
			logos.add(new Logo(file.get("name"), file.get("url")));
		});
		this.setLogotypes(logos);
	}

	@Data
	@AllArgsConstructor
	public class Logo {
		private String imageName;
		private String url;
	}
}
