package com.tuftstudios.kershoadmin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    String expireString;
    EditText promocode, discount, expire;
    Dialog dialog;
//    RadioGroup radioGroup;

    private static final String TAG = "MainActivity";


    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        radioGroup = dialog.findViewById(R.id.switch1_group);
//        radioGroup.setEnabled(false);

//        dialog = new Dialog(this);

        if (getIntent().getExtras() != null){
            String orderID = getIntent().getStringExtra("order_id");
            Toast.makeText(this, orderID, Toast.LENGTH_LONG).show();
        }

        BottomNavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relativeCont, new CurOrdersFragment()).commit();


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
                expire.setError(null);
                expire.setText(expireString);
            }

        };

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        if (task.getResult() != null) {
                            token = task.getResult().getToken();

                            if (!token.equals(SharedPrefManager.getInstance(MainActivity.this).getUserToken())){

                                SharedPrefManager.getInstance(MainActivity.this).setUserToken(token);

                                saveToken(token, SharedPrefManager.getInstance(MainActivity.this).getSavedUser().getId());
                            }

                        }

                    }
                });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment fragment = null;

                    switch (menuItem.getItemId()){
                        case R.id.menuCurOrders:
                            fragment = new CurOrdersFragment();
                            break;
                        case R.id.menuPreOrders:
                            fragment = new PreOrdersFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.relativeCont, fragment).commit();

                    return true;
                }
            };



    //here we check if user is NOT logged in then direct user to main activity to login
    @Override
    protected void onStart() {
        super.onStart();

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }



//    Overflow menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.overflow_menu, menu);

//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        SearchView searchView = (SearchView) searchItem.getActionView();

        return true;
    }

//   preparing items visibility

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        MenuItem menuItem = menu.findItem(R.id.adminControl);

        if(SharedPrefManager.getInstance(this).getSavedUser().getType() == 0){
            menuItem.setVisible(true);
        }else
            menuItem.setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

//    menu items and its cases
    //TODO: remove the toasts below

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.kitchenControl:
                Toast.makeText(this, "kitchen control item pressed", Toast.LENGTH_LONG).show();

                kitchenControlPopup();
                return true;
            case R.id.promocodes:
                Toast.makeText(this, "promocodes item pressed", Toast.LENGTH_LONG).show();

                promocodesPopup();
                return true;
            case R.id.complains:
                Toast.makeText(this, "complains item pressed", Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                saveToken(SharedPrefManager.getInstance(MainActivity.this).getUserToken(), -1);
                SharedPrefManager.getInstance(MainActivity.this).clear();
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

/*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        List<Order> orderListFiltered = new ArrayList<>();

        for (Order order : orderListFiltered){
            if (order.toString().toLowerCase().contains(userInput)){
                orderListFiltered.add(order);
            }
        }

        new OrdersAdapter()

//        OrdersAdapter.updateList(orderListFiltered);
        return false;
    }*/

    public void kitchenControlPopup (){

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.kitchencontrol_popup);

        final Spinner kitchenSpinner;
        kitchenSpinner = dialog.findViewById(R.id.chooseKitchen_spinner);

        final int[] userIndex = new int[1];
//        final int userIndex;

//        getKitchens(kitchenSpinner);

//        SpinnerClass.getKitchens(kitchenSpinner, MainActivity.this);

        final EditText passwordEditText = dialog.findViewById(R.id.changePassword_editText);
        final RadioGroup radioGroup = dialog.findViewById(R.id.switch_group);
        final RadioButton changePassword = dialog.findViewById(R.id.changePassword_button);
        final RadioButton changeStatus = dialog.findViewById(R.id.changeStatus_button);
        final LinearLayout changePasswordGroup = dialog.findViewById(R.id.changePassword_group);
        changePasswordGroup.setVisibility(View.GONE);
        final LinearLayout changeStatusGroup = dialog.findViewById(R.id.changeStatus_group);
        final RadioGroup changeStatusRadioGroup = dialog.findViewById(R.id.changeStatus_radioGroup);
        changeStatusGroup.setVisibility(View.GONE);
        final RadioButton ready = dialog.findViewById(R.id.ready);
        final RadioButton busy = dialog.findViewById(R.id.busy);
        final RadioButton outOfService = dialog.findViewById(R.id.outOfService);
        Button applyButton = dialog.findViewById(R.id.apply);
        Button cancelButton = dialog.findViewById(R.id.cancel);

        changePassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    changePasswordGroup.setVisibility(View.VISIBLE);
                    changeStatusGroup.setVisibility(View.GONE);
                }
            }
        });

        changeStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    changeStatusGroup.setVisibility(View.VISIBLE);
                    changePasswordGroup.setVisibility(View.GONE);
                }

            }
        });

        Call<Users> call = RetrofitClient.getInstance().getApi().getKitchens();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!response.isSuccessful()) {
                    //textViewResult.setText("code: " + response.code());

                    showMessage(String.valueOf(response.code()));
                    return;
                }else{

                    assert response.body() != null;
                    final ArrayList<User> users = response.body().getUsers();

                    final List<String> usernames = new ArrayList<>();

                    for(User user : users ){

                        usernames.add(user.toSting());
                    }



                    ArrayAdapter<String> spinnerAdapter;
                    spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, usernames);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kitchenSpinner.setAdapter(spinnerAdapter);
                    kitchenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            userIndex[0] = users.get(position).getId();

                            if (users.get(position).getType() != 1){

                                radioGroup.setVisibility(View.GONE);
                                changePasswordGroup.setVisibility(View.VISIBLE);
                                changeStatusGroup.setVisibility(View.GONE);
                                radioGroup.check(0);
                            }else{
                                radioGroup.setVisibility(View.VISIBLE);
                                changePasswordGroup.setVisibility(View.GONE);
                                changeStatusGroup.setVisibility(View.GONE);
                                radioGroup.check(0);
                            }

                            Log.i("user type: ", String.valueOf(users.get(position).getType()));
                            Log.i("user ID: ", String.valueOf(users.get(position).getId()));
                            getStatus(users.get(position).getId(), changeStatusRadioGroup);
