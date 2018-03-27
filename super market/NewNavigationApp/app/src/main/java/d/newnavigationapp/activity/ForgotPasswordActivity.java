package d.newnavigationapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import d.newnavigationapp.R;


public class ForgotPasswordActivity extends AppCompatActivity {
    EditText user_email,user_password;
    Button submit;
    String email_id,u_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        user_email =(EditText)findViewById(R.id.registered_emailid);
        user_password =(EditText)findViewById(R.id.newPass);
        submit = (Button)findViewById(R.id.forgot_button);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email_id =user_email.getText().toString();
                u_pass =user_password.getText().toString();

            }
        });
    }
}
