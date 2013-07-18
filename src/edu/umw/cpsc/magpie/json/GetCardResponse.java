package edu.umw.cpsc.magpie.json;

import com.google.gson.*;
import com.google.gson.annotations.Expose;
import edu.umw.cpsc.magpie.core.Card;
import edu.umw.cpsc.magpie.core.Student;
import edu.umw.cpsc.magpie.core.CardManager;
import java.util.ArrayList;
import java.util.List;

public class GetCardResponse {
	@Expose private String status;
	@Expose private Card card;
	@Expose private int time;
	@Expose private ArrayList<String> otherAnswers;

	public GetCardResponse(String status) {
        init();
		this.status = status;
	}

	public GetCardResponse(String status, int time, Card card,
        Student student) {
        init();
		this.status = status;
        if (!status.equals("done")) {
            this.card = card;
            this.time = time;
            List<Card> otherCards = 
                student.getNRandomActiveCardsWithAnswerNot(4, 
                                            card.getAnswer(), card.getDir());
            for (int i=0, n=otherCards.size(); i<n; i++) {
                otherAnswers.add(otherCards.get(i).getAnswer());
            }
        }
	}

    private void init() {
        otherAnswers = new ArrayList<String>();
    }

	public String toString() {
		return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
	}
}
