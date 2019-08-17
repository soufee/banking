package coders;

import com.google.gson.Gson;
import entities.Operation;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class OperationDecoder implements Decoder.Text<Operation> {
    private static Gson gson = new Gson();

    @Override
    public Operation decode(String s) throws DecodeException {
        return gson.fromJson(s, Operation.class);
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
