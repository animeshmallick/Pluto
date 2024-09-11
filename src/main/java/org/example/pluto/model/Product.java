package org.example.pluto.model;
import lombok.Getter;

@Getter
public class Product {
    private final int id;
    private final String name;
    private final String description;
    private final int inventoryCount;
    private final boolean isBuyable;
    private final boolean isRestricted;
    private final String imageKey;
    private final String[] keywords;
    private final String[] tags;
    private final String unit;
    private final double unitCost;
    private final double unitDiscount;
    private final double unitMrp;

    public Product(double unitDiscount, String imageKey, double unitCost,
                   boolean isRestricted, String[] tags, String description,
                   double unitMrp, String[] keywords, int inventoryCount,
                   String unit, int id, boolean isBuyable, String name) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.inventoryCount = inventoryCount;
        this.isBuyable = isBuyable;
        this.isRestricted = isRestricted;
        this.imageKey = imageKey;
        this.keywords = keywords;
        this.tags = tags;
        this.unit = unit;
        this.unitCost = unitCost;
        this.unitDiscount = unitDiscount;
        this.unitMrp = unitMrp;
    }

}
