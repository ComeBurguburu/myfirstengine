package com.comeb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

public class TestServlet extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doGet(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
		if(exist(req.getParameter("login"),req.getParameter("password"))){
			resp.getWriter().write("success");
		}else{
			resp.getWriter().write("failed");
		}
	}
	private boolean exist(String login,String password) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("Employee");
		q.addFilter("firstname",Query.FilterOperator.EQUAL, login);
		q.addFilter("password",Query.FilterOperator.EQUAL, password);

		PreparedQuery pq=datastore.prepare(q);
		
		return pq.countEntities()>0;
	}

}
