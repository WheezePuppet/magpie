package edu.umw.cpsc.magpie.core;

import java.sql.ResultSet;

public interface IItemFactory<T extends AbstractItem> {
	T create(ResultSet resultSet);
}
