package com.example.cloudpos;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cloudpos.connections.SignInTask;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInputEditText;
    private EditText passwordInputEditText;
    private Button registerBtn;
    private Button loginBtn;

    @Override
    protected void onResume() {
        // 액티비티 시작 시 EditText 입력 키보드가 위로 올라오지 않게 한다.
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        emailInputEditText = (EditText) findViewById(R.id.emailInput);
        passwordInputEditText = (EditText) findViewById(R.id.passwordInput);
        emailInputEditText.setText("이리온");
        passwordInputEditText.setText("1111");
//        emailInputEditText.setText("ckddn9496@gmail.com");
//        passwordInputEditText.setText("password");
        registerBtn = (Button) findViewById(R.id.registerBtn);
        loginBtn = (Button) findViewById(R.id.loginBtn);
        registerBtn.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.registerBtn:
                Toast.makeText(getApplicationContext(), "등록 버튼", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(StartActivity.this, JoinActivity.class);
                startActivity(intent);
                break;
            case R.id.loginBtn:
                Toast.makeText(getApplicationContext(), "로그인 버튼", Toast.LENGTH_LONG).show();
                SignInTask signInTask = new SignInTask(emailInputEditText.getText().toString(), passwordInputEditText.getText().toString(), getApplicationContext());
                signInTask.execute();
                break;
        }
    }
}