//                            showMessage(String.valueOf(users.get(position).getType()));

//                            TODO: remove logs

                            //TODO: check listener and updating get status location
                            /*RadioGroup radioGroup1 = (RadioGroup) findViewById(R.id.switch_group);
                            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(RadioGroup group, int checkedId) {

                                    if (checkedId == R.id.changePassword_button){
                                        changePasswordGroup.setVisibility(View.VISIBLE);
                                        changeStatusGroup.setVisibility(View.GONE);
                                    }else if (checkedId == R.id.changeStatus_button){
                                        changeStatusGroup.setVisibility(View.VISIBLE);
                                        changePasswordGroup.setVisibility(View.GONE);
                                        getStatus(userIndex[0], changeStatusRadioGroup);
                                    }


                                }
                            });*/
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

//                            TODO: initialize the default value to be null until an item is selected

                        }
                    });
                }



            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                showMessage(t.getMessage());

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String password = passwordEditText.getText().toString();

                if (changePassword.isChecked()){

                    if (password.isEmpty()){
                        passwordEditText.setError("Enter Password First");
                        passwordEditText.requestFocus();
                    }else if (password.length() < 6){
                        passwordEditText.setError("Password is less that 6 characters");
                        passwordEditText.requestFocus();
                    }

                    updatePassword(userIndex[0], password);


                }else if (changeStatus.isChecked()){

                    if (ready.isChecked()){
                        updateStatus(userIndex[0], 1);
                    }else if (busy.isChecked()){
                        updateStatus(userIndex[0], 2);
                    }else if (outOfService.isChecked()){
                        updateStatus(userIndex[0], 3);
                    }
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();


    }

