package org.foodmanoeuvre.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.API;
import org.foodmanoeuvre.R;
import org.foodmanoeuvre.modal.FoodItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FoodItemNgoAdapter extends RecyclerView.Adapter<FoodItemNgoAdapter.ViewHolder>  {

    List<FoodItem> productList;
    Context context;
    List<Float> iqty=new ArrayList<Float>();
    public FoodItemNgoAdapter(List<FoodItem> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ngofood,parent,false);
        context=view.getContext();
        return new FoodItemNgoAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FoodItem f=productList.get(i);
        iqty.add(f.getQty());
        f.setQty(0);
        viewHolder.txtname.setText(f.getName());
        viewHolder.txttime.setText(f.getTime());
        Picasso.with(context).load(API.IMG_URL+f.getImage())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(viewHolder.itemimage);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
        TextView txtname,txttime;
        CircleImageView itemimage;
        EditText qty;
        public ViewHolder(@NonNull View v) {
            super(v);
            itemimage=v.findViewById(R.id.itemimg);
            txtname=v.findViewById(R.id.itemname);
            txttime=v.findViewById(R.id.itemtime);
            qty=(EditText) v.findViewById(R.id.itemqty);
            qty.setSelection(qty.getText().length());
            qty.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            int position = getAdapterPosition();
            FoodItem p=productList.get(position);
            if(charSequence.length()>0)
            {
                if(qty.getText().toString().equals(""))
                {
                    qty.setText("0");
                    qty.setSelection(qty.getText().length());
                }
                else
                {
                    int q=Integer.parseInt(qty.getText().toString());
                    if(q <= iqty.get(position))
                    {
                        p.setQty(Integer.parseInt(qty.getText().toString()));
                    }
                    else
                    {
                        Toast.makeText(context, "Only "+iqty.get(position)+" Quantity are left", Toast.LENGTH_SHORT).show();
                        qty.setText("0");
                        qty.setSelection(qty.getText().length());
                    }
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
