package com.comeb;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class Welcome extends HttpServlet{

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String login = req.getParameter("login");
		try{
		DatastoreService dsf = DatastoreServiceFactory.getDatastoreService();
		Entity e = new Entity("message");
		e.setProperty("message", "Welcome "+login);
		dsf.put(e);
		}catch(Exception exception){
			exception.printStackTrace();
		}
		resp.getWriter().write("saved in db");
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(req,resp);
	}
	

}
