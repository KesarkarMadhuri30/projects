package d.newnavigationapp.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.adapter.CartAdapter;
import d.newnavigationapp.adapter.NetworkController;
import d.newnavigationapp.model.ProductModel;
import d.newnavigationapp.other.Mysingleton;



public class CartFragment extends Fragment {

    private ListView listView;
    String name,email;
    LinearLayout empty_cart,cart,cart_layout;
    Button bStartShopping,checkout;
     int countCart;
     TextView itemCount;
     int item_count;
     TextView text_prize;
    RequestQueue queue;
    int totalprize=0,productprize=0;
    AlertDialog.Builder builder;

    Integer productId,productPrize,quantity,mrp,updatePrize;
    String productName,image,category,email1;


    String cart_url="http://192.168.0.5/bigbazzar/FetchCart.php";
    String order_url="http://192.168.0.5/bigbazzar/Order.php";

    private ArrayList<ProductModel> cartList = new ArrayList<>();
    CartAdapter mAdapter;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.cart_fragment_layout, container, false);
        listView=(ListView)v.findViewById(R.id.cartlistView);
        empty_cart = (LinearLayout)v.findViewById(R.id.layout_cart_empty);
        cart_layout = (LinearLayout)v.findViewById(R.id.cart_layout);

         itemCount=(TextView)v.findViewById(R.id.item_count);
         text_prize=(TextView)v.findViewById(R.id.total_amount);
         checkout=(Button)v.findViewById(R.id.checkout);
        queue =  NetworkController.getInstance(getContext()).getRequestQueue();

        builder = new AlertDialog.Builder(getContext());


        name=this.getArguments().getString("name");
        email=this.getArguments().getString("email");
        countCart=this.getArguments().getInt("countcart");


        getData();
        mAdapter=new CartAdapter(getContext(),cartList);
        listView.setAdapter(mAdapter);


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder.setTitle("Attenton");
                builder.setMessage("Do you want to checkout");
                builder.setCancelable(true);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"click on yes",Toast.LENGTH_SHORT).show();

                        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,order_url, new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response)
                            {
                                try {
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
                                Toast.makeText(getContext(),"server not response",Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        })
                        {
                            @Override
                            protected Map<String, String> getParams()throws AuthFailureError {
                                Map<String, String> params=new HashMap<>();

                               for(int i=0;i<=cartList.size();i++) {
                                   System.out.println("product:"+cartList.get(i).getProductId());
                                 /*  params.put("ProductId", String.valueOf(cartList.get(i).getProductId()));
                                   params.put("ProductName", cartList.get(i).getProductName());
                                   params.put("ProductPrize", String.valueOf(cartList.get(i).getProductPrize()));
                                   params.put("ImagePath", cartList.get(i).getProductimage());
                                   params.put("MRP", String.valueOf(cartList.get(i).getMrp()));
                                   params.put("Category", cartList.get(i).getProductCategory());
                                   //params.put("Quantity",String.valueOf(cartList.get(i).getQuantity()));
                                   params.put("updatePrize",String.valueOf(cartList.get(i).getUpdateprize()));
                                   params.put("UserEmail", cartList.get(i).getEmail());*/

                                   params.put("ProductId", String.valueOf(cartList.get(i).getProductId()));
                                   params.put("ProductName",cartList.get(i).getProductName());
                                   params.put("ProductPrize",String.valueOf(cartList.get(i).getProductPrize()));
                                   params.put("ImagePath",cartList.get(i).getProductimage());
                                   params.put("MRP",String.valueOf(cartList.get(i).getMrp()));
                                   params.put("Category",cartList.get(i).getProductCategory());
                                   params.put("UserEmail",cartList.get(i).getEmail());
                               }
                                   return params;

                            }
                        };
                        Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getContext(),"click on No",Toast.LENGTH_SHORT).show();
                    }
                });


                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });

        return v;
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

    private void setCartLayout(int size) {

        if(String.valueOf(size).equals("0")){
            empty_cart.setVisibility(View.VISIBLE);
            cart_layout.setVisibility(View.GONE);
            bStartShopping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else {
            empty_cart.setVisibility(View.GONE);
            cart_layout.setVisibility(View.VISIBLE);



        }
    }

    private void getData() {

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,cart_url, new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("res:"+response);
                    int length=jsonArray.length();
                    System.out.println("len="+length);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String code = jsonObject1.getString("code");
                    System.out.println("code:"+code);
                    if(code.equals("Failed"))
                    {
                        int size=cartList.size();
                        System.out.println("size="+size);
                        setCartLayout(size);
                    }
                    else {
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Integer productId = jsonObject.getInt("ProductId");
                            String productName = jsonObject.getString("ProductName");

                            // String dat=jsonObject.getString("Date");
                            Integer productPrize = jsonObject.getInt("ProductPrize");
                            String image = jsonObject.getString("ImagePath");
                            Integer mrp = jsonObject.getInt("MRP");
                            String category = jsonObject.getString("Category");
                            Integer quantity = jsonObject.getInt("Quantity");
                            Integer updatePrize = jsonObject.getInt("UpdatePrize");
                            String email1 = jsonObject.getString("UserEmail");


                            productprize=quantity*productPrize;
                            ProductModel product = new ProductModel(productId, productName, productPrize, image, category, mrp, email1, quantity, updatePrize,productprize);


                            cartList.add(product);

                            totalprize = totalprize + productPrize * quantity;
                            System.out.println("totalprize=" + totalprize);


                        }

                        int size = cartList.size();
                        System.out.println("size=" + size);
                        setCartLayout(size);
                        itemCount.setText(String.valueOf(size));
                        text_prize.setText(String.valueOf(totalprize));

                    }
                    mAdapter.notifyDataSetChanged();


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
                Toast.makeText(getContext(),"server not response",Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("name",name);
                params.put("email", email);
                return params;
            }
        };
       Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
        //queue.add(stringRequest);

    }
}
