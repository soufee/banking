package coders;

import com.google.gson.Gson;
import entities.Client;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class ClientDecoder implements Decoder.Text<Client> {
    private static Gson gson = new Gson();

    @Override
    public Client decode(String s) throws DecodeException {
        return gson.fromJson(s, Client.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
