package edu.umw.cpsc.magpie.core;

import edu.umw.cpsc.magpie.util.DbHelper;

public class Loader extends javax.servlet.http.HttpServlet {
	public void init() {
		DbHelper.clearAll();
		DbHelper.loadAll();
	}
}
