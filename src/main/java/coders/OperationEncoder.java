package coders;

import com.google.gson.Gson;
import entities.Operation;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class OperationEncoder implements Encoder.Text<Operation> {
    private static Gson gson = new Gson();

    @Override
    public String encode(Operation object) throws EncodeException {
        return gson.toJson(object);
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
