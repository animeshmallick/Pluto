import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

@Slf4j
public class TestHelper {
    public String getResponseFromURL(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Cookie", "auth_user_id=admin@92668751");

            Assert.assertEquals(connection.getResponseCode(), 200, "ResponseCode not as expected");

            StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }
            return response.toString();
        } catch (IOException e) {return e.getMessage();}
    }
    protected JSONObject getExpectedResponseFromResource(String responseFile){
        JSONObject expectedJsonResponse = null;
        try {
            expectedJsonResponse = (JSONObject) (new JSONParser()).parse(new FileReader(System.getProperty("user.dir")+ String.format("\\src\\test\\resources\\%s.json", responseFile)));
        }catch(ParseException | IOException e){
            log.error(String.valueOf(e));
            Assert.fail("Failed to parse expected JSON response");
        }
        return expectedJsonResponse;
    }

    protected boolean isSimilarJson(JSONObject actual, JSONObject expected) {
        if (actual.size() != expected.size()) {
            System.out.println(actual + " != " + expected);
            return false;
        }
        for (Object key : actual.keySet()) {
            if (!expected.containsKey(key)) {
                System.out.println("key not found "+ key);
                return false;
            }
            Object value = actual.get(key);
            if (value instanceof JSONArray){
                if(validateJsonArray((JSONArray) value, (JSONArray) expected.get(key))) {
                    System.out.println("JSON Array not equals" + value);
                    return false;
                }
            }
            else if(value instanceof JSONObject){
                if(!isSimilarJson((JSONObject) value, (JSONObject) expected.get(key))) {
                    System.out.println("JSON Array not equals" + value);
                    return false;
                }
            }
            else {
                if (value == null) {
                    System.out.println("value is null");
                    return false;
                }
            }
        }
        return true;
    }
    private boolean validateJsonArray(JSONArray actual, JSONArray expected) {
        if(!expected.isEmpty()){
            if (actual.size() != expected.size()) {
                System.out.println("Actual array size not equals" + actual + " != " + expected);
                return true;
            }
        }

        for(int i=0; i<actual.size(); i++){
            if (actual.get(i) instanceof JSONArray) {
                if (validateJsonArray((JSONArray) actual.get(i), (JSONArray) expected.get(i)))
                    return true;
            }
            else if (actual.get(i) instanceof JSONObject) {
                if (isSimilarJson((JSONObject) actual.get(i), (JSONObject) expected.get(i)))
                    return true;
            }
            else {
                if (actual.get(i) == null)
                    return true;
            }
        }
        return false;
    }
}
