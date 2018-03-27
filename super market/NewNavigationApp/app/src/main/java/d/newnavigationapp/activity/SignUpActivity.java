package d.newnavigationapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;

import d.newnavigationapp.R;

public class SignUpActivity extends AppCompatActivity {

    public TextView signIn;
    public Button signUp;
    public EditText email,password,username,confirm_pass,mobile;
    String Email,Password,Username,Con_password,Mobile;
    public Context context;
    private CheckBox checkBox;
    String reg_url = "http://192.168.0.5/bigbazzar/SignUpUser.php";


    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        context = this;
        signIn=(TextView)findViewById(R.id.signin);
        email=(EditText)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        username=(EditText)findViewById(R.id.username);
        confirm_pass=(EditText)findViewById(R.id.con_password);
        mobile=(EditText)findViewById(R.id.mobile);
        signUp=(Button)findViewById(R.id.signup);
        checkBox = (CheckBox) findViewById(R.id.Condition);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        signin();
        builder = new AlertDialog.Builder(SignUpActivity.this);


    }

    private void signin() {
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
            }
        });
    }

    private void register() {



                Username=username.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                Con_password = confirm_pass.getText().toString();
                Mobile=mobile.getText().toString();

                if (Email.isEmpty()) {
                    email.setError("This field is required");
                    email.requestFocus();
                }
                else if ( !Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
                    email.setError("Enter valid email");
                    email.requestFocus();
                }

                else if (Username.isEmpty()) {
                    username.setError("This field is required");
                    username.requestFocus();
                }
                else if (!Username.matches("[A-Za-z]+"))
                {
                    username.setError("Enter valid name");
                    username.requestFocus();
                }
                else if (Password.isEmpty()) {
                    password.setError("This field is required");
                    password.requestFocus();
                }

                else if (!(Password.length() >=4)) {
                    password.setError("Password is too short");
                    password.requestFocus();
                }
                else if (Con_password.isEmpty())
                {
                    confirm_pass.setError("This field is required");
                    confirm_pass.requestFocus();
                }
                else if ( !Password.equals(Con_password))
                {
                    confirm_pass.setError("Password does not match");
                    confirm_pass.requestFocus();
                }

                else if (Mobile.isEmpty()) {
                    mobile.setError("This field is required");
                    mobile.requestFocus();
                }
                else if (!(Mobile.length()==10)) {
                    mobile.setError("Enter valid mobile number");
                    mobile.requestFocus();
                }

                else if (!Mobile.matches("[0-9]+"))
                {
                    mobile.setError("Enter valid mobile number ");
                    mobile.requestFocus();
                }

                else if(!checkBox.isChecked())
                {
                    builder.setTitle("Don't u agree with our condition");
                    builder.setMessage("Please click on chckbox");
                    displayAlert("Unchecked");
                }
                else
                {
           /* Intent i=new Intent(RegisterActivity.this,LoginActivity.class);
            RegisterActivity.this.finish();
            startActivity(i);*/

                    RequestQueue requestQueue= Volley.newRequestQueue(SignUpActivity.this);
                    StringRequest stringRequest = new StringRequest(Request.Method.POST,reg_url,new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                System.out.println("key="+response);
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String code = jsonObject.getString("code");
                                String message = jsonObject.getString("message");
                                builder.setTitle("Success");
                                builder.setMessage(message);

                                displayAlert(code);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            builder.setTitle("Server not response");
                            Toast.makeText(SignUpActivity.this,"server not response",Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();

                            System.out.println("dhsj"+Username);
                            params.put("FirstName", Username);
                            params.put("Email", Email);
                            params.put("Password", Password);
                            params.put("Mobile", Mobile);
                            return params;
                        }
                    };
                    // Mysingleton.getInstance(StudentRegisterActivity.this).addToRequestque(stringRequest);
                    requestQueue.add(stringRequest);
                }


    }


    private void displayAlert(final String code) {
        builder.setMessage(code);
        builder.setIcon(R.drawable.ic_manage);
        builder.setCancelable(true);

        if (code.equals("Success"))
        {
            builder.setPositiveButton("Stay Here?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    confirm_pass.setText("");
                    mobile.setText("");

                }
            });

            builder.setNegativeButton("Want to SignIn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent i=new Intent(SignUpActivity.this,SignInActivity.class);
                    SignUpActivity.this.finish();
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                }
            });
            AlertDialog alertDialo = builder.create();
            alertDialo.show();

        }
        else if (code.equals("Reg_failed")) {
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    confirm_pass.setText("");
                    mobile.setText("");

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
        if(code.equals("Unchecked"))
        {
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

    }


    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
