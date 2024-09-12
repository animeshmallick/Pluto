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

    @DataProvider(name = "getUrlsWithGoodMatchAsTrue")
    public String[] getUrlsWithGoodMatchAsTrue() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana?good_match=true",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit?good_match=true",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana?good_match=true",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit?good_match=true"
        };
    }
    @Test(dataProvider = "getUrlsWithGoodMatchAsTrue")
    public void test_search_products_with_good_match_as_true(String url) {
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

    @DataProvider(name = "getUrlsWithInvalidGoodMatch")
    public String[] getUrlsWithInvalidGoodMatch() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana?good_match=false",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit?good_match=xyz",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana?good_match=false",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit?good_match=xyz"
        };
    }
    @Test(dataProvider = "getUrlsWithInvalidGoodMatch")
    public void test_search_products_with_invalid_good_match(String url) {
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

    @DataProvider(name = "getUrlsWithTopMatchAsInclude")
    public String[] getUrlsWithTopMatchAsInclude() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana?top_match=include",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit?top_match=include",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana?top_match=include",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit?top_match=include"
        };
    }
    @Test(dataProvider = "getUrlsWithTopMatchAsInclude")
    public void test_search_products_with_top_match_as_include(String url) {
        System.out.println("Validating : " + url);
        String actualJsonResponse = getResponseFromURL(url);
        JSONObject expectedJsonResponse = getExpectedResponseFromResource("Search");

        try{
            JSONObject productsJson = (JSONObject) (new JSONParser()).parse(actualJsonResponse);
            Assert.assertEquals(productsJson.size(), 2,
                    "GetProduct response with top_match=include should have exactly 2 keys");
            Assert.assertTrue(productsJson.containsKey("primary") && productsJson.containsKey("secondary"),
                    "GetProduct response with top_match=include should have key as primary and secondary");

            JSONArray primaryProductsJson = (JSONArray) productsJson.get("primary");
            double strict_percentage = 0;
            for (Object product : primaryProductsJson) {
                Assert.assertTrue(isSimilarJson((JSONObject)product, expectedJsonResponse),
                        "JSON Not similar for product " + productsJson);
                strict_percentage = Math.max(strict_percentage, (double)((JSONObject) product).get("match"));
            }

            JSONArray secondaryProductsJson = (JSONArray) productsJson.get("secondary");
            for (Object product : secondaryProductsJson) {
                Assert.assertTrue(isSimilarJson((JSONObject)product, expectedJsonResponse),
                        "JSON Not similar for product " + productsJson);
                Assert.assertTrue((double)(((JSONObject) product).get("match")) < strict_percentage/2.0);
            }
        }
        catch (ParseException e){
            Assert.fail("Could not parse JSON\n\n" + actualJsonResponse);
        }
    }

    @DataProvider(name = "getUrlsWithTopMatchAsStrict")
    public String[] getUrlsWithTopMatchAsStrict() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts/banana?top_match=strict",
                "http://localhost:8080/Pluto/GetProducts/banana/fruit?top_match=strict",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana?top_match=strict",
                "http://43.205.135.121:8080/Pluto/GetProducts/banana/fruit?top_match=strict"
        };
    }
    @Test(dataProvider = "getUrlsWithTopMatchAsStrict")
    public void test_search_products_with_top_match_as_strict(String url) {
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
}
