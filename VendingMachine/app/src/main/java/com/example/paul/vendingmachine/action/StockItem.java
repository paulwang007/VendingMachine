package com.example.paul.vendingmachine.action;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.paul.vendingmachine.factory.Item;
import com.example.paul.vendingmachine.utils.Constants;

import java.lang.ref.WeakReference;

public class StockItem implements Stock{

    private final WeakReference<Context> mWeakContext;
    private final SharedPreferences pref;

    public StockItem(Context context) {
        mWeakContext = new WeakReference<>(context);
        pref = context.getSharedPreferences(Constants.PACKAGE_NAME, Context.MODE_PRIVATE);

        // First time turn on the vending machine,stock is full
        if (!pref.contains(Constants.KEY_HAS_STOCK)) {
            pref.edit()
                    .putBoolean(Constants.KEY_HAS_STOCK, true)
                    .putInt(Constants.CHIP, Constants.INITIAL_STOCK)
                    .putInt(Constants.SODA, Constants.INITIAL_STOCK)
                    .putInt(Constants.COFFEE, Constants.INITIAL_STOCK)
                    .apply();
        }
    }

    public void dispenseOneItem(Item item) {
        int currentStock = pref.getInt(item.getName(), 0);
        if (currentStock >= 1) {
            pref.edit().putInt(item.getName(), --currentStock);
        }
    }

    @Override
    public void restockOneItem(Item item, int addNumber) {
        if (addNumber <= 0) return;

        int currentStock = pref.getInt(item.getName(), 0);
        currentStock += addNumber;
        if (currentStock >= 0) {
            pref.edit().putInt(item.getName(), currentStock);
        }
    }

}
