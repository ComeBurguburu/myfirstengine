package com.comeb;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class queueServlet extends HttpServlet {


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String key="login";
		String value="jdoe";
		Queue queue = QueueFactory.getDefaultQueue();
		// Ajout d’une tache simple
		TaskOptions task=TaskOptions.Builder.withUrl("/welcome").param(key, value);
		queue.add(task);
		// Ajout d’une tache simple avec des paramètres de configuration
		Map<String, String> headers=new HashMap<String, String>();
		headers.put("X-AppEngine-QueueName","user");
		headers.put("X-AppEngine-TaskName","task2");
		headers.put("X-AppEngine-TaskRetryCount","4");		
		//queue.purge();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		super.doPost(req, resp);
	}
}
