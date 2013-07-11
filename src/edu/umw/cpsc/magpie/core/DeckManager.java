package edu.umw.cpsc.magpie.core;

import java.util.ArrayList;
import java.sql.ResultSet;

public class DeckManager extends AbstractItemManager<Deck> {
	private DeckManager() {
		super("deck", new IItemFactory<Deck>() {
			public Deck create(ResultSet resultSet) {
				return new Deck(resultSet);
			}
		});
	}

	public static DeckManager instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final DeckManager INSTANCE = new DeckManager();
	}
}
