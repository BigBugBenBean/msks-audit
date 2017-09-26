package sc2.msks.impl.msgexchange;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class MessageFactory {

    public JSONObject createRegistrationMessage(){
        JSONObject header = new JSONObject();
        header.put("deviceid", "JAVARR");
        header.put("channelid","RR_JAVARR");
        header.put("type","HAS");

        JSONObject json = new JSONObject();
        json.put("header", header);
        json.put("payload", new JSONObject());

        return json;
    }

    public JSONObject createResponse(JSONObject header){
        JSONObject json = new JSONObject();
        json.put("header", header);

        JSONObject response = new JSONObject();
        response.put("response", "from java");

        json.put("payload", response);

        return json;
    }
}
