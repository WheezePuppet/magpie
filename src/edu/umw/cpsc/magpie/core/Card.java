package edu.umw.cpsc.magpie.core;

import java.util.Collection;
import java.sql.Date;
import java.sql.ResultSet;
import com.google.gson.*;
import com.google.gson.annotations.Expose;

public class Card extends AbstractItem {
	@Expose private int cid;
	private int inverseid = -1;
	private int deckid;
	@Expose private String question;
	@Expose private String answer;
	@Expose private String dir;
    @Expose private String color;

    public class Stats {
        public int numReviews;
        public int numSuccessfulReviews;
        public double averageReviewTime;
        public Date mostRecentDate;
    }

	public Card(ResultSet resultSet) {
		try {
			cid = resultSet.getInt("cid");
			inverseid = resultSet.getInt("inverseid");
			deckid = resultSet.getInt("did");
			question = resultSet.getString("question");
			answer = resultSet.getString("answer");
			dir = resultSet.getString("dir");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
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

	public String getDir() {
		return dir;
	}

	protected String getTable() {
		return "card";
	}

	protected String getSelector() {
		return "cid='" + cid + "'";
	}

	protected String getInsertValues() {
		if (inverseid == -1)
			return "(question, answer, did, dir) VALUES ('" + question + "', '" + answer + "', '" + deckid + "', '" + dir + "')";

		return "(question, answer, did, inverseid, dir) VALUES ('" + question + "', '" + answer + "', '" + deckid + "', '" + inverseid + "', '" + dir + "')";
	}

    public String getColor() {
        return color;
    }

    void setColor(String c) {
        color = c;
    }

    private ReviewList getReviewsFor(Student s) {
        ReviewList rl = s.getReviews();
        ReviewList reviewsForThisCard = new ReviewList();
        for (int i=0; i<rl.size(); i++) {
            if (rl.get(i).getCardId() == cid) {
                reviewsForThisCard.add(rl.get(i));
            }
        }
        return reviewsForThisCard;
    }

    public Stats getStatsFor(Student s) {
        Review lastReview = s.getLastReviewFor(this);
        Stats stats = new Stats();
        if (lastReview == null) {
            return stats;
        }
        ReviewList rl = getReviewsFor(s);
        int numSuccessfulReviews = 0;
        double totalReviewTime = 0.0;
        for (int i=0; i<rl.size(); i++) {
            if (rl.get(i).getSuccess()) {
                numSuccessfulReviews++;
            }
            totalReviewTime += rl.get(i).getResponseTime();
        }
        stats.numReviews = rl.size();
        stats.numSuccessfulReviews = numSuccessfulReviews;
        stats.averageReviewTime = totalReviewTime / stats.numReviews;
        stats.mostRecentDate = lastReview.getDate();

        return stats;
    }
}
