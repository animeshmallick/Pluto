package org.example.pluto.model;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ProductSearchWrapper {
    private final Product product;
    private final String[] searchKeys;
    private final double match;

    public ProductSearchWrapper(Product product, String[] searchKeys) {
        this.product = product;
        this.searchKeys = searchKeys;
        this.match = getProductMatch(product, searchKeys);
    }

    private double getProductMatch(Product product, String[] searchKeys) {
        ArrayList<String> productKeys = new ArrayList<>();

        String[] keywords = product.getKeywords();
        for (String key : keywords){
            for (String k : key.split(" ")){
                if (!productKeys.contains(k) && !k.isEmpty() && !k.isBlank()){
                    productKeys.add(k);
                }
            }
        }

        String[] tags = product.getTags();
        for (String tag : tags){
            for (String t : tag.split(" ")){
                if (!productKeys.contains(t) && !t.isEmpty() && !t.isBlank()){
                    productKeys.add(t);
                }
            }
        }

        String name = product.getName();
        for (String n : name.split(" ")){
            if (!productKeys.contains(n) && !n.isEmpty() && !n.isBlank()){
                productKeys.add(n);
            }
        }
        double sum = 0;
        for(String sKey : searchKeys){
            double power = 1000.00;
            for(String pkey : productKeys){
                power = Math.min(power, levenshteinDistance(pkey, sKey));
            }
            sum += (1 - power/sKey.length())*100;
        }
        return Math.round(sum / searchKeys.length * 100.0) / 100.0;
    }

    private int levenshteinDistance(String s1, String s2) {
        return levenshteinDistance(s1.toLowerCase(), s2.toLowerCase(), s1.length(), s2.length());
    }

    private int levenshteinDistance(String str1, String str2, int m, int n) {
        if (m == 0)
            return n;
        if (n == 0)
            return m;
        if (str1.charAt(m - 1) == str2.charAt(n - 1))
            return levenshteinDistance(str1, str2, m - 1, n - 1);
        return 1 + Math.min(levenshteinDistance(str1, str2, m, n - 1),
                            Math.min(levenshteinDistance(str1, str2, m - 1, n),
                                    levenshteinDistance(str1, str2, m - 1, n - 1))
        );
    }
}
