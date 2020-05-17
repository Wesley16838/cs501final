package com.example.taiwanets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import static androidx.constraintlayout.widget.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Work#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Work extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Work() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Work.
     */
    // TODO: Rename and change types and number of parameters
    public static Work newInstance(String param1, String param2) {
        Work fragment = new Work();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        View v = inflater.inflate(R.layout.fragment_work, container, false);

        TextView comp = (TextView) v.findViewById(R.id.company);
        TextView title = (TextView) v.findViewById(R.id.jobtitle);
        TextView des = (TextView) v.findViewById(R.id.description);
        TextView locat = (TextView) v.findViewById(R.id.location);
        TextView date = (TextView) v.findViewById(R.id.date);

        String company = preferences.getString("companyname","");
        String jobtitle = preferences.getString("jobtitle","");
        String description = preferences.getString("description","");
        String location = preferences.getString("location","");
        String status = preferences.getString("status","");
        Log.d(TAG, "testtstatus" + status);
        if(status.equals("Student")){
             String year = preferences.getString("jobyear","");
             String month = preferences.getString("jobmonth","");
             String endmonth = preferences.getString("jobendmonth","");

             String endyear = preferences.getString("jobendyear","");
            date.setText(month + "/"+ year + " ~ "+ endmonth + "/"+ endyear);
        }else{
             String year = preferences.getString("jobyear","");
             String month = preferences.getString("jobmonth","");
             date.setText(month + "/"+year);
        }
        title.setText(jobtitle);
        comp.setText(company);
        des.setText(description);
        locat.setText(location);

        return v;
    }
}
