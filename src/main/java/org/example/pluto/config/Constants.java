package org.example.pluto.config;

import lombok.Getter;

@Getter
public enum Constants {
    GET_PRODUCT_SEARCH_TAGS_DELIMITER("/");

    private final String value;
    Constants(String value) {
        this.value = value;
    }
}
