package edu.umw.cpsc.magpie.core;

import java.util.Collection;
import java.sql.ResultSet;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

public class Card extends AbstractItem {
	@Expose private int cid;
	private int inverseid = -1;
	private int deckid;
	@Expose private String question;
	@Expose private String answer;

	public Card(ResultSet resultSet) {
		try {
			cid = resultSet.getInt("cid");
			inverseid = resultSet.getInt("inverseid");
			deckid = resultSet.getInt("did");
			question = resultSet.getString("question");
			answer = resultSet.getString("answer");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public Card(String question, String answer, int deckid) {
		this(question, answer, deckid, -1);
	}

	public Card(String question, String answer, int deckid, int inverseid) {
		this.question = question;
		this.answer = answer;
		this.deckid = deckid;
		this.inverseid = inverseid;

		insert();
	}	

	// just for testing
	static ReviewList scoreReviews = null;
	public double getAverageScore() {
		if (scoreReviews == null) {
			scoreReviews = new ReviewList();

			for (Student student : StudentManager.instance().values()) {
				if (student.getGradingGroup() != GradingGroup.SCORE)
					continue;

				ReviewList tempList = student.getReviewsClone();
				for (int i = 0; i < tempList.size(); i++)
					scoreReviews.add(tempList.get(i));
			}
		}

		return scoreReviews.getFor(this).getAverageScore();
	}

	static ReviewList timerReviews = null;
	public double getAverageResponseTime() {
		if (timerReviews == null) {
			timerReviews = new ReviewList();

			for (Student student : StudentManager.instance().values()) {
				if (student.getGradingGroup() != GradingGroup.SCORE)
					continue;

				ReviewList tempList = student.getReviewsClone();
				for (int i = 0; i < tempList.size(); i++)
					timerReviews.add(tempList.get(i));
			}
		}

		return timerReviews.getFor(this).getAverageResponseTime();
	}

	public double getAverageEasiness() {
		double totalEasiness = 0;
		Collection<Student> students = StudentManager.instance().values();

		for (Student student : students)
			totalEasiness += student.getEasinessFor(this);

		return totalEasiness / students.size();
	}
	// end just for testing

	public int getId() {
		return cid;
	}

	protected void setId(int id) {
		cid = id;
	}

	public Card getInverse() {
		return CardManager.instance().get(inverseid);
	}

	public Deck getDeck() {
		return DeckManager.instance().get(deckid);
	}

	public String getQuestion() {
		return question;
	}

	public String getAnswer() {
		return answer;
	}

	protected String getTable() {
		return "card";
	}

	protected String getSelector() {
		return "cid='" + cid + "'";
	}

	protected String getInsertValues() {
		if (inverseid == -1)
			return "(question, answer, did) VALUES ('" + question + "', '" + answer + "', '" + deckid + "')";

		return "(question, answer, did, inverseid) VALUES ('" + question + "', '" + answer + "', '" + deckid + "', '" + inverseid + "')";
	}
}
