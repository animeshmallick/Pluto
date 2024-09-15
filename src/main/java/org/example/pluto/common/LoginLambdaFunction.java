package org.example.pluto.common;

import org.example.pluto.config.LambdaEndpoints;
import org.example.pluto.model.User;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LoginLambdaFunction extends Common {
    private final String loginLambdaEndPoint;
    private final String validateUserLambdaEndPoint;

    public LoginLambdaFunction() {
        this.loginLambdaEndPoint = LambdaEndpoints.Login.getEndpoint();
        this.validateUserLambdaEndPoint = LambdaEndpoints.ValidateUser.getEndpoint();
    }

    public User login(String username, String password) {
        String loginLambdaUrl = String.format("%s/%s/%s", loginLambdaEndPoint, username, password);
        return getLoginUser(Common.getResponseFromURL(loginLambdaUrl));
    }

    private User getLoginUser(String response) {
        JSONObject user = null;
        try {
            user = (JSONObject) (new JSONParser()).parse(response);
        }catch (ParseException e){////
        }
        if (user == null || !user.containsKey("id")) {return null;}

        return new User(user.get("first_name").toString(), user.get("last_name").toString(),
                        user.get("username").toString(), Integer.parseInt(user.get("age").toString()),
                        user.get("email").toString(), user.get("phone").toString(),
                        user.get("type").toString());
    }

    public User validateUser(String username){
        String validateUserUrl = String.format("%s/%s", validateUserLambdaEndPoint, username);
        return getLoginUser(Common.getResponseFromURL(validateUserUrl));
    }
}
