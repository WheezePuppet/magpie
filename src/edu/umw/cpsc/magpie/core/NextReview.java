package edu.umw.cpsc.magpie.core;

import java.sql.ResultSet;
import java.sql.Date;
import org.apache.log4j.Logger;

public class NextReview extends AbstractItem {
	private static Logger log = Logger.getLogger(NextReview.class);

	private int id;
	private int uid;
	private int cid;
	private Date nextDate;

	public NextReview(ResultSet resultSet) {
		try {
			id = resultSet.getInt("nrid"); // TODO add to sql
			uid = resultSet.getInt("uid");
			cid = resultSet.getInt("cid");
			nextDate = resultSet.getDate("nextDate");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public NextReview(Student student, Card card, Date nextDate) {
		student.removeNextReview(card);

		this.uid = student.getId();
		this.cid = card.getId();
		this.nextDate = nextDate;

		insert();
	}
	
	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public Student getStudent() {
		return StudentManager.instance().get(uid);
	}

	public Card getCard() {
		return CardManager.instance().get(cid);
	}

	public Date getNextDate() {
		return nextDate;
	}

	public String getTable() {
		return "nextReview";
	}

	public String getSelector() {
		return "nrid='" + id + "'";
	}

	public String getInsertValues() {
		return "(uid, cid, nextDate) VALUES ('" + uid + "', '" + cid + "', DATE('" + nextDate.toString() + "'))";
	}
}
