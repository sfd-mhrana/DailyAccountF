package com.example.dailyaccount.ui.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.R;
import com.example.dailyaccount.databasesetup.AccountData;
import com.example.dailyaccount.ui.home.HomeViewModel;

import java.util.List;
import java.util.Map;

public class DailyViewModel  extends RecyclerView.Adapter<DailyViewModel.SetItems> {

    List<Map> mylist; LayoutInflater inflater;
    NavController nv;
    public DailyViewModel(List<Map> mylist, LayoutInflater inflater, NavController nv) {
        this.mylist=mylist;
        this.inflater=inflater;
        this.nv=nv;
    }

    @NonNull
    @Override
    public SetItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_designs,parent,false);
        return new SetItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetItems holder, int position) {
        if(position>=0){
            Map map = mylist.get(position);
            holder.details.setText((CharSequence) map.get("details"));
            holder.credit.setText((CharSequence) map.get("credit"));
            holder.devit.setText((CharSequence) map.get("devit"));
            int a= Integer.parseInt(String.valueOf(map.get("credit"))) ;
            int b= Integer.parseInt(String.valueOf(map.get("devit"))) ;
            if(a>b){
                holder.d_c_image.setImageResource(R.drawable.good);
            }else {
                holder.d_c_image.setImageResource(R.drawable.bad);
            }

            holder.all_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String month=String.valueOf( map.get("details"));
                    Bundle bundle = new Bundle();
                    bundle.putString("dates", month);
                    nv.navigate(R.id.action_nav_daily_to_nav_home,bundle);
                }
            });

        }
        else{
            holder.details.setText("No Data pound");
        }
    }

    @Override
    public int getItemCount() {
        return mylist.size();
    }

    public class SetItems extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView d_c_image;
        TextView details,credit,devit;
        CardView all_cardview;

        public SetItems(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            d_c_image=itemView.findViewById(R.id.d_c_image);
            details=itemView.findViewById(R.id.item_details);
            credit=itemView.findViewById(R.id.item_credit);
            devit=itemView.findViewById(R.id.item_devit);
            all_cardview=itemView.findViewById(R.id.dailycardview);
        }

        @Override
        public void onClick(View view) {

        }
    }
}