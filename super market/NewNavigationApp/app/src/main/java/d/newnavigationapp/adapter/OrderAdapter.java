package d.newnavigationapp.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
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
import java.util.Map;

import d.newnavigationapp.R;
import d.newnavigationapp.activity.MainActivity;
import d.newnavigationapp.model.ProductModel;

public class OrderAdapter extends BaseAdapter{
    public ArrayList<ProductModel> orderListItem;
    private LayoutInflater inflter;
    public Context context;
    AlertDialog.Builder builder;
    ImageLoader imageLoader;
    int mQuantity=0 ;
    int prize=0;

    public OrderAdapter(Context context, ArrayList<ProductModel> orderList) {
        this.orderListItem =orderList;
        this.context=context;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return orderListItem.size();
    }

    @Override
    public Object getItem(int i) {
        return orderListItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view =inflter.inflate(R.layout.order_list_item,null);

        String path="http://192.168.0.5/bigbazzar/"+orderListItem.get(i).getProductimage();

        imageLoader = CustomVolleyRequest.getInstance(view.getContext().getApplicationContext())
                .getImageLoader();
        TextView productName = (TextView) view.findViewById(R.id.product_name);
        TextView mrp = (TextView) view.findViewById(R.id.product_Mrpvalue);
        TextView productPrize=(TextView)view.findViewById(R.id.product_value);
        NetworkImageView product_image=(NetworkImageView)view.findViewById(R.id.product_image) ;
       // TextView upadeprize =(TextView)view.findViewById(R.id.u)
        final TextView qty=(TextView)view.findViewById(R.id.quantity_value);

        qty.setText(String.valueOf(orderListItem.get(i).getQuantity()));
        productName.setText(orderListItem.get(i).getProductName());
        mrp.setText(String.valueOf(orderListItem.get(i).getMrp()));
        productPrize.setText(String.valueOf(orderListItem.get(i).getProductPrize()));
        prize=orderListItem.get(i).getProductPrize();

        imageLoader.get(path, ImageLoader.getImageListener(product_image
                ,0,android.R.drawable
                        .ic_dialog_alert));

        product_image.setImageUrl(path, imageLoader);

        return view;
    }

}
