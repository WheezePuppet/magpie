package edu.umw.cpsc.magpie.core;

import java.sql.*;

public abstract class AbstractItem {
	public abstract int getId();
	protected abstract void setId(int id);

	protected abstract String getTable();
	protected abstract String getSelector();
	protected abstract String getInsertValues();

	public void insert() {
		String query = "INSERT INTO " + getTable() + " " + getInsertValues();
		setId(MagpieConnection.instance().executeUpdate(query));
	}

	public void remove() {
		String query = "DELETE FROM " + getTable() + " WHERE " + getSelector();
		MagpieConnection.instance().executeUpdate(query);
	}

	protected void update(String field, String value) {
		String query = "UPDATE " + getTable() + " SET " + field + "='" + value + "' WHERE " + getSelector();
		MagpieConnection.instance().executeUpdate(query);
	}

	protected void update(String field, int value) {
		update(field, Integer.toString(value));
	}

	protected void update(String field, boolean value) {
		update(field, value ? "1" : "0");
	}

	protected void update(String field, Date value) {
		update(field, value.toString());
	}

	protected void update(String field, GradingGroup value) {
		update(field, value.toDbString());
	}
}
