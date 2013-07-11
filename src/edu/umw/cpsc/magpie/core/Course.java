package edu.umw.cpsc.magpie.core;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.sql.ResultSet;
import java.sql.Date;
import edu.umw.cpsc.magpie.util.DateHelper;

public class Course extends AbstractItem {
	private int id;
	private String name;
	private Date startDate;
	private Date endDate;
    private int minsPerDay;

	private List<Integer> deckIds = Collections.synchronizedList(new ArrayList<Integer>());
	private List<Integer> userIds = Collections.synchronizedList(new ArrayList<Integer>());

	public Course(ResultSet resultSet) {
		try {
			id = resultSet.getInt("courseid");
			name = resultSet.getString("coursename");
			startDate = resultSet.getDate("startdate");
			endDate = resultSet.getDate("enddate");
			minsPerDay = resultSet.getInt("minsperday");

            // Spring 2011 - for simplicity (since the schema associates each
            // deck with a single course) simply assume all courses have all
            // decks.
			ResultSet deckRs = MagpieConnection.instance().executeQuery("SELECT * FROM deck");
			//ResultSet deckRs = MagpieConnection.instance().executeQuery("SELECT * FROM deck WHERE courseid='" + getId() + "'");
			while (deckRs.next())
				deckIds.add(deckRs.getInt("did"));

			ResultSet userRs = MagpieConnection.instance().executeQuery("SELECT * FROM courseLink WHERE courseid='" + getId() + "'");
			while (userRs.next())
				userIds.add(userRs.getInt("uid"));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public Course(String name, Date startDate) {
		this.name = name;
		this.startDate = startDate;

		insert();
	}
	
	public int getId() {
		return id;
	}
	
	protected void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public int getMinsPerDay() {
		return minsPerDay;
	}
	
	public void setMinsPerDay(int minsPerDay) {
		this.minsPerDay = minsPerDay;
        update("minsPerDay",minsPerDay);
	}

	public int getLength() {
		return DateHelper.daysBetween(startDate, endDate);
	}

	public ArrayList<Calendar> getMondays() {
		ArrayList<Calendar> mondays = new ArrayList<Calendar>();

		Calendar calendar = DateHelper.stripTime(startDate);
		while(calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY)
			calendar.add(Calendar.DATE, -1);

		while(DateHelper.daysBetween(calendar.getTime(), endDate) >= 0) {
			mondays.add((Calendar) calendar.clone());
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
		}

		return mondays;
	}

	public boolean includesDate(Calendar calendar) {
		return includesDate(calendar.getTime());
	}

	public boolean includesDate(java.util.Date date) {
        boolean blockedOut = false;
        try {
            ResultSet rs = MagpieConnection.instance().executeQuery(
                "SELECT COUNT(*) FROM blockoutDate WHERE courseid="+id+
                " and date='" + new SimpleDateFormat("yyyy-MM-dd").format(date) + "'");
            rs.next();
            blockedOut = rs.getInt(1) > 0;
        }
        catch (Exception e) {
            System.out.println("Couldn't read blockoutDate; assuming not blocked out...");
            e.printStackTrace();
        }
        
        return (!blockedOut &&
		    DateHelper.daysBetween(date, startDate) <= 0 &&
			DateHelper.daysBetween(date, endDate) >= 0);
	}

	public synchronized ArrayList<Deck> getDecks() {
		ArrayList<Deck> decks = new ArrayList<Deck>();

		for(int id : deckIds)
			decks.add(DeckManager.instance().get(id));

		return decks;
	}

	public synchronized ArrayList<Deck> getActiveDecks() {
		ArrayList<Deck> decks = new ArrayList<Deck>();

		for(int id : deckIds) {
			Deck deck = DeckManager.instance().get(id);
			if (deck.getActive())
				decks.add(deck);
		}

		return decks;
	}

	public synchronized ArrayList<Student> getEnrolledStudents() {
		ArrayList<Student> students = new ArrayList<Student>();

		for(int id : userIds) {
			Student student = StudentManager.instance().get(id);
			if (student != null)
				students.add(student);
		}

		Collections.sort(students);

		return students;
	}

	public synchronized ArrayList<Student> getEnrolledStudents(GradingGroup group) {
        if (group == GradingGroup.ALL) {
            return getEnrolledStudents();
        }

		ArrayList<Student> students = new ArrayList<Student>();

		for(int id : userIds) {
			Student student = StudentManager.instance().get(id);
			if (student != null && student.getGradingGroup() == group)
				students.add(student);
		}

		Collections.sort(students);

		return students;
	}

	public String getTable() {
		return "course";
	}

	public String getSelector() {
		return "courseid='" + id + "'";
	}

	public String getInsertValues() {
		return "(coursename, startdate, enddate) VALUES ('" + name + "', DATE('" + startDate.toString() + "'), DATE('" + endDate.toString() + "'))";
	}
}
