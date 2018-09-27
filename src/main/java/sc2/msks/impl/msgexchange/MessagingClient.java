package sc2.msks.impl.msgexchange;

import io.socket.client.IO;
import io.socket.client.Socket;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.pccw.sc2.audit.service.AuditLogService;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * hello world
 */
@Service
public class MessagingClient {

//    private Log log = LogFactory.getLog(MessagingClient.class);
	
	private static Logger log = LoggerFactory.getLogger(MessagingClient.class);

    @Autowired
    private MessageFactory msgfact;

    @Value("${ws.uri}")
    private String wsuri;

    @Value("${ws.namespace}")
    private String wsns;

    @Value("${ws.channelid}")
    private String channelid;
    
    @Autowired
    private AuditLogService auditLogService;

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
                   JSONObject resqPayload = regmsg.getJSONObject("payload");
                   String function = header.getString("function");
                   log.info("Processing function: "+function);

                   processFunction(function,resqPayload);
                   JSONObject response = msgfact.createOKResponse(header);

                   socket.emit(wsns+'/'+header.getString("channelid"), response.toString());
               });
            });

            socket.on(Socket.EVENT_DISCONNECT, objects -> socket.off(wsns+"/"+this.channelid));

            socket.on(Socket.EVENT_CONNECT_ERROR, objects -> log.debug("Socket connection error"));

            socket.on(Socket.EVENT_CONNECT_TIMEOUT, objects -> log.debug("Socket connection timeout"));

            socket.on(Socket.EVENT_CONNECTING, objects -> log.debug("Connecting server:"+ wsuri));

        }finally {
            if(socket!=null && socket.connected()){
                socket.close();
            }
        }
    }

    private void processFunction(String function,JSONObject resqPayload) {
        if("excptlg".equals(function)) {
//            String icno = resqPayload.getString("ic_no");
//            String locationId = resqPayload.getString("locationID");
//            String kioskId = resqPayload.getString("kioskID");
//            String message = resqPayload.getString("message");
            this.auditLogService.processExceptionLog(resqPayload);
        }else if("translg".equals(function)) {
            this.auditLogService.processOperationLog(resqPayload);
        }else if("tracklg".equals(function)) {
            this.auditLogService.processTrackLog(resqPayload);
        }
    }
}
