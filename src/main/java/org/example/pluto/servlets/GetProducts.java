package org.example.pluto.servlets;

import com.google.gson.Gson;
import org.example.pluto.common.GetProductsLambdaFunction;
import org.example.pluto.config.Constants;
import org.example.pluto.config.GetProductCustomParameters;
import org.example.pluto.model.Product;
import org.example.pluto.model.ProductSearchWrapper;
import org.example.pluto.model.ProductSearchWrapperRefined;
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
import java.util.List;

@WebServlet(name = "GetProducts", value = "/GetProducts/*")
public class GetProducts extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String allProductString = (new GetProductsLambdaFunction()).getAllProducts();

        ArrayList<Product> products;
        try {
            products = getAllProducts(allProductString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        if (!request.getParameterMap().containsKey(GetProductCustomParameters.INCLUDE_OUT_OF_STOCK.name().toLowerCase()) ||
                !request.getParameter(GetProductCustomParameters.INCLUDE_OUT_OF_STOCK.name().toLowerCase()).equals("true")) {
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getInventoryCount() <= 0) {
                    products.remove(i);
                    i--;
                }
            }
        }
        String[] search_key = getSearchKey(request.getPathInfo());
        if (search_key == null || search_key.length == 0)
            response.getWriter().println((new Gson()).toJson(products));
        else {
            ArrayList<ProductSearchWrapper> productSearchWrappers = new ArrayList<>();
            boolean responded = false;
            for (Product product : products) {
                productSearchWrappers.add(new ProductSearchWrapper(product, search_key));
            }
            productSearchWrappers.sort((w1, w2) -> (int) (w2.getMatch() - w1.getMatch()));
            ArrayList<ProductSearchWrapper> productSearchWrappersClone = new ArrayList<>(List.copyOf(productSearchWrappers));

            if (request.getParameterMap().containsKey(GetProductCustomParameters.GOOD_MATCH.name().toLowerCase()) &&
                    request.getParameter(GetProductCustomParameters.GOOD_MATCH.name().toLowerCase()).equals("true")) {
                for (int i = 0; i < productSearchWrappersClone.size(); i++) {

                    if (productSearchWrappersClone.get(i).getMatch() <= 0) {
                        productSearchWrappersClone.remove(i);
                        i--;
                    }
                }
            }
            if (request.getParameterMap().containsKey(GetProductCustomParameters.TOP_MATCH.name().toLowerCase()) &&
                    request.getParameter(GetProductCustomParameters.TOP_MATCH.name().toLowerCase()).equals("strict")) {
                double topMatchPercent = productSearchWrappers.get(0).getMatch() / 2.0;
                for (int i = 0; i < productSearchWrappersClone.size(); i++) {
                    if (productSearchWrappersClone.get(i).getMatch() < topMatchPercent) {
                        productSearchWrappersClone.remove(i);
                        i--;
                    }
                }
            }
            if (request.getParameterMap().containsKey(GetProductCustomParameters.TOP_MATCH.name().toLowerCase()) &&
                    request.getParameter(GetProductCustomParameters.TOP_MATCH.name().toLowerCase()).equals("include")) {
                double topMatchPercent = productSearchWrappers.get(0).getMatch() / 2.0;
                ProductSearchWrapperRefined productSearchWrappersRefined = new ProductSearchWrapperRefined();
                for (ProductSearchWrapper wrapper : productSearchWrappersClone) {
                    if (wrapper.getMatch() < topMatchPercent)
                        productSearchWrappersRefined.AddSecondary(wrapper);
                    else
                        productSearchWrappersRefined.AddPrimary(wrapper);
                }
                response.getWriter().println((new Gson()).toJson(productSearchWrappersRefined));
                responded = true;
            }
            if(!responded)
                response.getWriter().println((new Gson()).toJson(productSearchWrappersClone));
        }
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
