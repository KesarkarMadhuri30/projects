package d.newnavigationapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.model.ProductModel;
import d.newnavigationapp.other.Mysingleton;


public class SearchResultsAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    private ArrayList<ProductModel> productDetails=new ArrayList<>();
    int count;
    Context context;
    String cartUrl="http://192.168.0.5/bigbazzar/AddCart.php";
    AlertDialog.Builder builder;

    public SearchResultsAdapter(Context context, ArrayList<ProductModel> filteredProductResults) {

        layoutInflater = LayoutInflater.from(context);

        this.productDetails=filteredProductResults;
        this.count= filteredProductResults.size();
        this.context = context;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int position) {
        return   productDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        String path="http://192.168.0.5/bigbazzar/"+productDetails.get(position).getProductimage();


        convertView = layoutInflater.inflate(R.layout.search_list_item, null);
        holder = new ViewHolder();
        holder.imageLoader = CustomVolleyRequest.getInstance(convertView.getContext().getApplicationContext())
                .getImageLoader();

        holder.product_name = (TextView) convertView.findViewById(R.id.product_name);
        holder.product_mrp = (TextView) convertView.findViewById(R.id.product_mrp);
        holder.product_mrpvalue = (TextView) convertView.findViewById(R.id.product_Mrpvalue);
        holder.product_prize = (TextView) convertView.findViewById(R.id.product_value);
        holder.addToCart = (ImageView) convertView.findViewById(R.id.add_to_cart);
        holder.productImage=(NetworkImageView) convertView.findViewById(R.id.product_image);
        holder.product_name.setText(productDetails.get(position).getProductName());
        holder.product_mrpvalue.setText(String.valueOf(productDetails.get(position).getMrp()));
        holder.product_prize.setText(String.valueOf(productDetails.get(position).getProductPrize()));
        holder.imageLoader.get(path, ImageLoader.getImageListener(holder.productImage
                ,0,android.R.drawable
                        .ic_dialog_alert));


        holder.productImage.setImageUrl(path, holder.imageLoader);

        holder.addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                builder = new AlertDialog.Builder(v.getContext());
                StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,cartUrl, new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try {
                            System.out.println("key="+response);
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String code = jsonObject.getString("code");

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


                            notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }

                }, new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(v.getContext(),"server not response",Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams()throws AuthFailureError {
                        Map<String, String> params=new HashMap<>();

                        System.out.println("id="+String.valueOf(productDetails.get(position).getProductId()));
                        params.put("ProductId", String.valueOf(productDetails.get(position).getProductId()));
                        params.put("ProductName",productDetails.get(position).getProductName());
                        params.put("ProductPrize",String.valueOf(productDetails.get(position).getProductPrize()));
                        params.put("ImagePath",productDetails.get(position).getProductimage());
                        params.put("MRP",String.valueOf(productDetails.get(position).getMrp()));
                        params.put("Category",productDetails.get(position).getProductCategory());
                        params.put("UserEmail",productDetails.get(position).getEmail());
                        return params;
                    }
                };
                Mysingleton.getInstance(v.getContext()).addToRequestque(stringRequest);



            }
        });


        return convertView;
    }

    private void displayAlert(String message) {
            builder.setTitle("Attenton");
            builder.setMessage(message);
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

class ViewHolder
{
    TextView product_name;
    TextView product_mrp;
    TextView product_mrpvalue;
    TextView product_prize;
    ImageView addToCart;
    NetworkImageView productImage;
    ImageLoader imageLoader;

}

