import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestSearchProducts extends TestHelper {
    @DataProvider(name = "getUrls")
    public String[] getUrls() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit"
        };
    }

    @Test(dataProvider = "getUrls")
    public void test_search_products(String url) {
        System.out.println("Validating : " + url);
        String actualJsonResponse = getResponseFromURL(url);
        JSONObject expectedJsonResponse = getExpectedResponseFromResource("Search");

        try{
            JSONArray productsJson = (JSONArray) (new JSONParser()).parse(actualJsonResponse);
            for (Object product : productsJson) {
                Assert.assertTrue(isSimilarJson((JSONObject)product, expectedJsonResponse),
                        "JSON Not similar for product " + productsJson);
            }
        }
        catch (ParseException e){
            Assert.fail("Could not parse JSON\n\n" + actualJsonResponse);
        }
    }

    @DataProvider(name = "getUrlsWithGoodMatch")
    public String[] getUrlsWithGoodMatch() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana?good_match=true",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit?good_match=true",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana?good_match=true",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit?good_match=true"
        };
    }
    @Test(dataProvider = "getUrls")
    public void test_search_products_with_good_match(String url) {
        System.out.println("Validating : " + url);
        String actualJsonResponse = getResponseFromURL(url);
        JSONObject expectedJsonResponse = getExpectedResponseFromResource("Search");

        try{
            JSONArray productsJson = (JSONArray) (new JSONParser()).parse(actualJsonResponse);
            for (Object product : productsJson) {
                Assert.assertTrue(isSimilarJson((JSONObject)product, expectedJsonResponse),
                        "JSON Not similar for product " + productsJson);
                Assert.assertTrue(Double.parseDouble(((JSONObject) product).get("match").toString()) > 0,
                        "Match should be positive with good_match set to true");
            }
        }
        catch (ParseException e){
            Assert.fail("Could not parse JSON\n\n" + actualJsonResponse);
        }
    }
}
