package edu.umw.cpsc.magpie.json;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import edu.umw.cpsc.magpie.core.Card;

public class GetCardResponse {
	@Expose private String status;
	@Expose private Card card;
	@Expose private int time;

	public GetCardResponse(String status) {
		this.status = status;
	}

	public GetCardResponse(String status, int time, Card card) {
		this.status = status;
		this.card = card;
		this.time = time;
	}

	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
