package edu.umw.cpsc.magpie.core;

import java.util.ArrayList;
import java.sql.ResultSet;

public class CardManager extends AbstractItemManager<Card> {
	private CardManager() {
		super("card", new IItemFactory<Card>() {
			public Card create(ResultSet resultSet) {
				return new Card(resultSet);
			}
		});
	}

	public static CardManager instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static CardManager INSTANCE = new CardManager();
	}
}
