package com.example.dailyaccount.ui.home;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.R;
import com.example.dailyaccount.databasesetup.AccountData;

import java.util.List;

public class HomeViewModel extends RecyclerView.Adapter<HomeViewModel.SetItems> {
    List<AccountData> ad;
    LayoutInflater context;
    HomeFragment homeFragment;
    public HomeViewModel(HomeFragment homeFragment, List<AccountData> ad, LayoutInflater context) {
        this.ad = ad;
        this.context = context;
        this.homeFragment=homeFragment;
    }

    @NonNull
    @Override
    public SetItems onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=context.inflate(R.layout.home_items,parent,false);
        return new SetItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetItems holder, int position) {
        if(position>=0){
            AccountData userModel = ad.get(position);
            if (userModel.getStatus().toString().equalsIgnoreCase("Devit")){
                holder.d_c_image.setImageResource(R.drawable.cashout);
            }else if(userModel.getStatus().toString().equalsIgnoreCase("Credit")){
                holder.d_c_image.setImageResource(R.drawable.cashin);
            }
            holder.details.setText(userModel.getDetails());
            holder.date.setText(userModel.getDate());
            holder.amount.setText(userModel.getAmount());

            holder.all_cardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    homeFragment.details.setText(userModel.getDetails());
                    homeFragment.amount.setText(userModel.getAmount());
                    if (userModel.getStatus().toString().equalsIgnoreCase("Devit")){
                        ((RadioButton)homeFragment.radioGroup.getChildAt(0)).setChecked(true);
                    }else if(userModel.getStatus().toString().equalsIgnoreCase("Credit")){
                        ((RadioButton)homeFragment.radioGroup.getChildAt(1)).setChecked(true);
                    }
                    homeFragment.submit.setVisibility(View.GONE);
                    homeFragment.update.setVisibility(View.VISIBLE);
                    homeFragment.editable_ID=userModel.getId();
                }
            });

        }
        else{
            holder.details.setText("No Data pound");
        }
    }

    @Override
    public int getItemCount() {
        return ad.size();
    }

    public class SetItems extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView d_c_image;
        TextView details,date,amount;
        CardView all_cardview;

        public SetItems(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            d_c_image=itemView.findViewById(R.id.hd_c_image);
            details=itemView.findViewById(R.id.hitem_details);
            date=itemView.findViewById(R.id.hidate);
            amount=itemView.findViewById(R.id.hiamount);
            all_cardview=itemView.findViewById(R.id.home_cardview);
        }

        @Override
        public void onClick(View view) {

        }
    }
}