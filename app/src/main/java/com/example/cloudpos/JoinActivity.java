package com.example.cloudpos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudpos.connections.SignUpTask;

public class JoinActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "JoinActivity";

    private EditText emailInput, passwordInput, passwordVerifyInput, nameInput, mobileInput;
    private Button registerBtn;

    @Override
    protected void onResume() {
        // 액티비티 시작 시 EditText 입력 키보드가 위로 올라오지 않게 한다.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        emailInput = (EditText) findViewById(R.id.join_emailInput);
        passwordInput = (EditText) findViewById(R.id.join_passwordInput);
        passwordVerifyInput = (EditText) findViewById(R.id.join_passwordVerifyInput);
        nameInput = (EditText) findViewById(R.id.join_nameInput);
        mobileInput = (EditText) findViewById(R.id.join_mobileInput);

        registerBtn = (Button) findViewById(R.id.join_registerBtn);

        registerBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.join_registerBtn:
                Log.d(TAG, "join");

                if (!passwordInput.getText().toString().equals(passwordVerifyInput.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                    return;
                }

                SignUpTask signUpTask = new SignUpTask(emailInput.getText().toString(), passwordInput.getText().toString(), nameInput.getText().toString(), mobileInput.getText().toString(), getApplicationContext());
                signUpTask.execute();
                finish();
                break;
        }
    }
}
