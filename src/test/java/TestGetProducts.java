import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class TestGetProducts extends TestHelper{
    @DataProvider(name = "GetAllProducts")
    public String[] getProducts() {
        return new String[]{
                "http://localhost:8080/Pluto/GetProducts",
                "http://43.205.135.121:8080/Pluto/GetProducts"
        };
    }

    @Test(dataProvider = "GetAllProducts")
    public void test_get_all_products(String url) {
        System.out.println("Validating : " + url);
        String actualJsonResponse = getResponseFromURL(url);
        JSONObject expectedJsonResponse = getExpectedResponseFromResource("Product");

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
