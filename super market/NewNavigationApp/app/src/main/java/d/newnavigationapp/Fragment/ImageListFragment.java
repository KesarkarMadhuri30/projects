package d.newnavigationapp.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.adapter.NetworkController;
import d.newnavigationapp.adapter.RecyclerViewAdapter;
import d.newnavigationapp.model.ProductModel;
import d.newnavigationapp.other.Mysingleton;



public class ImageListFragment extends Fragment {

    String product_url="http://192.168.0.5/bigbazzar/product.php";
    private ArrayList<ProductModel> productList = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter mAdapter;
    String url =null;
    ImageLoader imageLoader;
    String s_Title,name,email;
    RequestQueue queue;
    public ImageListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //  s_Title = this.getArguments().getString("name");
       // System.out.println("title:"+s_Title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_layout, container, false);
        s_Title = this.getArguments().getString("name");
        name=this.getArguments().getString("nm");
        email=this.getArguments().getString("email");
        recyclerView=(RecyclerView)v.findViewById(R.id.recyclerview);
        queue =  NetworkController.getInstance(getContext()).getRequestQueue();


        setupRecyclerView();

        mAdapter=new RecyclerViewAdapter(getContext(),recyclerView,productList);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);

        // recyclerView.setAdapter(new RecyclerViewAdapter(getContext(),recyclerView, productList));

        return v;
    }

    public void setupRecyclerView() {
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,product_url, new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    System.out.println("response:"+response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer productID=jsonObject.getInt("ProductId");
                        Integer productPrize=jsonObject.getInt("ProductPrize");

                        // String dat=jsonObject.getString("Date");
                        String productName=jsonObject.getString("ProductName");

                        String image=jsonObject.getString("ImagePath");
                        System.out.println("img:"+image);
                        String category=jsonObject.getString("Category");
                        Integer mrp=jsonObject.getInt("MRP");
                        ProductModel product = new ProductModel(productID,productName,productPrize,image,category,mrp,name,email);

                        productList.add(product);
                      //  mAdapter.notifyDataSetChanged();

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
            protected Map<String, String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<String,String>();
                params.put("Title",s_Title);
                return params;
            }
        };
    //    Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
        queue.add(stringRequest);



    }

}
