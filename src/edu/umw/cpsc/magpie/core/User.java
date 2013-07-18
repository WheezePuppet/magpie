package edu.umw.cpsc.magpie.core;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.Random;
import edu.umw.cpsc.magpie.util.SHA1Generator;

public abstract class User extends AbstractItem {
	protected int id;
	protected String username;
	protected String password;
	protected String firstName;
	protected String lastName;

	protected List<Integer> courseIds = Collections.synchronizedList(new ArrayList<Integer>());

	protected User(ResultSet resultSet) {
		try {
			id = resultSet.getInt("uid");
			username = resultSet.getString("username");
			password = resultSet.getString("password");
			firstName = resultSet.getString("firstname");
			lastName = resultSet.getString("lastname");

			ResultSet courseLinkRs = MagpieConnection.instance().executeQuery
				("SELECT * FROM courseLink WHERE uid='" + getId() + "'");
			while (courseLinkRs.next())
				courseIds.add(courseLinkRs.getInt("courseid"));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public User(String username, String password, String firstName, String lastName) {
		this.username = username;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public abstract Role getRole();

	public boolean dueForPasswordChange() {
		boolean result = false;

		try {
			result = new SHA1Generator().SHA1(username).equals(password);
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		return result;
	}

	public int getId() {
		return id;
	}

	protected void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
		update("password", password);
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
		update("firstname", firstName);
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
		update("lastname", lastName);
	}

	public synchronized boolean hasCourse(Course course) {
		for(int courseid : courseIds) {
			if (course.getId() == courseid)
				return true;
		}

		return false;
	}

	public synchronized ArrayList<Course> getCourses() {
		ArrayList<Course> courses = new ArrayList<Course>();

		for(int id : courseIds)
			courses.add(CourseManager.instance().get(id));

		return courses;
	}

	public synchronized ArrayList<Deck> getDecks() {
		ArrayList<Deck> matches = new ArrayList<Deck>();

		for(Course course : getCourses()) {
			for(Deck deck : course.getDecks())
				matches.add(deck);
		}

		return matches;
	}

	public synchronized ArrayList<Deck> getActiveDecks() {
		ArrayList<Deck> matches = new ArrayList<Deck>();

		for(Course course : getCourses()) {
			for(Deck deck : course.getActiveDecks())
				matches.add(deck);
		}

		return matches;
	}

	public synchronized ArrayList<Card> getCards() {
		ArrayList<Card> matches = new ArrayList<Card>();

		for(Deck deck : getDecks()) {
			for(Card card : deck.getCards())
				matches.add(card);
		}

		return matches;
	}

	public synchronized ArrayList<Card> getActiveCards() {
		ArrayList<Card> matches = new ArrayList<Card>();

		for(Deck deck : getActiveDecks()) {
			for(Card card : deck.getCards())
				matches.add(card);
		}

		return matches;
	}

    public synchronized List<Card> getNRandomActiveCardsWithAnswerNot(
        int n, String unwantedAnswer, String dir) {

        List<Card> allCards = getActiveCards();
        Random r = new Random();
        int numCards = allCards.size();
        List<Card> theCards = new ArrayList<Card>();
        for (int i=0; i<n; i++) {
            Card c = allCards.get(r.nextInt(numCards));
            while (c.getAnswer().equals(unwantedAnswer)  ||
                theCards.contains(c)  ||  !c.getDir().equals(dir)) {
                c = allCards.get(r.nextInt(numCards));
            }
            theCards.add(c);
        }
        return theCards;
    }

	public String getTable() {
		return "user";
	}

	public String getSelector() {
		return "uid='" + id + "'";
	}
}
