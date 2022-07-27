package com.example.dailyaccount.ui.monthly;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailyaccount.R;
import com.example.dailyaccount.databasesetup.AccountModel;
import com.example.dailyaccount.databinding.FragmentDailyBinding;
import com.example.dailyaccount.databinding.FragmentMonthlyBinding;
import com.example.dailyaccount.ui.daily.DailyViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MonthlyFragment extends Fragment {

    private MonthlyViewModel monthlyViewModel;
    private FragmentMonthlyBinding binding;
    AccountModel amb;
    public TextView detailswithmonth,devitamount,creditamount;
    String strDate;
    RecyclerView monthlyIteminYear;
    LayoutInflater inflater;
    NavController nv;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMonthlyBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        this.inflater=inflater;

        String da=getArguments().getString("year");
        if (da .equalsIgnoreCase("month")){
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy");
            strDate = dateFormat.format(date);
            //Toast.makeText(container.getContext(),strDate,Toast.LENGTH_LONG).show();
        }else {
            strDate=da;
            //Toast.makeText(container.getContext(),strDate+"Def",Toast.LENGTH_LONG).show();
        }




        amb=new AccountModel(container.getContext());


        detailswithmonth=binding.monthlyYearname;
        devitamount=binding.monthlyDevit;
        creditamount=binding.monthlyCredit;
        monthlyIteminYear=binding.monthlyItemRecy;
        monthlyIteminYear.setHasFixedSize(true);
        monthlyIteminYear.setLayoutManager(new LinearLayoutManager(getContext()));


        //Stop My Code
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nv= Navigation.findNavController(view);

        List<Map> mylist=amb.getMonthlyinYear(strDate);


        Map a=new HashMap();
        a=mylist.get(mylist.size()-1);

        String year=String.valueOf( a.get("selectedYear")).substring(0, 4);

        int de=Integer.parseInt(String.valueOf(a.get("totalDevit")));
        int ce=Integer.parseInt(String.valueOf(a.get("totalCredit")));

        detailswithmonth.setText(year+" Cash: "+(ce-de));
        devitamount.setText(""+de);
        creditamount.setText(""+ce);



        mylist.remove(mylist.get(mylist.size()-1));

        monthlyViewModel=new MonthlyViewModel(mylist,inflater,nv);
        monthlyIteminYear.setAdapter(monthlyViewModel);
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
                nv.navigate(R.id.action_nav_monthly_to_nav_home,bundle);
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