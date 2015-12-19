package com.example.paul.vendingmachine.factory;

import android.text.TextUtils;

public class DispenseFactory {

    private final static int PRICE_FOR_CHIP = 100;
    private final static int PRICE_FOR_SODA = 50;
    private final static int PRICE_FOR_COFFEE = 200;

    public final static String CHIP = "chip";
    public final static String SODA = "soda";
    public final static String COFFEE = "coffee";


    public DispenseFactory() {

    }

    public Item getItem(String item) {

        if (TextUtils.isEmpty(item)) return null;

        if (item.equalsIgnoreCase(CHIP)) {
            return new Chip(PRICE_FOR_CHIP);
        } else if (item.equalsIgnoreCase(SODA)) {
            return new Soda(PRICE_FOR_SODA);
        } else if (item.equalsIgnoreCase(COFFEE)) {
            return new Coffee(PRICE_FOR_COFFEE);
        }

        return null;

    }
}
