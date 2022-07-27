package com.example.dailyaccount.ui.yearly;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.MainwindowsActivity;
import com.example.dailyaccount.R;
import com.example.dailyaccount.ui.monthly.MonthlyFragment;
import com.example.dailyaccount.ui.monthly.MonthlyViewModel;

import java.util.List;
import java.util.Map;


public class YearlyViewModel extends RecyclerView.Adapter<YearlyViewModel.SetItems>{
    List<Map> mylist; LayoutInflater inflater;
    NavController nv;

    public YearlyViewModel(List<Map> mylist, LayoutInflater inflater, NavController nv) {
        this.mylist = mylist;
        this.inflater = inflater;
        this.nv=nv;
    }

    @NonNull
    @Override
    public SetItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.item_designs,parent,false);
        return new YearlyViewModel.SetItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetItems holder, int position) {
        if(position>=0){

            Map map = mylist.get(position);
            String year=String.valueOf( map.get("details")).substring(0, 4);
            holder.details.setText(year);
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
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("year", year);
                    nv.navigate(R.id.action_nav_yearly_to_nav_monthly,bundle);
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