package org.example.pluto.common;

import org.example.pluto.config.LambdaEndpoints;
import org.example.pluto.model.Product;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class GetProductsLambdaFunction extends Common {
    private final String getProductLambdaEndPoint;

    public GetProductsLambdaFunction(){
        this.getProductLambdaEndPoint = LambdaEndpoints.GetProducts.getEndpoint();
    }
    public ArrayList<Product> getProducts() {
        return getProductsFromString(Common.getResponseFromURL(getProductLambdaEndPoint));
    }
    private ArrayList<Product> getProductsFromString(String allProductString) {
        JSONArray products;
        try {
            products = (JSONArray) (new JSONParser()).parse(allProductString);
        }catch (ParseException e){
            return null;
        }
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
}
