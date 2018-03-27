package d.newnavigationapp.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
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
import d.newnavigationapp.adapter.NetworkController;
import d.newnavigationapp.adapter.SearchResultsAdapter;
import d.newnavigationapp.model.ProductModel;



public class SearchFragment extends Fragment{
    View myFragmentView;
    SearchView search;
    ImageButton buttonBarcode;
    ImageButton buttonAudio;
    Typeface type;
    ListView searchResults;
    private static String url=new String();
    String found = "N";
    String searchurl="http://192.168.0.5/bigbazzar/search.php";

    String name,email;

    ArrayList<ProductModel> productResults = new ArrayList<ProductModel>();
    SearchResultsAdapter mAdapter;
    RequestQueue queue;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final MainActivity activity = (MainActivity) getActivity();

        // type= Typeface.createFromAsset(activity.getAssets(),"fonts/book.TTF");
        myFragmentView = inflater.inflate(
                R.layout.search_fragment_layout, container, false);

        name=this.getArguments().getString("name");
        email=this.getArguments().getString("email");

        search=(SearchView) myFragmentView.findViewById(R.id.searchView1);
        search.setQueryHint("Start typing to search...");

        searchResults = (ListView) myFragmentView.findViewById(R.id.listview_search);

        buttonBarcode = (ImageButton) myFragmentView.findViewById(R.id.imageButton1);

        queue =  NetworkController.getInstance(getContext()).getRequestQueue();
        // buttonAudio = (ImageButton) myFragmentView.findViewById(R.id.imageButton1);

      /*  buttonBarcode.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                //Intent intent=new Intent(getActivity(),Barcode.class);
                //startActivity(intent);
                startActivityForResult(new Intent(activity, Barcode.class),1);
            }
        });*/


        search.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener()
        {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub

                //Toast.makeText(activity, String.valueOf(hasFocus),Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {

            @Override
            public boolean onQueryTextSubmit(String query) {
                // TODO Auto-generated method stub

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (newText.length() > 3)
                {

                    searchResults.setVisibility(myFragmentView.VISIBLE);
                    getData(newText);
                }
                else
                {

                    searchResults.setVisibility(myFragmentView.INVISIBLE);
                }



                return false;
            }

        });

        return myFragmentView;
    }

    private void getData(final String newText) {

        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST,searchurl, new com.android.volley.Response.Listener<String>()
        {
            @Override
            public void onResponse(String response)
            {
                try {

                    System.out.println("text"+response);
                    JSONArray jsonArray = new JSONArray(response);
                    int length=jsonArray.length();
                    for(int i=0;i<length;i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer productId=jsonObject.getInt("ProductId");
                        String productName=jsonObject.getString("ProductName");

                        // String dat=jsonObject.getString("Date");
                        Integer productPrize =jsonObject.getInt("ProductPrize");

                        String image=jsonObject.getString("ImagePath");
                        Integer mrp=jsonObject.getInt("MRP");
                        System.out.println("mrp:"+mrp);
                        String category=jsonObject.getString("Category");
                        ProductModel product = new ProductModel(productId,productName,productPrize,image,category,mrp,name,email);

                        found="N";
                        for(int j = 0; i<productResults.size(); i++)
                        {
                            if(productResults.get(j).getProductId().equals(product.getProductId()))
                            {
                                found="y";
                            }
                        }

                        if(found=="N")
                        {
                            productResults.add(product);
                            mAdapter=new SearchResultsAdapter(getContext(),productResults);
                            searchResults.setAdapter(mAdapter);
                        }



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
                params.put("text",newText);
                return params;
            }
        };
       // Mysingleton.getInstance(getContext()).addToRequestque(stringRequest);
        queue.add(stringRequest);

    }

}



