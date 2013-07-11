package edu.umw.cpsc.magpie.core;

import java.util.Calendar;
import java.sql.ResultSet;
import java.sql.Date;

public class DoneMarker extends AbstractItem {
	private int id;
	private int uid;
	private Date doneDate;

	public DoneMarker(ResultSet resultSet) {
		try {
			id = resultSet.getInt("dmid");
			uid = resultSet.getInt("uid");
			doneDate = resultSet.getDate("doneDate");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public DoneMarker(Student student, Calendar doneCal) {
		this(student, new Date(doneCal.getTimeInMillis()));
	}

	public DoneMarker(Student student, Date doneDate) {
		this.uid = student.getId();
		this.doneDate = doneDate;

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

	public Date getDoneDate() {
		return doneDate;
	}

	public String getTable() {
		return "doneMarker";
	}

	public String getSelector() {
		return "dmid='" + id + "'";
	}

	public String getInsertValues() {
		return "(uid, doneDate) VALUES ('" + uid + "', DATE('" + doneDate.toString() + "'))";
	}
}
