package com.example.paul.vendingmachine.money;

import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Created by paul on 12/17/15.
 */
public class Balance {

    private int mCurrentBalance;
    private WeakReference<TextView> mWeakBalanceView;

    // Start of balance should always be 0
    public Balance(TextView balanceView) {
        mCurrentBalance = 0;
        mWeakBalanceView = new WeakReference<>(balanceView);
    }

    public void addBalance(int addition) {

        mCurrentBalance += addition;

        setView(mCurrentBalance);

    }

    public void minusBalance(int subtraction) {

        mCurrentBalance -= subtraction;

        setView(mCurrentBalance);

    }

    public int getBalance() {
        return mCurrentBalance;
    }

    private void setView(int balance) {
        TextView balanceView = mWeakBalanceView.get();
        if (balanceView != null) {
            balanceView.setText("Current Balance: " + balance + " cents");
        }
    }

}
