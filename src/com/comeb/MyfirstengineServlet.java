package com.comeb;
import java.io.IOException;

import javax.servlet.http.*;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;

@SuppressWarnings("serial")
public class MyfirstengineServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		
		
		resp.setContentType("text/html");
	//	resp.getWriter().println("Hello, world");
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	//	Entity employee = new Entity("Employee");
	//	employee.setProperty("firstname", "Antonio");
	//	datastore.put(employee);
		
		Query q = new Query("Employee");
		Object lastNameParam="Antonio";
		//q.addFilter("firstname",Query.FilterOperator.EQUAL,lastNameParam);
		PreparedQuery pq=datastore.prepare(q);
		resp.getWriter().write("<!DOCTYPE><html><body><ul>");
		for(Entity result: pq.asIterable()){
			String r=(String)result.getProperty("firstname");
			resp.getWriter().write("<li>"+r+"</li>");
		}
		resp.getWriter().write("</ul></body></html>");
	}
	public void toDataStore(String name,String password){
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Entity employee = new Entity("Employee");
		employee.setProperty("firstname", name);
		employee.setProperty("password",password);
		datastore.put(employee);
	}
	public void doPost(HttpServletRequest req,HttpServletResponse resp) throws IOException{
		String login = req.getParameter("login");
		String password = req.getParameter("password");
		if(!exist(login)){
			resp.getWriter().write("welcome "+login);
		}else{
			resp.getWriter().write("login not available");
		}
		toDataStore(login,password);
	}
	private boolean exist(String login) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		
		Query q = new Query("Employee");
		q.addFilter("firstname",Query.FilterOperator.EQUAL, login);
		PreparedQuery pq=datastore.prepare(q);
		
		return pq.countEntities()>0;
	}
}
