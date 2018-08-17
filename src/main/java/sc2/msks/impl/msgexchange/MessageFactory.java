package sc2.msks.impl.msgexchange;

import java.util.HashMap;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageFactory {

    @Value("${ws.channelid}")
    private String channelid;

    @Value("${ws.deviceid}")
    private String deviceid;

    @Value("${ws.type}")
    private String type;

    public JSONObject createRegistrationMessage() {
        JSONObject header = new JSONObject();
        header.put("deviceid", deviceid);
        header.put("channelid",channelid);
        header.put("type",type);
        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("payload", new JSONObject());
        return json;
    }

    public JSONObject createResponse(JSONObject header, JSONObject respPayload) {
        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("payload", respPayload);
        return json;
    }

    public JSONObject createOKResponse(JSONObject header) {
        return this.createResponse(header, new JSONObject().put("result",true).put("error_info", new HashMap<String,Integer>() {private static final long serialVersionUID = 1L;
		{
            this.put("error_code",0);
        }}));
    }
}
