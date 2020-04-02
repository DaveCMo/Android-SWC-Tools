package com.swctools.common.enums;

public enum LayoutFavouriteTypes {

    TOPLAYOUTS("Top Layouts"),
    FAVOURITELAYOUTS("Favourite Layouts"),
    LASTUSED("Last Used"),
    MOSTUSED("Most Used");

    private final String name;

    LayoutFavouriteTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
