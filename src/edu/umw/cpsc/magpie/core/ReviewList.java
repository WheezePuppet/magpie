package edu.umw.cpsc.magpie.core;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Comparator;
import java.util.Collections;
import edu.umw.cpsc.magpie.util.DateHelper;

public class ReviewList {
	private List<Review> reviews = Collections.synchronizedList(new ArrayList<Review>());

	public void add(Review review) {
		reviews.add(review);
	}
	
	public Review get(int index) {
		return reviews.get(index);
	}
	
	public void remove(Review review) {
		for(int i = 0; i < size(); i++) {
			if (reviews.get(i) == review) {
				reviews.remove(i);
				break;
			}
		}
	}

	public synchronized ReviewList getFor(Card card) {
		ReviewList newList = new ReviewList();

		for(Review review : reviews) {
			if (review.getCard() == card)
				newList.add(review);
		}

		return newList;
	}

	public synchronized ReviewList getFor(Date date) {
		ReviewList newList = new ReviewList();

		for(Review review : reviews) {
			if (review.getDate().equals(date))
				newList.add(review);
		}

		return newList;
	}

	public synchronized void removeUnsuccessful() {
		ArrayList<Review> filtered = new ArrayList<Review>();

		for(Review review : reviews) {
			if (review.getSuccess())
				filtered.add(review);
		}

		reviews = filtered;
	}

	public int size() {
		return reviews.size();
	}

	public void reverse() {
		Collections.reverse(reviews);
	}

	public void sortByDateTime() {
		Collections.sort(reviews, new DateTimeOrdering());
	}

	public void sortByResponseTime() {
		Collections.sort(reviews, new ResponseTimeOrdering());
	}

	public double getAverageScore() {
		double total = 0;

		for (Review review : reviews)
			total += review.getScore();

		return total / reviews.size();
	}

	public double getAverageResponseTime() {
		double total = 0;

		for (Review review : reviews)
			total += review.getResponseTime();

		return total / reviews.size();
	}

	public java.sql.Date getNextDate() {
		int interval = getInterval();

		if (interval == -1)
			return null;
		
		java.util.Date lastDate = reviews.get(size() - 1).getDate();

		return new java.sql.Date(DateHelper.addDays(lastDate, interval).getTimeInMillis());
	}

	public int getInterval() {
		sortByDateTime();

		switch (getReviewsSinceLapse()) {
			case 0:
				return -1;
			case 1:
				return 1;
			case 2:
				return 6;
			default:
				double interval = ((double) getLastInterval()) * getEasiness();

				if (interval % 1 != 0)
					return (int)(interval + 1);

				return (int) interval;
		}
	}

	public double getEasiness() {
		sortByDateTime();

		double easiness = 2.5;

		for (int i = 0; i < size(); i++) {
			double q = get(i).getQuality();

			if (q >= 3) {
				double delta = 0.1 - (5 - q) * (0.08 + (5 - q) * 0.02);
				easiness += delta;
				easiness = Math.max(easiness, 1.3);
			}
		}

		return easiness;
	}


	private int getReviewsSinceLapse() {
		if (size() < 1)
			return 0;

		for (int i = 0; i < size(); i++) {
			Review review = get(size() - i - 1);
			if (!review.getSuccess())
				return i;
		}

		return size();
	}

	public int getLastInterval() {
		sortByDateTime();

		if (size() < 2)
			return 1;

		Date date1 = get(size() - 2).getDate();
		Date date2 = get(size() - 1).getDate();

		return Math.max(1, DateHelper.daysBetween(date1, date2));
	}

	public synchronized ReviewList clone() {
		ReviewList newList = new ReviewList();
		for (Review review : reviews)
			newList.reviews.add(review);

		return newList;
	}

	private class DateTimeOrdering implements Comparator<Review> {
		public int compare(Review r1, Review r2) {
			int dateResult = r1.getDate().compareTo(r2.getDate());
			if (dateResult != 0)
				return dateResult;

			int timeResult = r1.getTime().compareTo(r2.getTime());
			if (timeResult != 0)
				return timeResult;

			// For cases where the date and time are exactly equal. Shouldn't occur in the real world, but occurs in unit testing.
			int idResult = new Integer(r1.getId()).compareTo(r2.getId());
			return idResult;
		}

		public boolean equals(Review r1, Review r2) {
			return r1.getDate().equals(r2.getDate()) && r1.getTime().equals(r2.getTime()) && r1.getId() == r2.getId();
		}
	}

	private static class ResponseTimeOrdering implements Comparator<Review> {
		public int compare(Review r1, Review r2) {
			return new Integer(r1.getResponseTime()).compareTo(r2.getResponseTime());
		}

		public boolean equals(Review r1, Review r2) {
			return r1.getResponseTime() == r2.getResponseTime();
		}
	}
}
