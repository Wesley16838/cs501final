package com.example.taiwanets;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Educational#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Educational extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Educational() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Educational.
     */
    // TODO: Rename and change types and number of parameters
    public static Educational newInstance(String param1, String param2) {
        Educational fragment = new Educational();
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
        View v = inflater.inflate(R.layout.fragment_educational, container, false);
        TextView school = (TextView) v.findViewById(R.id.school);
        TextView degreeandmajor = (TextView) v.findViewById(R.id.degreeandmajor);
        TextView date = (TextView) v.findViewById(R.id.date);

        String sc = preferences.getString("school","");
        String dg = preferences.getString("degree","");
        String mj = preferences.getString("major","");
        String year = preferences.getString("schoolyear","");
        String month = preferences.getString("schoolmonth","");
        String status = preferences.getString("status","");
        school.setText(sc);
        degreeandmajor.setText(dg + " in " + mj);
        if(status.equals("Student")){
            date.setText("Expect "+month +"/"+year);
        }else{
            date.setText(month +"/"+year);
        }


        return v;
    }
}
