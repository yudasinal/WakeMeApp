package com.yljv.alarmapp.client.ui.start;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.parse.ParseException;
import com.parse.PushService;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.R.id;
import com.yljv.alarmapp.R.layout;
import com.yljv.alarmapp.client.helper.ApplicationSettings;
import com.yljv.alarmapp.client.helper.MenuMainActivity;
import com.yljv.alarmapp.server.user.AccountManager;
import com.yljv.alarmapp.server.user.ParseRegisterListener;

public class RegisterActivity extends Activity implements OnClickListener,
        ParseRegisterListener {

    public final static String FIRST_NAME = "com.yljv.alarmapp.FIRST_NAME";

    EditText editFirstName;
    Button btnRegister;
    EditText editEmail;
    EditText confirmEmail;
    EditText editPassword;
    ProgressBar progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        editFirstName = (EditText) findViewById(R.id.editFirstName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        confirmEmail = (EditText) findViewById(R.id.confirmEmail);
        editPassword = (EditText) findViewById(R.id.editPassword);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        btnRegister.setOnClickListener(this);
        getActionBar().setTitle("Register");
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View register) {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        // TODO create User here
        switch (register.getId()) {
            case R.id.btnRegister:
                progress.setVisibility(View.VISIBLE);
                btnRegister.setTextColor(Color.parseColor("#fa8b60"));
                registerAttempt();
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void registerAttempt() {
        String firstName = editFirstName.getText().toString();
        String email = editEmail.getText().toString();
        String emailConfirm = confirmEmail.getText().toString();
        String password = editPassword.getText().toString();
        boolean cancel = false;
        View focusView = null;
        progress.setVisibility(View.GONE);
        btnRegister.setTextColor(Color.parseColor("#3f2860"));

        // TODO Error fields (user does not exist, etc)
        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            editPassword.setError("This field is required");
            focusView = editPassword;
            cancel = true;
        } else if (password.length() < 6) {
            editPassword.setError("Short password");
            focusView = editPassword;
            cancel = true;
        }
        if(TextUtils.isEmpty(firstName)){
            editFirstName.setError("Field is required");
        }
        if (TextUtils.isEmpty(email)) {
            editEmail.setError("Field is required");
            focusView = editEmail;
            cancel = true;
            //TODO check if the email is valid(in the form of ""@"" and has a dot)
        } else if (!email.contains("@") || !email.contains(".")) {
            editEmail.setError("Invalid email address");
            focusView = editEmail;
            cancel = true;
        }
        if (!email.equals(emailConfirm)){
            progress.setVisibility(View.GONE);
            btnRegister.setText("Emails do not match!");
            btnRegister.setTextColor(Color.parseColor("#3f2860"));
            cancel = true;
        }
        if (cancel) {
            // There was an error; don'tuple attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            progress.setVisibility(View.VISIBLE);
            btnRegister.setTextColor(Color.parseColor("#fa8b60"));

            AccountManager.register(this, email, password);
        }
    }


    @Override
    public void onRegisterSuccess() {

        PushService.subscribe(this, AccountManager.getSubscribedChannel(),
                MenuMainActivity.class);

        //PushService.subscribe(this, AccountManager.getEmail(), MenuMainActivity.class);
        //TODO invalid username

        String name = ApplicationSettings.getUserName();
        Intent intent = new Intent(this, RegisterPartnerActivity.class);
        intent.putExtra(FIRST_NAME, name);

        startActivity(intent);
    }

    @Override
    public void onRegisterFail(ParseException e) {
        boolean isThereInternet = isNetworkAvailable();
        if(isThereInternet == false) {
            progress.setVisibility(View.GONE);
            btnRegister.setText("No or slow internet connection");
            btnRegister.setTextColor(Color.parseColor("#3f2860"));
        }
        // TODO error messages (passwords do not match, etc

    }

    private boolean isNetworkAvailable(){

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void back() {
        Intent intent = new Intent(this, ChoiceActivity.class);
        startActivity(intent);

    }

}
