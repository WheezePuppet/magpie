package edu.umw.cpsc.magpie.core;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;

public class Hand {
	private final int MAX_CARDS = 5;

	private List<Card> cards = new ArrayList<Card>();
	private Student student;

	public Hand(Student student) {
		this.student = student;
	}

	public void refillFrom(List<Card> validCards) {
		Collection<Card> unmemorizedCards = new ArrayList<Card>();
		for(Card card : cards) {
			if(!student.hasMemorized(card))
				unmemorizedCards.add(card);
		}
		cards.retainAll(unmemorizedCards);
		
		Collections.shuffle(validCards, new Random());

		for(Card card : validCards) {
			if(cards.size() >= MAX_CARDS)
				break;

			if(!cards.contains(card))
				cards.add(card);
		}
	}

	public Card next(Card lastCard) {
		Random random = new Random();

		List<Card> tempCards = new ArrayList<Card>();
		for(Card card : cards)
			tempCards.add(card);

		if(tempCards.size() > 1 && lastCard != null)
			tempCards.remove(lastCard);

		return tempCards.get(random.nextInt(tempCards.size()));
	}

	public boolean hasNext() {
		return cards.size() > 0;
	}
}
