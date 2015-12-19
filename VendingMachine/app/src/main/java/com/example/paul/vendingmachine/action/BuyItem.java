package com.example.paul.vendingmachine.action;


import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.vendingmachine.R;
import com.example.paul.vendingmachine.factory.Item;
import com.example.paul.vendingmachine.money.Balance;
import com.example.paul.vendingmachine.utils.Constants;

import java.lang.ref.WeakReference;


public class BuyItem implements Buy {

    private Balance mBalanceObject;
    private int mCurrentBalance;
    private Item mItem;
    private StockItem mStockItem;
    private WeakReference<TextView> mWeakCurrentBalance;
    private WeakReference<TextView> mWeakChangeBalance;
    private WeakReference<TextView> mWeakMessage;
    private WeakReference<ImageView> mWeakImageView;

    private final static String CHANGE_BALANCE = "Change Balance: ";
    private final static String FIVE_DOLLAR_BILL = " $5: ";
    private final static String ONE_DOLLAR_BILL = " $1: ";
    private final static String ONE_QUARTER = " 25¢: ";
    private final static String ONE_DIME = " 10¢: ";
    private final static String ONE_NICKEL = " 5¢: ";
    private final static String ONE_PENNY = " 1¢: ";




    public BuyItem(Balance balance, Item item, StockItem stockItem, TextView currentBalance,
                   TextView changeBalance, TextView message, ImageView imageView) {

        if (balance == null) {
            throw new IllegalArgumentException("balance can not be null");
        }

        if (currentBalance == null || changeBalance == null || message == null) {
            throw new IllegalArgumentException("view can not be null");
        }

        mBalanceObject = balance;
        mCurrentBalance = balance.getBalance();
        mItem = item;
        mStockItem = stockItem;
        mWeakCurrentBalance = new WeakReference<>(currentBalance);
        mWeakChangeBalance = new WeakReference<>(changeBalance);
        mWeakMessage = new WeakReference<>(message);
        mWeakImageView = new WeakReference<>(imageView);

    }

    /**
     * Return what is in the mCurrentBalance
     */
    private void returnChange() {

        if (mBalanceObject == null) return;

        mBalanceObject.minusBalance(mCurrentBalance);

        TextView changeBalance = mWeakChangeBalance.get();
        if (changeBalance != null) {

            int fiveDollar;
            int oneDollar;
            int quarter;
            int dime;
            int nickel;
            int penny;
            StringBuilder builder = new StringBuilder();
            builder.append(CHANGE_BALANCE);

            fiveDollar = mCurrentBalance / Constants.FIVE_DOLLAR;
            if (fiveDollar >= 1) {
                mCurrentBalance -= fiveDollar * Constants.FIVE_DOLLAR;
                builder.append(FIVE_DOLLAR_BILL).append(fiveDollar);
            }

            oneDollar = mCurrentBalance / Constants.ONE_DOLLAR;
            if (oneDollar >= 1) {
                mCurrentBalance -= oneDollar * Constants.ONE_DOLLAR;
                builder.append(ONE_DOLLAR_BILL).append(oneDollar);
            }

            quarter = mCurrentBalance / Constants.TWENTY_FIVE_CENT;
            if (quarter >= 1) {
                mCurrentBalance -= quarter * Constants.TWENTY_FIVE_CENT;
                builder.append(ONE_QUARTER).append(quarter);
            }

            dime = mCurrentBalance / Constants.TEN_CENT;
            if (dime >= 1) {
                mCurrentBalance -= dime * Constants.TEN_CENT;
                builder.append(ONE_DIME).append(dime);
            }

            nickel = mCurrentBalance / Constants.FIVE_CENT;
            if (nickel >= 1) {
                mCurrentBalance -= nickel * Constants.FIVE_CENT;
                builder.append(ONE_NICKEL).append(nickel);
            }

            penny = mCurrentBalance / Constants.ONE_CENT;
            if (penny >= 1) {
                mCurrentBalance -= penny * Constants.ONE_CENT;
                builder.append(ONE_PENNY).append(penny);
            }

            changeBalance.setText(builder.toString());

        }

    }

    /**
     * Release item in this method.
     * Current balance is also changed in this method
     */
    private void releaseItem() {

        // When user canceled transaction
        if (mItem == null) {
            return;
        }

        if (mCurrentBalance < mItem.getPrice()) {
            TextView message = mWeakMessage.get();
            if (message != null) {
                message.setText(R.string.insufficient_fund);
                return;
            }
        }

        if (mItem.getStock() <= 0 ) {
            TextView message = mWeakMessage.get();
            if (message != null) {
                message.setText(R.string.out_of_stock);
                return;
            }
        }

        // Calculate change
        mCurrentBalance -= mItem.getPrice();

        // Release item
        TextView message = mWeakMessage.get();
        if (message != null) {
            ImageView imageView = mWeakImageView.get();

            if (Constants.CHIP.equalsIgnoreCase(mItem.getName())) {
                imageView.setImageResource(R.drawable.chip);
            } else if (Constants.SODA.equalsIgnoreCase(mItem.getName())) {
                imageView.setImageResource(R.drawable.soda);
            } else if (Constants.COFFEE.equalsIgnoreCase(mItem.getName())) {
                imageView.setImageResource(R.drawable.coffee);
            }

            message.setText(R.string.item_dispensed);

            // Update stock
            mStockItem.dispenseOneItem(mItem);

        }
    }

    private void terminateTransaction() {

        // Reset values
        TextView currentBalance = mWeakCurrentBalance.get();
        if (currentBalance != null) {
            currentBalance.setText(R.string.current_balance);
        }

        // Reset objects to avoid memory leaks
        mBalanceObject = null;

    }

    @Override
    public void dispenseItem() {
        releaseItem();

        returnChange();

        terminateTransaction();
    }


}
