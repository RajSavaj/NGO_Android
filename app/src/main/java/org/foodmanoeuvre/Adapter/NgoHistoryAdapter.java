package org.foodmanoeuvre.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.API;
import org.foodmanoeuvre.R;
import org.foodmanoeuvre.modal.FoodHistory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NgoHistoryAdapter extends  RecyclerView.Adapter<NgoHistoryAdapter.ViewHolder> {

    List<FoodHistory> foodHistories;
    Context context;

    public NgoHistoryAdapter(List<FoodHistory> foodHistories) {
        this.foodHistories = foodHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_histroy,parent,false);
        context=view.getContext();
        return new NgoHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        FoodHistory h=foodHistories.get(i);
        viewHolder.txtitename.setText(h.getName());
        viewHolder.txtqty.setText(""+h.getQty());
        viewHolder.txtngoname.setText(h.getNgoname());
        Picasso.with(context).load(API.IMG_URL+h.getImage())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(viewHolder.imageView);
        if(h.getStatus()==0)
        {
            viewHolder.status.setText("Pending");
        }
        else if(h.getStatus()==1)
        {
            viewHolder.status.setText("Verified");
        }
        else
        {
            viewHolder.status.setText("Rejected");
        }
    }

    @Override
    public int getItemCount() {
        return  foodHistories.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView txtqty,txtitename,txtngoname,status,ptime;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=v.findViewById(R.id.itemimg);
            txtqty=v.findViewById(R.id.itemqty);
            txtitename=v.findViewById(R.id.itemname);
            txtngoname=v.findViewById(R.id.ngoname);
            linearLayout=v.findViewById(R.id.viewunverified);
            status=v.findViewById(R.id.status);
            linearLayout.setVisibility(View.GONE);
            status.setVisibility(View.VISIBLE);

        }
    }
}
