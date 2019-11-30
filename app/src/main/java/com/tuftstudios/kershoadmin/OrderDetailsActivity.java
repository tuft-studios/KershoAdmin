package com.tuftstudios.kershoadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Dish> dishesList;
    private DishesAdapter dishesAdapter;
    Order order;

    TextView timeTv, addressTv, mobileTv, subTotalTv, deliveryFeesTv, totalTx, canceledTv, discountTv;

    ImageView submittedImage, receivedImage, readyImage, deliveredImage, backToMain, cancelBtn, promoBtn;

    EditText promoCode, discount, expire;

    ConstraintLayout constraintLayout;

    int userType;

    Dialog dialog;

    Calendar myCalendar;

    DatePickerDialog.OnDateSetListener date;

    String expireString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        initiateViews();

        userType = SharedPrefManager.getInstance(this).getSavedUser().getType();


        if (getIntent().getSerializableExtra("order") != null){
            order = (Order) getIntent().getSerializableExtra("order");

            if (SharedPrefManager.getInstance(this).getSavedUser().getType() != 0){

                cancelBtn.setVisibility(View.GONE);
                promoBtn.setVisibility(View.GONE);
            }else if (order.getStatus() == 4 || order.getStatus() == 3) {

                cancelBtn.setVisibility(View.GONE);

            }else {
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setOnClickListener(this);

                promoBtn.setVisibility(View.VISIBLE);
                promoBtn.setOnClickListener(this);
            }

            setOrderData();


            dishesDetails();
        }



    }

    private void dishesDetails(){

        recyclerView = findViewById(R.id.recycler_cart2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        dishesAdapter = new DishesAdapter(OrderDetailsActivity.this, order.getDishesList(), order);
        recyclerView.setAdapter(dishesAdapter);

        /*
        int OrderId = order.getId();

//        Bundle extras = getIntent().getExtras();
//        if (intent != null) {
//            int OrderId = extras.getInt("OrderId");


        Call<DishesItem> call = RetrofitClient.getInstance().getApi().getDishesItem(OrderId);
        call.enqueue(new Callback<DishesItem>() {
            @Override
            public void onResponse(Call<DishesItem> call, Response<DishesItem> response) {

                dishesList = response.body().getDishes();
                dishesAdapter = new DishesAdapter(OrderDetailsActivity.this, dishesList, order);
                recyclerView.setAdapter(dishesAdapter);

            }

            @Override
            public void onFailure(Call<DishesItem> call, Throwable t) {

                Toast.makeText(OrderDetailsActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });*/

    }


    private void initiateViews() {
        // National ID, Password input text
        timeTv = findViewById(R.id.tv_delivery_time_sum);
        addressTv = findViewById(R.id.tv_address_sum);
        addressTv.setOnClickListener(this);
        mobileTv = findViewById(R.id.tv_phone_sum);
        mobileTv.setOnClickListener(this);
        subTotalTv = findViewById(R.id.tv_subtotal_sum);
        deliveryFeesTv = findViewById(R.id.tv_delivery_sum);
        totalTx = findViewById(R.id.tv_total_sum);
        discountTv = findViewById(R.id.tv_discount_sum);

        recyclerView = findViewById(R.id.recycler_cart2);


        submittedImage = findViewById(R.id.imageView);
        receivedImage = findViewById(R.id.imageView2);
        readyImage = findViewById(R.id.imageView3);
        deliveredImage = findViewById(R.id.imageView4);
        canceledTv = findViewById(R.id.canceled_tv);

        backToMain = findViewById(R.id.btn_back_to_main);
        backToMain.setOnClickListener(this);

        constraintLayout = findViewById(R.id.order_tracker_layout);

        cancelBtn = findViewById(R.id.cancelBtn);

        promoBtn = findViewById(R.id.promoBtn);


        myCalendar = Calendar.getInstance();

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                expireString = sdf.format(myCalendar.getTime());
                expire.setText(expireString);
            }

        };

    }


    private void setOrderData() {


        timeTv.setText(order.getOrderTime());

        if (userType != 1) {
            addressTv.setText(order.getFullAddress());
            mobileTv.setText(order.getMobile());
            subTotalTv.setText(order.getSubtotalPrice() + " EGP");
            deliveryFeesTv.setText(order.getDelivery() + " EGP");
            discountTv.setText(order.getDiscount() + "EGP");


            totalTx.setText(order.getTotalPrice() + " EGP");
        }else {

            addressTv.setVisibility(View.GONE);
            mobileTv.setVisibility(View.GONE);
            totalTx.setVisibility(View.GONE);

            findViewById(R.id.textView3).setVisibility(View.GONE);
            findViewById(R.id.textView4).setVisibility(View.GONE);
            findViewById(R.id.textView6).setVisibility(View.GONE);
            findViewById(R.id.cardView2).setVisibility(View.GONE);

            //findViewById(R.id.textView5)
        }

        switch (order.getStatus()){
            //waiting Status
            case 0:
                submittedImage.setImageResource(R.drawable.dot_and_circle);
                receivedImage.setImageResource(R.drawable.circle);
                readyImage.setImageResource(R.drawable.circle);
                deliveredImage.setImageResource(R.drawable.circle);
                if (userType == 0 || userType == 1) {
                    receivedImage.setOnClickListener(this);
                }
                break;
            //received Status
            case 1:
                submittedImage.setImageResource(R.drawable.circle);
                receivedImage.setImageResource(R.drawable.dot_and_circle);
                readyImage.setImageResource(R.drawable.circle);
                deliveredImage.setImageResource(R.drawable.circle);
                receivedImage.setOnClickListener(null);
                if (userType == 0 || userType == 1) {
                    readyImage.setOnClickListener(this);
                }
                break;
            //ready Status
            case 2:
                submittedImage.setImageResource(R.drawable.circle);
                receivedImage.setImageResource(R.drawable.circle);
                readyImage.setImageResource(R.drawable.dot_and_circle);
                deliveredImage.setImageResource(R.drawable.circle);
                readyImage.setOnClickListener(null);
                if (userType == 2) {
                    deliveredImage.setOnClickListener(this);
                }
                break;
            //delivered Status
            case 3:
                submittedImage.setImageResource(R.drawable.circle);
                receivedImage.setImageResource(R.drawable.circle);
                readyImage.setImageResource(R.drawable.circle);
                deliveredImage.setImageResource(R.drawable.dot_and_circle);
                readyImage.setOnClickListener(null);
                deliveredImage.setOnClickListener(null);
                break;
            //canceled Status
            case 4:
                constraintLayout.setVisibility(View.INVISIBLE);
                canceledTv.setVisibility(View.VISIBLE);
                break;

            default:
                submittedImage.setImageResource(R.drawable.dot_and_circle);
                receivedImage.setImageResource(R.drawable.circle);
                readyImage.setImageResource(R.drawable.circle);
                deliveredImage.setImageResource(R.drawable.circle);
                readyImage.setOnClickListener(this);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addressTv){


            double latitude = order.getLatitude();
            double longitude = order.getLongitude();
            String label = "Deliver Address!";
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent mapIntent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            startActivity(mapIntent);


        }else if (v == mobileTv){

            String mobile = order.getMobile();
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + mobile));
            startActivity(intent);
        }else if (v == receivedImage){

            submittedImage.setImageResource(R.drawable.circle);
            receivedImage.setImageResource(R.drawable.dot_and_circle);
            deliveredImage.setImageResource(R.drawable.circle);
            receivedImage.setOnClickListener(null);
            if (userType == 0 || userType == 1) {
                readyImage.setOnClickListener(this);
            }
            updateOrderStatus(1, order.getId());
        }else if (v == readyImage){

            receivedImage.setImageResource(R.drawable.circle);
            readyImage.setImageResource(R.drawable.dot_and_circle);
            deliveredImage.setImageResource(R.drawable.circle);
            readyImage.setOnClickListener(null);
            if (userType == 2) {
                deliveredImage.setOnClickListener(this);
            }

            if (userType == 1) {
                updateOrderStatus(2, order.getId());
            }else if(userType == 0){
                setOrderReady(order.getId());
            }
            //orderDetailsPresenter.updateOrderStatus(1, order.getId());
        }else if (v == deliveredImage){

            readyImage.setImageResource(R.drawable.circle);
            deliveredImage.setImageResource(R.drawable.dot_and_circle);
            deliveredImage.setOnClickListener(null);
            updateOrderStatus(3, order.getId());
        }else if (v == backToMain){

            Intent intent = new Intent(this, LoginActivity.class);
            //intent.putExtra("update", true);
            startActivity(intent);
        }else if (v == cancelBtn){
            final AlertDialog alertDialog = new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to cancel this order?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateOrderStatus(4, order.getId());
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
//            updateOrderStatus(4, order.getId());
        }else if (v == promoBtn){
            promoCodesPopup ();
        }
    }

    private void updateOrderStatus(final int status, int id) {

        Call<Result> call = RetrofitClient.getInstance().getApi().updateOrderStatus(id, status);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                showMessage(response.body().getMessage());

                if (status == 4){
                    constraintLayout.setVisibility(View.INVISIBLE);
                    canceledTv.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                showMessage(t.getMessage());

            }
        });
    }


    private void setOrderReady(int id) {

        Call<Result> call = RetrofitClient.getInstance().getApi().setOrderReady(id);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                showMessage(response.body().getMessage());

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                showMessage(t.getMessage());

            }
        });
    }

    public void showMessage(String message) {

        Toast.makeText(this, message,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        Intent intent = new Intent(this, LoginActivity.class);
        //intent.putExtra("update", true);
        startActivity(intent);
    }


    public void promoCodesPopup (){

        Button applyButton, cancelButton;

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.promocodes_popup);

        promoCode = dialog.findViewById(R.id.promocode_editText);
        discount = dialog.findViewById(R.id.discount_editText);
        expire = dialog.findViewById(R.id.date);
        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new DatePickerDialog(OrderDetailsActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });
        applyButton = dialog.findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String promo = promoCode.getText().toString();
                String discountText = discount.getText().toString();
                if (validatePromoCode(promo, discountText)){
                    addUserPromoCode(order.getUserId(), promo, discountText);
                }
            }
        });

        cancelButton = dialog.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addUserPromoCode(int UserId, String promo, String discountText) {
        Call<Result> call = RetrofitClient.getInstance().getApi().addUserPromoCode(UserId, promo, discountText, expireString);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {

                showMessage(response.body().getMessage());

                dialog.dismiss();

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                showMessage(t.getMessage());

            }
        });
    }

    private boolean validatePromoCode(String promo, String discountText) {
        boolean valid = true;


        if (promo.isEmpty()){

            promoCode.setError("enter Promocode");
            promoCode.requestFocus();
            valid = false;
        }else {
            promoCode.setError(null);
        }

        if (discountText.isEmpty()) {

            discount.setError("enter discount amount");
            discount.requestFocus();
            valid = false;
        } else if (Integer.parseInt(discountText) > 100) {
            discount.setError("More Than 100% ?!");
            discount.requestFocus();
            valid = false;
        } else {

            discount.setError(null);
        }

        return valid;
    }
}
