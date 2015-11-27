var token = '{{ token }}';
getToken=function(){
	$.post(
			"/token",
			{ login: $("#loginId").val(), CMD:"GET_TOKEN"},
			function(m){
				//retreive the token generated associated with the created Channel
				token=m.token;
				openChannel();
			} );
};
openChannel = function() {
	//retrieve the communication channel associated with the given token
	var channel = new goog.appengine.Channel(token);
	//define function handler associated with the channel
	var handler = {
			'onopen': onOpened,
			'onmessage': onMessage,
			'onerror': function(err)
			{ alert("Error Occurred"+err.data);},
			'onclose': function() {alert("channel Closed"); }
	};
	//open the channel with associated function handler
	var socket = channel.open(handler);
	socket.onopen = onOpened;
	socket.onmessage = onMessage;
}
onOpened = function() {
	alert("Opened");
};
onMessage = function(m) {
	$("#historyId").append(m.data);

}
sendMessage=function(){
	var message={};
	message.token=token;
	message.CMD="SEND_MSG";
	message.message=$("#txtId").val();
//	Send message to the servlet at the URL /token with message to send
	$.post(
			"/token",
			message,
			function(m){

			} );
//	remove text from input box
	$("#txtId").val("");
};