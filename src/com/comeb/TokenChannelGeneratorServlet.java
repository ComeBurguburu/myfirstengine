package com.comeb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;



public class TokenChannelGeneratorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ConcurrentHashMap<String, String> loginTokenMap;
	public TokenChannelGeneratorServlet() {
		loginTokenMap = new ConcurrentHashMap<String, String>();
	}
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String currentOrder = req.getParameter("CMD");
		if ("GET_TOKEN".equals(currentOrder)) {
			String currentLogin = req.getParameter("login");
			registerChannelSendToken(currentLogin, resp);
		} else if ("SEND_MSG".equals(currentOrder)) {
			String currentMessage = req.getParameter("message");
			String token = req.getParameter("token");
			sendMessageToAllChannel(currentMessage, token);
		}
	}
	private void registerChannelSendToken(String currentLogin,
			HttpServletResponse resp) throws IOException {
		String token = "";
		if (!loginTokenMap.containsKey(currentLogin)) {
			String uuid = UUID.randomUUID().toString();
			// Call the Channel service
			ChannelService channelService = ChannelServiceFactory .getChannelService();
			// Generate the Channel associated with the unique identifier
			token = channelService.createChannel(uuid);
			loginTokenMap.put(currentLogin, token);
		} else {
			token = loginTokenMap.get(currentLogin);
		}
		resp.setContentType("application/json");
		JSONObject jsonToSend;
		try {
			jsonToSend = new JSONObject("{'token':" + token + "}");
			PrintWriter out = resp.getWriter();
			out.write(jsonToSend.toString());
			out.flush();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	private void sendMessageToAllChannel(String currentMessage,
			String originToken) {
		// Get channel service
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		// Browse all channel token
		for (String token : this.loginTokenMap.values()) {
			// send a message to the current channel token
			channelService.sendMessage(new ChannelMessage(token,
					getLoginFromToken(originToken) + ": " + currentMessage + "<br>"));
		}
	}
	private String getLoginFromToken(String originToken) {
		for (String s : this.loginTokenMap.keySet()) {
			if (originToken.equals(loginTokenMap.get(s))) {
				return s;
			}
		}
		return "unknown";
	}
}
