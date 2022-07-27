package com.example.dailyaccount.ui.yearly;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.R;
import com.example.dailyaccount.databasesetup.AccountModel;
import com.example.dailyaccount.databinding.FragmentYearlyBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearlyFragment extends Fragment {

    private YearlyViewModel yearlyViewModel;
    private FragmentYearlyBinding binding;
    AccountModel amb;
    public TextView detailswithyear,devitamount,creditamount;
    RecyclerView yearlyIteminYear;
    LayoutInflater inflater;
    ViewGroup container;
    NavController nv;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentYearlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        this.inflater=inflater;
        this.container=container;

        //Start My Code

        amb=new AccountModel(container.getContext());


        detailswithyear=binding.yearlyAllyear;
        devitamount=binding.yearlyDevit;
        creditamount=binding.yearlyCredit;
        yearlyIteminYear=binding.yearlyItemRecy;
        yearlyIteminYear.setHasFixedSize(true);
        yearlyIteminYear.setLayoutManager(new LinearLayoutManager(getContext()));


        //Stop My Code
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nv= Navigation.findNavController(view);


        List<Map> mylist=amb.getYearlyinAllData();

        Map a=new HashMap();
        a=mylist.get(mylist.size()-1);

        int de=Integer.parseInt(String.valueOf(a.get("totalDevit")));
        int ce=Integer.parseInt(String.valueOf(a.get("totalCredit")));

        detailswithyear.setText("Total Cash: "+(ce-de));
        devitamount.setText(""+de);
        creditamount.setText(""+ce);


        mylist.remove(mylist.get(mylist.size()-1));

        yearlyViewModel=new YearlyViewModel(mylist,inflater,nv);
        yearlyIteminYear.setAdapter(yearlyViewModel);

    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {

                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(date);

                Bundle bundle = new Bundle();
                bundle.putString("year", strDate);
                nv.navigate(R.id.action_nav_yearly_to_nav_home,bundle);
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
}