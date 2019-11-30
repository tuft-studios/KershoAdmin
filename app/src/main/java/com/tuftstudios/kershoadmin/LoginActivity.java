package com.tuftstudios.kershoadmin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText editTextUsername, editTextPassword;
    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextUsername = findViewById(R.id.user);
        editTextPassword = findViewById(R.id.password);

        editTextPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);
                    userLogin();
                    handled = true;
                }
                return handled;
            }
        });

        //textViewResult = findViewById(R.id.textViewResult);

        findViewById(R.id.btn_login).setOnClickListener(this);
    }

    //here we override the on start to check first if user is logged in then we direct user
//to orders activity directly
//TODO uncomment the onstart
    @Override
    protected void onStart() {
        super.onStart();

        if (SharedPrefManager.getInstance(this).isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void userLogin() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();


        if (username.isEmpty()) {
            editTextUsername.setError("Username is required");
            editTextUsername.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }

        Call<Login> call = RetrofitClient.getInstance().getApi().userLogin(username, password);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (!response.isSuccessful()) {
                    //textViewResult.setText("code: " + response.code());

                    showMessage(String.valueOf(response.code()));
                    return;
                }

                Login loginResponse = response.body();
                String content = "";

                if (loginResponse.isError() == false) {

                    content += "Error: " + loginResponse.isError() + "\n";
                    content += "Message: " + loginResponse.getMessage() + "\n";
                    content += "User ID: " + loginResponse.getUser().getId() + "\n";
                    content += "User Type: " + loginResponse.getUser().getType() + "\n";
                    content += "User Username: " + loginResponse.getUser().getUsername() + "\n\n";

                    Log.i("content", content);

                    //textViewResult.append(content);

                    showMessage("login successfully");

                    /*now if error is false, login successful, we will call shared pref, pass the context (main act)
                    and save the user with saveUser method defined in sharedpref class, and get the user to be shared
                    from the login response body*/
                    SharedPrefManager.getInstance(LoginActivity.this).saveUser(loginResponse.getUser());

                    //also we will start the orders activity but we need to set flags to start it as new task and clear previous tasks
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {

                    content += "Error: " + loginResponse.isError() + "\n";
                    content += "Message: " + loginResponse.getMessage() + "\n\n";

                    Log.i("content", content);

                    //textViewResult.append(content);

                    showMessage(loginResponse.getMessage());
                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                showMessage(t.getMessage());

            }
        });

    }


    @Override
    public void onClick(View view) {

        InputMethodManager mgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(editTextUsername.getWindowToken(), 0);
        mgr.hideSoftInputFromWindow(editTextPassword.getWindowToken(), 0);


        userLogin();


    }


    public void showMessage(String message) {

        Toast.makeText(this, message,Toast.LENGTH_LONG).show();

    }

}
