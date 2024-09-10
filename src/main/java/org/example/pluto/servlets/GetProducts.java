package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.example.pluto.common.GetProductsLambdaFunction;
import org.example.pluto.config.Constants;
import org.example.pluto.model.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "GetProducts", value = "/GetProducts/*")
public class GetProducts extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getPathInfo());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String allProductString = (new GetProductsLambdaFunction()).getAllProducts();

        ArrayList<Product> products;
        try {
            products = getAllProducts(allProductString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String[] search_key = getSearchKey(request.getPathInfo());
        if(search_key == null || search_key.length == 0)
            response.getWriter().println((new Gson()).toJson(products));
        else
            response.getWriter().println((new Gson()).toJson(search_key));

    }

    private ArrayList<Product> getAllProducts(String allProductString) throws ParseException {
        JSONArray products = (JSONArray) (new JSONParser()).parse(allProductString);
        ArrayList<Product> productList = new ArrayList<>();
        for (Object obj : products) {
            JSONObject productJSON = (JSONObject) obj;

            productList.add(
                    new Product(
                            Double.parseDouble(productJSON.get("productUnitDiscount").toString()),
                            productJSON.get("productImageKey").toString(),
                            Double.parseDouble(productJSON.get("productUnitCost").toString()),
                            Boolean.parseBoolean(productJSON.get("productIsRestricted").toString()),
                            getStringArray(productJSON.get("productTags").toString()),
                            productJSON.get("productDescription").toString(),
                            Double.parseDouble(productJSON.get("productUnitMRP").toString()),
                            getStringArray(productJSON.get("productKeywords").toString()),
                            Integer.parseInt(productJSON.get("productInventory").toString()),
                            productJSON.get("productUnit").toString(),
                            Integer.parseInt(productJSON.get("productId").toString()),
                            Boolean.parseBoolean(productJSON.get("productIsBuyable").toString()),
                            productJSON.get("productName").toString()
                    ));
        }
        return productList;
    }

    private String[] getStringArray(String str){
        str = str.replace("[", "")
                .replace("]", "")
                .replace("\"", "");
        String[] temp = str.split(",");
        ArrayList<String> result = new ArrayList<>();
        for (String k : temp) {
            result.add(k.trim());
        }
        return result.toArray(new String[0]);
    }

    private String[] getSearchKey(String pathInfo) {
        if (pathInfo == null)
            return null;
        if (pathInfo.length() <= 1)
            return null;
        String[] keys = pathInfo.split(Constants.GET_PRODUCT_SEARCH_TAGS_DELIMITER.getValue());
        ArrayList<String> searchKeys = new ArrayList<>();
        for (String k : keys) {
            if (k != null && !k.trim().isEmpty() && !searchKeys.contains(k.trim())) {
                searchKeys.add(k.trim());
            }
        }
        return searchKeys.toArray(new String[0]);
    }
}
