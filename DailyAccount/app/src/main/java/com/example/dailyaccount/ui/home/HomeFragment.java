package com.example.dailyaccount.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.R;
import com.example.dailyaccount.databasesetup.AccountData;
import com.example.dailyaccount.databasesetup.AccountModel;
import com.example.dailyaccount.databinding.FragmentHomeBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;


    public int editable_ID=-1;
    public EditText details,amount;
    public RadioGroup radioGroup;
    public Button submit,clear,update;
    public String details_data,amount_data,status_data,strDate;
    AccountModel amb;
    RecyclerView homeitems;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        String da=getArguments().getString("dates");
        if (da .equalsIgnoreCase("items")){
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            strDate = dateFormat.format(date);
            //Toast.makeText(container.getContext(),strDate,Toast.LENGTH_LONG).show();
        }else {
            strDate=da;
            //Toast.makeText(container.getContext(),strDate+"Def",Toast.LENGTH_LONG).show();
        }


        // Start My Code

        amb=new AccountModel(container.getContext());

        details=binding.details;
        amount=binding.amount;
        radioGroup=binding.statusGrp;
        submit=binding.submitBtn;
        clear=binding.clearBtn;
        update=binding.updateBtn;

        homeitems=binding.dailyAccRecy;
        homeitems.setHasFixedSize(true);
        homeitems.setLayoutManager(new LinearLayoutManager(getContext()));


        showAllData(inflater,strDate);



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(details.getText().toString())) {
                    Toast.makeText(container.getContext(),"Enter Details, Please!",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(container.getContext(),"Enter Amount, Please!",Toast.LENGTH_LONG).show();
                }else if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(container.getContext(),"Select Devit or Credit, Please!",Toast.LENGTH_LONG).show();
                }else{

                    RadioButton radioButton = (RadioButton) root.findViewById(radioGroup.getCheckedRadioButtonId());

                    details_data=details.getText().toString();
                    amount_data=amount.getText().toString();
                    status_data=radioButton.getText().toString();

                    if(amount_data.length()>8){
                        Toast.makeText(container.getContext(),"Sorry, Your Amount is Too Long. Please, Select less than 8",Toast.LENGTH_LONG).show();
                    } else{
                        addData(details_data,amount_data,status_data);
                    }
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearField();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(details.getText().toString())) {
                    Toast.makeText(container.getContext(),"Enter Details, Please!",Toast.LENGTH_LONG).show();
                }else if(TextUtils.isEmpty(amount.getText().toString())){
                    Toast.makeText(container.getContext(),"Enter Amount, Please!",Toast.LENGTH_LONG).show();
                }else if (radioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(container.getContext(),"Select Devit or Credit, Please!",Toast.LENGTH_LONG).show();
                }else if (editable_ID<0){
                    Toast.makeText(container.getContext(),"Sorry, You are Not Editable Mood, Please Select One...",Toast.LENGTH_LONG).show();
                }
                else{
                    RadioButton radioButton = (RadioButton) root.findViewById(radioGroup.getCheckedRadioButtonId());

                    details_data=details.getText().toString();
                    amount_data=amount.getText().toString();
                    status_data=radioButton.getText().toString();
                    if(amount_data.length()>8){
                        Toast.makeText(container.getContext(),"Sorry, Your Amount is Too Long. Please, Select less than 8",Toast.LENGTH_LONG).show();
                    } else{

                        updateData(editable_ID,details_data,amount_data,status_data);
                    }
                }
            }
        });

        //Stop My Code

        return root;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                System.exit(0);
                //Toast.makeText(container.getContext()," Defination ",Toast.LENGTH_LONG).show();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addData(String det,String amo,String stat){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        //Toast.makeText(getContext(),strDate,Toast.LENGTH_LONG).show();
        AccountData accountData=new AccountData(det,amo,stat,strDate);
        if(amb.addMyData(accountData)>0){
            Toast.makeText(getContext(),"Data Add Successful",Toast.LENGTH_LONG).show();
            showAllData(getLayoutInflater(),strDate);
            clearField();
        }

    }

    public void showAllData(LayoutInflater inflater,String date){
        final List<AccountData> alldata=amb.getAlldata(date);
        homeViewModel=new HomeViewModel(this,alldata,inflater);
        homeitems.setAdapter(homeViewModel);
    }

    public void clearField(){
        details.setText("");
        amount.setText("");
        this.radioGroup.clearCheck();
        submit.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
    }

    private void updateData(int editable_id, String details_data, String amount_data, String status_data) {
        AccountData accountData=new AccountData(editable_id,details_data,amount_data,status_data);
        if(amb.updateMyData(accountData)>0){
            Toast.makeText(getContext(),"Data Update Successful",Toast.LENGTH_LONG).show();
            showAllData(getLayoutInflater(),strDate);
            clearField();
        }
    }

}