package com.example.paul.vendingmachine.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.paul.vendingmachine.R;
import com.example.paul.vendingmachine.action.BuyItem;
import com.example.paul.vendingmachine.action.StockItem;
import com.example.paul.vendingmachine.factory.DispenseFactory;
import com.example.paul.vendingmachine.factory.Item;
import com.example.paul.vendingmachine.money.Balance;
import com.example.paul.vendingmachine.utils.Constants;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Balance mBalanceObject;
    private Item mItem;
    private StockItem mStockItem;
    private Button mOneCent;
    private Button mFiveCent;
    private Button mTenCent;
    private Button mTwentyFiveCent;
    private Button mOneDollar;
    private Button mFiveDollar;
    private Button mDispenseButton;
    private TextView mCurrentBalanceView;
    private TextView mDispenseChangeView;
    private TextView mMessageView;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find views;
        findViews();

        // Set listeners for buttons
        setButtonListeners();

        // Setup objects
        setDataAndObject();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setDataAndObject() {
        mBalanceObject = new Balance(mCurrentBalanceView);
        mStockItem = new StockItem(this);
    }

    private void findViews() {
        mOneCent = (Button) findViewById(R.id.button_1_cent);
        mFiveCent = (Button) findViewById(R.id.button_5_cent);
        mTenCent = (Button) findViewById(R.id.button_10_cent);
        mTwentyFiveCent = (Button) findViewById(R.id.button_25_cent);
        mOneDollar = (Button) findViewById(R.id.button_1_dollar);
        mFiveDollar = (Button) findViewById(R.id.button_5_dollar);
        mDispenseButton = (Button) findViewById(R.id.dispense_button);

        mCurrentBalanceView = (TextView) findViewById(R.id.current_balance);
        mDispenseChangeView = (TextView) findViewById(R.id.dispense_change);
        mMessageView = (TextView) findViewById(R.id.message);

        mImageView = (ImageView) findViewById(R.id.vending_machine_image);
    }
    private void setButtonListeners() {

        mOneCent.setOnClickListener(this);
        mFiveCent.setOnClickListener(this);
        mTenCent.setOnClickListener(this);
        mTwentyFiveCent.setOnClickListener(this);
        mOneDollar.setOnClickListener(this);
        mFiveDollar.setOnClickListener(this);
        mDispenseButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        // Reset image and value
        reset();

        if (mBalanceObject == null) {
            mBalanceObject = new Balance(mCurrentBalanceView);
        }
        switch (v.getId()) {
            case R.id.button_1_cent:
                mBalanceObject.addBalance(Constants.ONE_CENT);
                break;
            case R.id.button_5_cent:
                mBalanceObject.addBalance(Constants.FIVE_CENT);
                break;
            case R.id.button_10_cent:
                mBalanceObject.addBalance(Constants.TEN_CENT);
                break;
            case R.id.button_25_cent:
                mBalanceObject.addBalance(Constants.TWENTY_FIVE_CENT);
                break;
            case R.id.button_1_dollar:
                mBalanceObject.addBalance(Constants.ONE_DOLLAR);
                break;
            case R.id.button_5_dollar:
                mBalanceObject.addBalance(Constants.FIVE_DOLLAR);
                break;
            case R.id.dispense_button:
                // Display a list of items for user to choose
                popItemList();
                break;
            default:
                break;

        }
    }

    private void popItemList() {

        final DispenseFactory factory = new DispenseFactory();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setTitle("Pick a item")
                .setItems(R.array.item_list, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Chip
                                dispense(factory.getItem(Constants.CHIP));
                                break;
                            case 1:
                                // Soda
                                dispense(factory.getItem(Constants.SODA));
                                break;
                            case 2:
                                // Coffee
                                dispense(factory.getItem(Constants.COFFEE));
                                break;
                            case 3:
                                // Cancel Transaction
                                dispense(null);
                            default:
                                break;
                        }
                    }
                }).show();
    }

    private void dispense(final Item item) {

        BuyItem buyItem = null;
        try {
            buyItem = new BuyItem(mBalanceObject, item, mStockItem,
                    mCurrentBalanceView, mDispenseChangeView, mMessageView, mImageView);
        } catch (IllegalArgumentException e) {
            Log.e("Exception", e.getMessage());
        }

        if (buyItem != null) {
            buyItem.dispenseItem();
        }

        mBalanceObject = null;

    }

    private void reset() {

        mImageView.setImageResource(R.drawable.bear_utique_v);

        mDispenseChangeView.setText(R.string.change_balance);

        mMessageView.setText("");
    }


}
