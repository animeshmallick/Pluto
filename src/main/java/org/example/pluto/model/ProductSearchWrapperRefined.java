package org.example.pluto.model;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public class ProductSearchWrapperRefined {
    private final ArrayList<ProductSearchWrapper> primary;
    private final ArrayList<ProductSearchWrapper> secondary;

    public ProductSearchWrapperRefined() {
        this.primary = new ArrayList<>();
        this.secondary = new ArrayList<>();
    }
    public void AddPrimary(ProductSearchWrapper wrapper) {
        primary.add(wrapper);
    }
    public void AddSecondary(ProductSearchWrapper wrapper) {
        secondary.add(wrapper);
    }
}
