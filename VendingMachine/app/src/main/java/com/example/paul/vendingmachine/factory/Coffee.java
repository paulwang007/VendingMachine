package com.example.paul.vendingmachine.factory;

import com.example.paul.vendingmachine.utils.Constants;


public class Coffee implements Item {

    private final int mUnitPrice;

    public Coffee(int price) {
        mUnitPrice = price;
    }

    @Override
    public int getPrice() {
        return mUnitPrice;
    }

    @Override
    public int getStock() {
        // Return how many items are currently in the vending machine
        return 1;
    }

    @Override
    public String getName() {
        return Constants.COFFEE;
    }

}
