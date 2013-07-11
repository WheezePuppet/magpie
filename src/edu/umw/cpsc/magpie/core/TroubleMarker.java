package edu.umw.cpsc.magpie.core;

import java.util.Calendar;
import java.sql.ResultSet;
import java.sql.Date;

public class TroubleMarker extends AbstractItem {
	private int id;
	private int uid;
	private Date date;

	public TroubleMarker(ResultSet resultSet) {
		try {
			id = resultSet.getInt("tmid");
			uid = resultSet.getInt("uid");
			date = resultSet.getDate("date");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public TroubleMarker(Student student, Calendar cal) {
		this(student, new Date(cal.getTimeInMillis()));
	}

	public TroubleMarker(Student student, Date date) {
		this.uid = student.getId();
		this.date = date;

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

	public Date getDate() {
		return date;
	}

	public String getTable() {
		return "troubleMarker";
	}

	public String getSelector() {
		return "tmid='" + id + "'";
	}

	public String getInsertValues() {
		return "(uid, date) VALUES ('" + uid + "', DATE('" + date.toString() + "'))";
	}
}
