package edu.umw.cpsc.magpie.core;

import java.sql.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractItemManager<T extends AbstractItem> {
	private final String table;
	private final String selector;
	private final IItemFactory<T> factory;

	protected Map<Integer, T> items = Collections.synchronizedMap(new HashMap<Integer, T>());

	public AbstractItemManager(String table, IItemFactory<T> factory) {
		this.table = table;
		this.selector = "true";
		this.factory = factory;
	}

	public AbstractItemManager(String table, String selector, IItemFactory<T> factory) {
		this.table = table;
		this.selector = selector;
		this.factory = factory;
	}

	public synchronized void load() {
		try {
			ResultSet resultSet = MagpieConnection.instance().executeQuery("SELECT * FROM " + table + " WHERE " + selector);

			while(resultSet.next())
				add(factory.create(resultSet));
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public synchronized void clear() {
		items.clear();
	}

	public synchronized void clearDb() {
		// Be CAREFUL with this.
		MagpieConnection.instance().executeUpdate("DELETE FROM " + table);
		clear();
	}

	public synchronized void add(T item) {
		items.put(item.getId(), item);
	}

	public synchronized T get(int id) {
		return items.get(id);
	}

	public synchronized void remove(int id) {
		remove(get(id));
	}

	public synchronized void remove(T item) {
		items.remove(item.getId());
		item.remove();
	}

	public synchronized int size() {
		return items.values().size();
	}

	public Collection<T> values() {
		return items.values();
	}
}
