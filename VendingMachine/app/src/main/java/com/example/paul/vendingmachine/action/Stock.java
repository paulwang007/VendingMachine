package com.example.paul.vendingmachine.action;


import com.example.paul.vendingmachine.factory.Item;

public interface Stock {
    void restockOneItem(Item item, int addNumber);
}
