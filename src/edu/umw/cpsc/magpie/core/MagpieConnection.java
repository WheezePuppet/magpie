package edu.umw.cpsc.magpie.core;

import java.sql.*;
import org.apache.log4j.Logger;

public class MagpieConnection {
	private static Logger log = Logger.getLogger(MagpieConnection.class);

	private String db;
	private String user;
	private String pass;

	private Connection connection;

	private MagpieConnection() {
		db = "jdbc:mysql://localhost/magpie?charSet=UTF-8&useUnicode=true&characterEncoding=UTF-8";
		user = "stephen";
		pass = "iloverae";

		connect();
	}

	public void setConnection(String db, String user, String pass) {
		this.db = db;
		this.user = user;
		this.pass = pass;

		connect();
	}

	private void connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//log.debug("Connecting to db '" + db + "' with username='" + user + "' and password='" + pass + "'");
			connection = DriverManager.getConnection(db, user, pass);
		} catch (Exception e) {
			connection = null; // Make absolutely sure we'not not connected to any database if anything goes wrong--we don't want to bork data.
			log.error("Exception in setConnection(), about to print stacktrace.");
			e.printStackTrace(System.err);
		}

		try {
			if (connection == null || connection.isClosed())
				log.error("Could not connect to database.");
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}

	public ResultSet executeQuery(String query) {
		try {
			if (connection.isClosed())
				connect();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		//log.debug("Executing query '" + query + "'");

		ResultSet result = null;
		
		try {
			Statement statement = connection.createStatement();
statement.executeUpdate("set character_set_server=utf8");
statement.executeUpdate("set character_set_connection=utf8");

			result = statement.executeQuery(query);
		} catch (Exception e) {
			connect();

			try {
				Statement statement = connection.createStatement();
statement.executeUpdate("set character_set_server=utf8");
statement.executeUpdate("set character_set_connection=utf8");
				result = statement.executeQuery(query);
			} catch (Exception innerE) {
				innerE.printStackTrace(System.err);
			}
		}

		return result;
	}

	public int executeUpdate(String query) {
		try {
			if (connection.isClosed())
				connect();
		} catch (Exception e) {
			e.printStackTrace(System.err);
		}

		//log.debug("Executing update '" + query + "'");

		int result = 0;
		
		try {
			Statement statement = connection.createStatement();
statement.executeUpdate("set character_set_server=utf8");
statement.executeUpdate("set character_set_connection=utf8");
			statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
			ResultSet resultSet = statement.getGeneratedKeys();
			if (resultSet.next())
				result = resultSet.getInt(1);
		} catch (Exception e) {
			connect();

			try {
				Statement statement = connection.createStatement();
statement.executeUpdate("set character_set_server=utf8");
statement.executeUpdate("set character_set_connection=utf8");
				statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet resultSet = statement.getGeneratedKeys();
				if (resultSet.next())
					result = resultSet.getInt(1);
			} catch (Exception innerE) {
				innerE.printStackTrace(System.err);
			}
		}

		return result;
	}

	public static MagpieConnection instance() {
		return SingletonHolder.INSTANCE;
	}

	private static class SingletonHolder {
		private static final MagpieConnection INSTANCE = new MagpieConnection();
	}
}
