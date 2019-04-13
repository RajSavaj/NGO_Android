package org.foodmanoeuvre.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.API;
import org.foodmanoeuvre.R;
import org.foodmanoeuvre.modal.FoodHistory;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FoodStatusAdapter extends RecyclerView.Adapter<FoodStatusAdapter.ViewHolder> {

    List<FoodHistory> foodHistories;
    Context context;

    public FoodStatusAdapter(List<FoodHistory> foodHistories) {
        this.foodHistories = foodHistories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_histroy,parent,false);
        context=view.getContext();
        return new FoodStatusAdapter.ViewHolder(view);
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
        if(h.getStatus()!=0)
        {
            viewHolder.linearLayout.setVisibility(View.GONE);
            viewHolder.status.setVisibility(View.VISIBLE);
            if(h.getStatus()==1)
            {
                viewHolder.status.setText("Verified");
            }
            else {
                viewHolder.status.setText("Rejected");
            }
        }
    }

    @Override
    public int getItemCount() {
        return foodHistories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CircleImageView  imageView;
        TextView txtqty,txtitename,txtngoname,status;
        ImageView acces,rej;
        LinearLayout linearLayout;
        public ViewHolder(@NonNull View v) {
            super(v);
            imageView=v.findViewById(R.id.itemimg);
            txtqty=v.findViewById(R.id.itemqty);
            txtitename=v.findViewById(R.id.itemname);
            txtngoname=v.findViewById(R.id.ngoname);
            acces=v.findViewById(R.id.confirm);
            rej=v.findViewById(R.id.itemrej);
            linearLayout=v.findViewById(R.id.viewunverified);
            status=v.findViewById(R.id.status);
            rej.setOnClickListener(this);
            acces.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position=getAdapterPosition();
            if(v.getId()==R.id.confirm)
            {
                changeStatus(String.valueOf(foodHistories.get(position).getId()),"1");
            }
            else if(v.getId()==R.id.itemrej)
            {
                changeStatus(String.valueOf(foodHistories.get(position).getId()),"2");
            }
        }

        public  void changeStatus(String rid, final String st)
        {
            final ProgressDialog progressDialog = new ProgressDialog(context,
                    R.style.AppTheme_Dark_Dialog);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Processing ...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Retrofit retrofit=new Retrofit.Builder()
                    .baseUrl(API.BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            API api=retrofit.create(API.class);

            Call<String> call=api.acceptItem(rid,st);
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    progressDialog.dismiss();
                    if(response.isSuccessful())
                    {
                        linearLayout.setVisibility(View.GONE);
                        status.setVisibility(View.VISIBLE);
                        if(st.equals("1"))
                        {
                            status.setText("Verified");
                        }
                        else
                        {
                            status.setText("Rejected");
                        }
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });
        }
    }


}
