package coders;

import com.google.gson.Gson;
import entities.Account;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class AccountEncoder  implements Encoder.Text<Account>  {
    private static Gson gson = new Gson();

    @Override
    public String encode(Account object) throws EncodeException {
        return gson.toJson(object);    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
