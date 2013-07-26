package edu.umw.cpsc.magpie.core;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;

public class Deck extends AbstractItem implements Comparable<Deck> {
	private int id;
	private String name;
	private boolean active;
	private int courseid;
	private String color;

	private List<Integer> cardIds = Collections.synchronizedList(new ArrayList<Integer>());

	public Deck(ResultSet resultSet) {
		try {
			id = resultSet.getInt("did");
			name = resultSet.getString("deckname");
			active = resultSet.getInt("active") == 1;
			courseid = resultSet.getInt("courseid");
			color = resultSet.getString("color");

			ResultSet cardRs = MagpieConnection.instance().executeQuery("SELECT * FROM card WHERE did='" + getId() + "' order by cid");
			while (cardRs.next()) {
                int cid = cardRs.getInt("cid");
				cardIds.add(cid);
                CardManager.instance().get(cid).setColor(color);
            }
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public Deck(String name, boolean active, int courseid) {
		this.name = name;
		this.active = active;
		this.courseid = courseid;

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

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		update("active", active);
	}

	public Course getCourse() {
		return CourseManager.instance().get(courseid);
	}

	public synchronized ArrayList<Card> getCards() {
		ArrayList<Card> cards = new ArrayList<Card>();

		for(int id : cardIds)
			cards.add(CardManager.instance().get(id));

		return cards;
	}

	public String getTable() {
		return "deck";
	}

	public String getSelector() {
		return "did='" + id + "'";
	}

	public String getInsertValues() {
		return "(deckname, courseid, active) VALUES ('" + name + "', '" + courseid + "', '" + (active ? 1 : 0) + "')";
	}

	public int compareTo(Deck other) {
        if (id < other.id) {
            return -1;
        }
        if (id > other.id) {
            return 1;
        }
        return 0;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String c) {
        color = c;
	    for (int i=0; i<cardIds.size(); i++) {
            CardManager.instance().get(cardIds.get(i)).setColor(c);
        }
		update("color", color);
    }
}
