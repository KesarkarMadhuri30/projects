package d.newnavigationapp.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.other.Mysingleton;
import d.newnavigationapp.other.Session;

public class SignInActivity extends AppCompatActivity {
    private View mLoginFormView;
    public TextView signUp,forgotpassword;
    public Button signIn;
    public EditText email_id,et_password;
    private CheckBox checkBox;
    private View mProgressView;
    String email;
    String password;

    public boolean mAuthTask=false;
    String login_url="http://192.168.0.5/bigbazzar/LogInUser.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        signUp=(TextView)findViewById(R.id.signup);
        signIn=(Button)findViewById(R.id.signin);
        email_id=(EditText)findViewById(R.id.email_id);
        et_password=(EditText)findViewById(R.id.pass_word);
        forgotpassword=(TextView) findViewById(R.id.forgotPassword);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        checkBox=(CheckBox)findViewById(R.id.remember);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usersignIn();
            }
        });

        forgotpassword();
        Session session=new Session(SignInActivity.this);
        if(session.getPassword()!="")
        {
            String em=session.getEmail();
            String pass=session.getPassword();
            showProgress(true);
            UserLoginTask(em,pass);

        }
    }

    private void forgotpassword() {

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignInActivity.this,ForgotPasswordActivity.class);
                intent.putExtra("email",email);
                intent.putExtra("password",password);
                startActivity(intent);
            }
        });
    }

    private void usersignIn() {

         email = email_id.getText().toString();
         password =et_password.getText().toString();

        if (email.isEmpty()) {
            email_id.setError("This field is required");
            email_id.requestFocus();
        }
        else if ( !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_id.setError("Enter valid email");
            email_id.requestFocus();
        }

        else if (password.isEmpty()) {
            et_password.setError("This field is required");
            et_password.requestFocus();
        }

        else if (!(password.length() >=4)) {
            et_password.setError("Password is too short");
            et_password.requestFocus();
        }
        else
        {
            showProgress(true);
            UserLoginTask(email, password);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            //int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
            int shortAnimTime=2000;
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    public void UserLoginTask(final String mEmail, final String mPassword) {
      /*  Intent i= new Intent(StudentLoginActivity.this,StudentHomeActivity.class);
        startActivity(i);*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                Intent intent=new Intent(SignInActivity.this,MainActivity.class);
                try {
                    //getting json array from server

                    showProgress(false);

                    System.out.println("json key:"+response);
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    if(code.equals("Login_failed"))
                    {
                        mAuthTask=false;

                        et_password.setError(getString(R.string.error_incorrect_password));
                        et_password.requestFocus();
                    }
                    else
                    {
                        mAuthTask=true;
                        if(checkBox.isChecked())
                        {
                            Session session=new Session(SignInActivity.this);
                            session.setPassword(mPassword);
                            session.setEmail(mEmail);
                        }
                        //data perse to spinner acctivity
                        Bundle bundle=new Bundle();
                        bundle.putString("Name",jsonObject.getString("Name"));
                        bundle.putString("Email",mEmail);
                        intent.putExtras(bundle);
                        SignInActivity.this.finish();
                        SignInActivity.this.startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mAuthTask=false;
                showProgress(false);
                Toast.makeText(getApplicationContext(),"Please check your connection",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Email",mEmail);
                params.put("Password",mPassword);
                return params;
            }
        };
        Mysingleton.getInstance(SignInActivity.this).addToRequestque(stringRequest);
        // Simulate network access.

    }



}
