package org.foodmanoeuvre.Adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInstaller;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.API;
import org.foodmanoeuvre.FoodArea;
import org.foodmanoeuvre.R;
import org.foodmanoeuvre.modal.FoodItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    List<FoodItem> productList;
    Context context;
    public FoodItemAdapter(List<FoodItem> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_resitem,parent,false);
        context=view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        FoodItem f=productList.get(position);
        viewHolder.txtname.setText(f.getName());
        viewHolder.txtqty.setText(""+f.getQty());
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

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtname,txtqty,txttime;
        CircleImageView itemimage;
        ImageView btndelete,btnedit;

        public ViewHolder(@NonNull View v) {
            super(v);
            txtname=v.findViewById(R.id.itemname);
            txtqty=v.findViewById(R.id.itemqty);
            txttime=v.findViewById(R.id.itemtime);

            itemimage=v.findViewById(R.id.itemimg);
            btndelete=v.findViewById(R.id.itemdelete);
            btnedit=v.findViewById(R.id.itemdedit);

            btnedit.setOnClickListener(this);
            btndelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            final int position=getAdapterPosition();
            if(v.getId()==R.id.itemdedit)
            {
                LayoutInflater li = LayoutInflater.from(context);
                View promptsView = li.inflate(R.layout.prompt, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        context,R.style.AlertDialogTheme);
                alertDialogBuilder.setView(promptsView);
                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);
                userInput.setText(""+productList.get(position).getQty());
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        chagneQty(productList.get(position).getId(),userInput.getText().toString());
                                        txtqty.setText(userInput.getText().toString());
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
            if(v.getId()==R.id.itemdelete)
            {
                final NiftyDialogBuilder dialogBuilder=NiftyDialogBuilder.getInstance(context);
                dialogBuilder
                        .withTitle("Delete Item")                                  //.withTitle(null)  no title
                        .withTitleColor("#FFFFFF")                                  //def
                        .withDividerColor("#333366")                                //def
                        .withMessage("Are You Sure Want To Delete Item?")                     //.withMessage(null)  no Msg
                        .withMessageColor("#FFFFFFFF")                              //def  | withMessageColor(int resid)
                        .withDialogColor("#ff8000")
                        .isCancelableOnTouchOutside(true)                           //def    | isCancelable(true)
                        .withDuration(700)                                          //def
                        .withEffect(Effectstype.SlideBottom)                                         //def Effectstype.Slidetop
                        .withButton1Text("OK")                                      //def gone
                        .withButton2Text("Cancel")      //.setCustomView(View or ResId,context)
                        .setButton1Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteItem(productList.get(position).getId());
                                productList.remove(position);
                                notifyDataSetChanged();
                                dialogBuilder.dismiss();
                            }
                        })
                        .setButton2Click(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(v.getContext(), "Item Will Be Safe", Toast.LENGTH_SHORT).show();
                                dialogBuilder.dismiss();
                            }
                        })
                        .show();
            }
        }
    }

    public  void deleteItem(int id)
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

        Call<String> call=api.deleteItem(String.valueOf(id));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    public void chagneQty(int id,String qty)
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

        Call<String> call=api.updateqty(String.valueOf(id),qty);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }
}
