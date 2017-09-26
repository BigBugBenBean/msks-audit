package sc2.msks.impl.msgexchange;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.transports.WebSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class MessagingClient {

    private Log log = LogFactory.getLog(MessagingClient.class);

    @Autowired
    private MessageFactory msgfact;

    @Value("${ws.uri}")
    private String wsuri;

    @Value("${ws.namespace}")
    private String wsns;

    public void startMessaging() throws URISyntaxException {
        IO.Options options = new IO.Options();
        options.forceNew = true;

        final Socket socket= IO.socket(new URI(wsuri), options);
        try{

            socket.connect().on(Socket.EVENT_CONNECT, objects -> {
               log.debug("socket connected");
               JSONObject registration = msgfact.createRegistrationMessage();

               log.debug("Emitting registration ["+wsns+"]"+registration);
               socket.emit(wsns+"/registration", registration.toString());

               socket.on(wsns+'/'+registration.getJSONObject("header").getString("channelid"), (Object... request) ->{
                   Object[] objA = (Object[])request;
                   JSONObject regmsg = (JSONObject) objA[0];

                   JSONObject header = regmsg.getJSONObject("header");
                   String function = header.getString("function");
                   log.info("Processing function: "+function);

                   JSONObject response = msgfact.createResponse(header);
                   socket.emit(wsns+'/'+header.getString("channelid"), response.toString());
               });
            });

            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> log.debug("Socket connection error"));

            socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> log.debug("Socket connection timeout"));

            socket.on(Socket.EVENT_CONNECTING, objects -> log.debug("Connecting server:"+ wsuri));

        }finally {
            if(socket!=null && socket.connected()){
                socket.close();
            }
        }
    }

}
