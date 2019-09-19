package org.foodmanoeuvre.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.foodmanoeuvre.API;
import org.foodmanoeuvre.ItemView;
import org.foodmanoeuvre.R;
import org.foodmanoeuvre.modal.FoodItem;
import org.foodmanoeuvre.modal.Restorent;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RestorentItemAdapter extends RecyclerView.Adapter<RestorentItemAdapter.ViewHolder>  {

    List<Restorent> restorentList;
    Context context;

    private GoogleMap mMap;

    public RestorentItemAdapter(List<Restorent> restorentList) {
        this.restorentList = restorentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.row_restorent,parent,false);
        context=view.getContext();
        return new RestorentItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Restorent r=restorentList.get(i);
        viewHolder.txtname.setText(r.getName());
        viewHolder.txturl.setText(r.getUrl());
        viewHolder.txtmno.setText(r.getMno());
        viewHolder.txtadd.setText(r.getAddress());
        Picasso.with(context).load(API.IMG_URL+r.getImage())
                .placeholder(R.drawable.ic_menu)
                .error(R.drawable.ic_menu)
                .into(viewHolder.itemimage);
    }

    @Override
    public int getItemCount() {
        return restorentList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtname,txtmno,txturl,txtadd;
        CircleImageView itemimage;
        LinearLayout linearLayout;

        public ViewHolder(@NonNull View v) {
            super(v);
            linearLayout=v.findViewById(R.id.linear);
            txtname=v.findViewById(R.id.itemresname);
            txtmno=v.findViewById(R.id.itemmno);
            txturl=v.findViewById(R.id.itemurl);
            txtadd=v.findViewById(R.id.itemadd);
            itemimage=v.findViewById(R.id.itemimg);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int i=getAdapterPosition();
                    Intent intent=new Intent(v.getContext(), ItemView.class);
                    intent.putExtra("RID",restorentList.get(i).getUid());
                    v.getContext().startActivity(intent);
                }
            });
            itemimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(getAdapterPosition());
                }
            });

        }
    }

    public void showDialog(final int pos)
    {
        final Restorent r=restorentList.get(pos);
        Dialog dialog = new Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogmap);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        MapView mMapView = (MapView) dialog.findViewById(R.id.mapView);
        MapsInitializer.initialize(context);
        mMapView = (MapView) dialog.findViewById(R.id.mapView);
        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();// needed to get the map to display immediately
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                LatLng sydney = new LatLng(Double.parseDouble(r.getLatt()), Double.parseDouble(r.getLang()));
                mMap.addMarker(new MarkerOptions().position(sydney).title(r.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,10F));
            }
        });
    }
}
