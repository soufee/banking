package coders;

import com.google.gson.Gson;
import entities.Client;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class ClientEncoder implements Encoder.Text<Client> {
    private static Gson gson = new Gson();

    @Override
    public String encode(Client object) throws EncodeException {
        return gson.toJson(object);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
