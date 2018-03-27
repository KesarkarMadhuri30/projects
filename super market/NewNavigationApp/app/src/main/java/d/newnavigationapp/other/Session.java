package d.newnavigationapp.other;

import android.content.Context;
import android.content.SharedPreferences;



public class Session {

    SharedPreferences sharedPreferences;
    Context ctx;
    private String password;
    private String email;


    public String getEmail() {
        email=sharedPreferences.getString("email","");
        return email;
    }

    public void setEmail(String email) {
        sharedPreferences.edit().putString("email",email).commit();
        this.email = email;
    }

    public String getPassword() {
        password=sharedPreferences.getString("password","");
        return password;
    }

    public void setPassword(String password) {
        sharedPreferences.edit().putString("password",password).commit();
        this.password = password;
    }


    public Session(Context ctx) {
        this.ctx = ctx;
        sharedPreferences=ctx.getSharedPreferences("mydata",Context.MODE_PRIVATE);
    }
    public void removeUser()
    {
        sharedPreferences.edit().clear().commit();

    }
}
