package coders;

import com.google.gson.Gson;
import entities.Account;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

public class AccountDecoder implements Decoder.Text<Account>{
    private static Gson gson = new Gson();

    @Override
    public Account decode(String s) throws DecodeException {
        return gson.fromJson(s, Account.class);
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
