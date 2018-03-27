package d.newnavigationapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.activity.MainActivity;
import d.newnavigationapp.model.ProductModel;
import d.newnavigationapp.other.Mysingleton;


 public class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {


    public RecyclerView recyclerview;
    private ArrayList<ProductModel> productList;
    public Context context;
    String cartUrl="http://192.168.0.5/bigbazzar/AddCart.php";
    //String wishListUrl="http://192.168.0.101/bigbazzar/AddToWishList.php";
    AlertDialog.Builder builder;
 //   ImageLoader imageLoader;

    public RecyclerViewAdapter(Context context, RecyclerView recyclerView, ArrayList<ProductModel> productList) {
        this.recyclerview=recyclerView;
        this.productList=productList;
        this.context=context;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


       String path="http://192.168.0.5/bigbazzar/"+productList.get(position).getProductimage();

       System.out.println("path:"+path);

       holder.imageLoader.get(path, ImageLoader.getImageListener(holder.imageView
                ,0,android.R.drawable
                        .ic_dialog_alert));
        holder.imageView.setImageUrl(path, holder.imageLoader);

        holder.price.setText(String.valueOf(productList.get(position).getProductPrize()));
        holder.name.setText(productList.get(position).getProductName());

        holder.cartImage.setOnClickListener(new View.OnClickListener() {
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

                          //  notifyItemInserted(position);
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

                        System.out.println("id="+String.valueOf(productList.get(position).getProductId()));
                        params.put("ProductId", String.valueOf(productList.get(position).getProductId()));
                        params.put("ProductName",productList.get(position).getProductName());
                        params.put("ProductPrize",String.valueOf(productList.get(position).getProductPrize()));
                        params.put("ImagePath",productList.get(position).getProductimage());
                        params.put("MRP",String.valueOf(productList.get(position).getMrp()));
                        params.put("Category",productList.get(position).getProductCategory());
                        params.put("UserEmail",productList.get(position).getEmail());
                        return params;
                    }
                };
                Mysingleton.getInstance(v.getContext()).addToRequestque(stringRequest);


            }
        });


        // holder.build(productList,position);

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

     private void displayAlertMessage(String message) {
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


     @Override
    public int getItemCount() {
        return productList.size();
    }
 }

class MyViewHolder extends RecyclerView.ViewHolder {

    public final NetworkImageView imageView;
    public final LinearLayout layoutItem;
  //  public final ImageView wishlistImage;
    public final TextView price;
    public final TextView name;
    public final ImageView cartImage;
    ImageLoader imageLoader;


    public MyViewHolder(View itemView) {
        super(itemView);

        imageView = (NetworkImageView) itemView.findViewById(R.id.imageView);
        layoutItem = (LinearLayout) itemView.findViewById(R.id.layout_item);
       // wishlistImage = (ImageView) itemView.findViewById(R.id.wishlist_image);
        price = (TextView) itemView.findViewById(R.id.product_price);
        name = (TextView) itemView.findViewById(R.id.product_name);
        cartImage = (ImageView) itemView.findViewById(R.id.cart_image);
        imageLoader = CustomVolleyRequest.getInstance(itemView.getContext().getApplicationContext())
                .getImageLoader();


    }






   /* public void build(final Integer image_id, final Integer price,final String name) {
        layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailsActivity.class);
                intent.putExtra("ImageId", image_id);
                intent.putExtra("Price",price);
                intent.putExtra("Name",name);
                v.getContext().startActivity(intent);

            }
        });*/

      /*  wishlistImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProductInfo productInfoUtils = new ProductInfo();
                productInfoUtils.addWishlistImage(image_id,price,name);
               // wishlistImage.setImageResource(R.drawable.ic_favorite);
                Toast.makeText(view.getContext(),"Item added to wishlist.",Toast.LENGTH_SHORT).show();

            }
        });*/



}

