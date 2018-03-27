package d.newnavigationapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.activity.MainActivity;
import d.newnavigationapp.model.ProductModel;
import fr.ganfra.materialspinner.MaterialSpinner;


public class CartAdapter extends BaseAdapter {
    public ArrayList<ProductModel> listItem;
    private LayoutInflater inflter;
    public Context context;
    LinearLayout linearLayout;
    String remove_url = "http://192.168.0.5/bigbazzar/DeleteCartItem.php";
    String update_url = "http://192.168.0.5/bigbazzar/UpdateCart.php";
    AlertDialog.Builder builder;
    ImageLoader imageLoader;
     int mQuantity=0 ;
    int prize=0;

    public CartAdapter(Context context, ArrayList<ProductModel> cartList) {
        this.listItem =cartList;
        this.context=context;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return listItem.size();
    }

    @Override
    public Object getItem(int position) {
        return listItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflter.inflate(R.layout.cart_list_item, null);
        String path="http://192.168.0.5/bigbazzar/"+listItem.get(position).getProductimage();


        imageLoader = CustomVolleyRequest.getInstance(convertView.getContext().getApplicationContext())
                .getImageLoader();
        TextView productName = (TextView) convertView.findViewById(R.id.product_name);
        TextView mrp = (TextView) convertView.findViewById(R.id.product_Mrpvalue);
        TextView produvtPrize=(TextView)convertView.findViewById(R.id.product_value);
        NetworkImageView product_image=(NetworkImageView)convertView.findViewById(R.id.product_image) ;
        Button incr=(Button)convertView.findViewById(R.id.increment_button);
        Button decr=(Button)convertView.findViewById(R.id.decrement_button);
        final TextView qty=(TextView)convertView.findViewById(R.id.quantity_text_view);

        qty.setText(String.valueOf(listItem.get(position).getQuantity()));
        productName.setText(listItem.get(position).getProductName());
        mrp.setText(String.valueOf(listItem.get(position).getMrp()));
        produvtPrize.setText(String.valueOf(listItem.get(position).getProductPrize()));
        prize=listItem.get(position).getProductPrize();

        imageLoader.get(path, ImageLoader.getImageListener(product_image
                ,0,android.R.drawable
                        .ic_dialog_alert));

        product_image.setImageUrl(path, imageLoader);


        ImageButton remove=(ImageButton)convertView.findViewById(R.id.delete);
        builder = new AlertDialog.Builder(convertView.getContext());
        final View finalConvertView = convertView;
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RequestQueue requestQueue= Volley.newRequestQueue(v.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST,remove_url,new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("key="+response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            if (code.equals("Success"))
                            {
                                builder.setTitle(code);
                                displayAlert(jsonObject.getString("message"));
                            }
                            else
                            {
                                builder.setTitle("Failed");
                                displayAlert(jsonObject.getString("message"));
                            }

                            listItem.remove(position);
                            notifyDataSetChanged();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setTitle("Server not response");
                        Toast.makeText(v.getContext(),"server not response",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("ProductId",String.valueOf(listItem.get(position).getProductId()));
                        return params;
                    }
                };
                // Mysingleton.getInstance(StudentRegisterActivity.this).addToRequestque(stringRequest);
                requestQueue.add(stringRequest);
                MainActivity.mNotificationsCount--;

            }
        });

        final View finalConvertView1 = convertView;
        incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity = mQuantity + 1;
                qty.setText(String.valueOf(mQuantity));
                prize=prize*mQuantity;


                RequestQueue requestQueue= Volley.newRequestQueue(finalConvertView1.getContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST,update_url,new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("key="+response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            //  builder.setTitle("Success");
                            //   builder.setMessage(message);

                            //  displayAlert(code);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setTitle("Server not response");
                        Toast.makeText(finalConvertView1.getContext(),"server not response",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("mquntity", String.valueOf(mQuantity));
                        params.put("updatePrize",String.valueOf(prize));
                        params.put("Id",String.valueOf(listItem.get(position).getProductId()));

                        return params;
                    }
                };
                // Mysingleton.getInstance(StudentRegisterActivity.this).addToRequestque(stringRequest);
                requestQueue.add(stringRequest);

            }
        });
        decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuantity = mQuantity - 1;
                qty.setText(String.valueOf(mQuantity));
                prize=prize*mQuantity;

                RequestQueue requestQueue= Volley.newRequestQueue(finalConvertView1.getContext());
             //   final View finalConvertView1 = convertView;
                StringRequest stringRequest = new StringRequest(Request.Method.POST,update_url,new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println("key="+response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");
                            String message = jsonObject.getString("message");
                            //  builder.setTitle("Success");
                            //   builder.setMessage(message);

                            //  displayAlert(code);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        notifyDataSetChanged();


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setTitle("Server not response");
                        Toast.makeText(finalConvertView1.getContext(),"server not response",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();

                        params.put("mquntity", String.valueOf(mQuantity));
                        params.put("updatePrize",String.valueOf(prize));
                        params.put("Id",String.valueOf(listItem.get(position).getProductId()));

                        return params;
                    }
                };
                // Mysingleton.getInstance(StudentRegisterActivity.this).addToRequestque(stringRequest);
                requestQueue.add(stringRequest);

            }
        });

        return convertView;
    }




    private void displayAlert(String code) {
        builder.setTitle("Attenton");
        builder.setMessage(code);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }




}
