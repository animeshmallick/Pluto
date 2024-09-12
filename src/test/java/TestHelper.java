import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class TestHelper {
    public String getResponseFromURL(String url) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest api_request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> api_response;
        try {
            api_response = client.send(api_request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
        return api_response.body();
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
                if(!validateJsonArray((JSONArray) value, (JSONArray) expected.get(key))) {
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
                return false;
            }
        }

        for(int i=0; i<actual.size(); i++){
            if (actual.get(i) instanceof JSONArray) {
                if (!validateJsonArray((JSONArray) actual.get(i), (JSONArray) expected.get(i)))
                    return false;
            }
            else if (actual.get(i) instanceof JSONObject) {
                if (!isSimilarJson((JSONObject) actual.get(i), (JSONObject) expected.get(i)))
                    return false;
            }
            else {
                if (actual.get(i) == null)
                    return false;
            }
        }
        return true;
    }
}
