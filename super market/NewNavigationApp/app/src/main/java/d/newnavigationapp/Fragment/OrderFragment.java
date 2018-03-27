package d.newnavigationapp.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import d.newnavigationapp.activity.MainActivity;
import d.newnavigationapp.adapter.OrderAdapter;
import d.newnavigationapp.model.ProductModel;
import d.newnavigationapp.other.Mysingleton;



public class OrderFragment extends Fragment {

    ListView listView;
    String name,email;
    Button buy;
    RequestQueue queue;
    Integer totalCost=0;
    TextView total,totalprize;
    LinearLayout empty_orderList;
    RelativeLayout order_layout;
    AlertDialog.Builder builder;
    OrderAdapter mAdapter;
    private ArrayList<ProductModel> orderList = new ArrayList<>();

    String orderUrl="http://192.168.0.5/bigbazzar/FetchOrder.php";

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // type= Typeface.createFromAsset(activity.getAssets(),"fonts/book.TTF");
        View view = inflater.inflate(R.layout.order_fragment_layout, container, false);

        empty_orderList = (LinearLayout)view.findViewById(R.id.layout_orderlist_empty);
        order_layout = (RelativeLayout) view.findViewById(R.id.orderlayout);
        listView=(ListView)view.findViewById(R.id.listview_order);
        buy=(Button)view.findViewById(R.id.buy);
        total=(TextView)view.findViewById(R.id.total);
        totalprize=(TextView)view.findViewById(R.id.totalprize);

        name=this.getArguments().getString("name");
        email=this.getArguments().getString("email");

        mAdapter = new OrderAdapter(getContext(),orderList);
        listView.setAdapter(mAdapter);

        getData();

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Toast.makeText(getContext(),"buy now",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,orderUrl, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    /*System.out.println("key="+response);
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String code = jsonObject1.getString("code");
                    System.out.println("code:"+code);

                    int length=jsonArray.length();
                    System.out.println("len="+length);*/

                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("res:"+response);
                    int length=jsonArray.length();
                    System.out.println("len="+length);
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String code = jsonObject1.getString("code");
                    System.out.println("code:"+code);

                    if(code.equals("Failed"))
                    {
                        int size=orderList.size();
                        System.out.println("size="+size);
                        setCartLayout(size);
                    }
                    else {
                        for (int i = 0; i <length; i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Integer productId = jsonObject.getInt("ProductId");
                            String productName = jsonObject.getString("ProductName");
                            Integer productPrize = jsonObject.getInt("ProductPrize");
                            String image = jsonObject.getString("ImagePath");
                            Integer mrp = jsonObject.getInt("MRP");
                            String category = jsonObject.getString("Category");
                            Integer quantity = jsonObject.getInt("Quantity");
                            Integer updatePrize = jsonObject.getInt("UpdatePrize");
                            String email1 = jsonObject.getString("UserEmail");

                            totalCost = totalCost + updatePrize;
                            totalprize.setText(String.valueOf(totalCost));
                            //ProductModel product = new ProductModel(productId, productName, productPrize, image, category, mrp, email1, quantity, updatePrize,totalCost);
                            ProductModel product = new ProductModel(productId,productName,productPrize,image,mrp,category,quantity,updatePrize,email1,totalCost);

                            orderList.add(product);

                        }
                        int size = orderList.size();
                        System.out.println("size=" + size);
                        setCartLayout(size);
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
                params.put("name",name);
                params.put("email", email);
                return params;
            }
        };
        Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
       // queue.add(stringRequest);

    }


    private void setCartLayout(int size) {

        if(String.valueOf(size).equals("0")){
            empty_orderList.setVisibility(View.VISIBLE);
            order_layout.setVisibility(View.GONE);

        }
        else {
            empty_orderList.setVisibility(View.GONE);
            order_layout.setVisibility(View.VISIBLE);

        }
    }


}
