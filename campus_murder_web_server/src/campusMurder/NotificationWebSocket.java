package campusMurder;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocketendpoint")
public class NotificationWebSocket {

	private String username;

	@OnOpen
	public void onOpen(){
		System.out.println("opened web socket");
	}

	@OnClose
	public void onClose(){
	    System.out.println("Close Connection ...");
	}

	@OnMessage
	public void onMessage(String message){
	    System.out.println("Message from the client: " + message);
	    username = message;
	}

	@OnError
	public void onError(Throwable e){
	    e.printStackTrace();
	}
	
}