/*    private void getKitchens(final Spinner kitchenSpinner) {
        Call<Users> call = RetrofitClient.getInstance().getApi().getKitchens();

        call.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Call<Users> call, Response<Users> response) {
                if (!response.isSuccessful()) {
                    //textViewResult.setText("code: " + response.code());

                    showMessage(String.valueOf(response.code()));
                    return;
                }else{

                    assert response.body() != null;
                    final ArrayList<User> users = response.body().getUsers();

                    final List<String> usernames = new ArrayList<>();
                    for(User user : users){

                        usernames.add(user.toSting());
                    }



                    ArrayAdapter<String> spinnerAdapter;
                    spinnerAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_spinner_item, usernames);
                    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    kitchenSpinner.setAdapter(spinnerAdapter);
                    kitchenSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            RadioGroup radioGroup;
                            radioGroup = dialog.findViewById(R.id.switch1_group);
                            radioGroup.setEnabled(false);

                            if (users.get(position).getType() == 1){

                                radioGroup.setEnabled(true);

                            }else{

                                radioGroup.setEnabled(false);

                            }

                            showMessage(String.valueOf(users.get(position).getType()));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

//                            TODO: initialize the default value to be null until an item is selected

                        }
                    });
                }



            }

            @Override
            public void onFailure(Call<Users> call, Throwable t) {
                showMessage(t.getMessage());

            }
        });
    }*/


    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
    public void promocodesPopup (){

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.promocodes_popup);

        final EditText promocode = dialog.findViewById(R.id.promocode_editText);
        final EditText discount = dialog.findViewById(R.id.discount_editText);
        final TextView expireTxt = dialog.findViewById(R.id.expire_text);
        expire = dialog.findViewById(R.id.date);
        expire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, date,
                        myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                datePickerDialog.show();
            }
        });
        final TextView promocodeTypeTxt = dialog.findViewById(R.id.promocodeType_text);
        final RadioGroup radioGroup = dialog.findViewById(R.id.promocodeType_group);
        final RadioButton individual = dialog.findViewById(R.id.individual_button);
        final RadioButton corporate = dialog.findViewById(R.id.corporate_button);
        Button applyButton = dialog.findViewById(R.id.apply);
        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String promocodeS = promocode.getText().toString();
                if (promocodeS.isEmpty()) {
                    promocode.setError("Enter Promocode First");
                    promocode.requestFocus();
                    return;
                } else if (promocodeS.length() < 6 || promocodeS.length() > 8) {
                    promocode.setError("6 to 8 characters");
                    promocode.requestFocus();
                    return;
                }


                String discountS = discount.getText().toString();
                if (discountS.isEmpty()) {
                    discount.setError("Enter Discount");
                    discount.requestFocus();
                    return;
                } else if (Integer.parseInt(discountS) > 100) {
                    discount.setError("More Than 100% ?!");
                    discount.requestFocus();
                    return;
                }


                String expireS = expire.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = new Date();
                try {
                    date = sdf.parse(expireS);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (expireS.isEmpty()) {
                    expire.setError("Choose Expiry Date");
                    expire.requestFocus();
                    return;
                }


                if (!individual.isChecked() && !corporate.isChecked()){
                    promocodeTypeTxt.setError("Choose Promocode Type first");
                    return;
//                    Toast.makeText(MainActivity.this, "Choose Promocode Type first", Toast.LENGTH_SHORT).show();
                }else if (individual.isChecked()){
                    promocodeTypeTxt.setError(null);

                    // 1 = individual promo code
                    addPromoCode(promocodeS, discountS, expireS, 1 );
                } else if(corporate.isChecked()){
                    promocodeTypeTxt.setError(null);


                    // 2 = corporate promo code
                    addPromoCode(promocodeS, discountS, expireS, 2);
                }



//                Toast.makeText(MainActivity.this, "Promocode Type: " + promocodeTypeValue, Toast.LENGTH_SHORT).show();
                //addPromoCode(promocodeS, discountS, expireS, promocodeTypeValue);
            }
        });

        Button cancelButton = dialog.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void addPromoCode(String promocode, String discount, String expireString, int promocodeType) {
        Call<Result> call = RetrofitClient.getInstance().getApi().addPromoCode(promocode, discount, expireString, promocodeType);

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

    private void updatePassword(int userIndex, String password){

        Call<Result> resultCall = RetrofitClient.getInstance().getApi().updateKitchenPassword(userIndex, password);

        resultCall.enqueue(new Callback<Result>() {
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

    private void updateStatus(int userIndex, int value) {

        Call<Result> resultCall = RetrofitClient.getInstance().getApi().updateKitchenStatus(userIndex, value);

        resultCall.enqueue(new Callback<Result>() {
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

    private void getStatus(int userIndex, final RadioGroup changeStatusRadioGroup){

        Call<StatusResult> resultCall = RetrofitClient.getInstance().getApi().getKitchenStatus(userIndex);

        resultCall.enqueue(new Callback<StatusResult>() {
            @Override
            public void onResponse(Call<StatusResult> call, Response<StatusResult> response) {

//                if (!response.isSuccessful()) {
//                    //textViewResult.setText("code: " + response.code());
//
//                    Log.i("Respose code: ", String.valueOf(response.code()));
//                    showMessage(String.valueOf(response.code()));
//                    return;
//                }

                //use availability value to update radio group
                int availability = response.body().getAvailability();
                Log.i("Success msg: ", String.valueOf(response.code()));
                Log.i("availability: ", String.valueOf(availability));
                switch (availability){
                    case 1:
                        changeStatusRadioGroup.check(R.id.ready);
                        break;
                    case 2:
                        changeStatusRadioGroup.check(R.id.busy);
                        break;
                    case 3:
                        changeStatusRadioGroup.check(R.id.outOfService);
                        break;
                    default:
                        changeStatusRadioGroup.check(R.id.ready);
                        break;

                }
                //changeStatusRadioGroup.check(availability);
                //showMessage(response.body().getMessage());

            }

            @Override
            public void onFailure(Call<StatusResult> call, Throwable t) {

                Log.i("Failure msg: ", t.getMessage());
                showMessage(t.getMessage());
            }
        });
    }


    public void saveToken(String token, int userId) {

        Call<Result> call = RetrofitClient.getInstance().getApi().saveToken(token, userId);


        //calling the api
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {


                //displaying the message from the response as toast
                if (response.body() != null){

                    if (!response.body().getError()){
                        showMessage(response.body().getMessage());
                    }

                }

            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {

                showMessage(t.getMessage());
            }
        });
    }

}
