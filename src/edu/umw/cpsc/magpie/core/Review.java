package edu.umw.cpsc.magpie.core;

import java.util.Comparator;
import java.util.Calendar;
import java.sql.*;
import edu.umw.cpsc.magpie.util.DateHelper;

public class Review extends AbstractItem {
	private int id;
	private int studentid;
	private int cardid;
	private Date date;
	private Time time;
	private int score;
	private boolean success;
	private int responseTime;

public String toString() {
    return "(" + 
        id + "," +
        studentid + "," +
        cardid + "," +
        date + "," +
        time + "," +
        score + "," +
        success + "," +
        responseTime + ")";
}
	public Review(ResultSet resultSet) {
		try {
			id = resultSet.getInt("rid");
			studentid = resultSet.getInt("uid");
			cardid = resultSet.getInt("cid");
			score = resultSet.getInt("score");
			success = resultSet.getBoolean("success");
			responseTime = resultSet.getInt("responseTime");
			date = resultSet.getDate("reviewDatetime");
			time = resultSet.getTime("reviewDatetime");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public Review(int studentid, int cardid, int score, boolean success, int responseTime) {
		this.studentid = studentid;
		this.cardid = cardid;
		this.score = score;
		this.success = success;
		this.responseTime = responseTime;

		this.date = DateHelper.nowSqlDate();
		this.time = DateHelper.nowSqlTime();

		insert();
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return StudentManager.instance().get(studentid);
	}

	public Card getCard() {
		return CardManager.instance().get(cardid);
	}

    public int getCardId() {
        return cardid;
    }

	public Date getDate() {
		return date;
	}

	public Time getTime() {
		return time;
	}

	public int getScore() {
		return score;
	}

	public boolean getSuccess() {
		return success;
	}

	public int getResponseTime() {
		return responseTime;
	}

	public long getTimeSince(Review other) {
		return time.getTime() - other.time.getTime();
	}

	public double getQuality() {
		Student student = getStudent();
		if (student.getGradingGroup() == GradingGroup.SCORE)
			return score;

		if (!success)
			return 0;

		ReviewList reviews = student.getReviewsClone();
		reviews.remove(this);
		reviews.removeUnsuccessful();

		if (reviews.size() == 0)
			return 4;

		reviews.sortByResponseTime();
		reviews.reverse();

		for(int i = 0; i < reviews.size(); i++) {
			if (reviews.get(i).responseTime < responseTime)
				return 3 + ((double) i) / ((double) reviews.size()) * 2.0;
		}

		return 5;
	}

	public String getTable() {
		return "review";
	}

	public String getSelector() {
		return "rid='" + id + "'";
	}

	public String getInsertValues() {
		String dateString = DateHelper.toDatetimeString(date, time);

		return "(uid, cid, score, success, responseTime, reviewDatetime) VALUES ('" +
			studentid + "', '" + cardid + "', '" + score + "', '" + (success ? 1 : 0) + "', '" + responseTime + "', " + dateString + ")";
	}
}
