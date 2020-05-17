package com.example.taiwanets;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {


    private static final String TAG = "Home_Fragment";
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v =  inflater.inflate(R.layout.fragment_home,container, false);
        final SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        final ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        final TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tablayout);
        final TextView fullname = (TextView) v.findViewById(R.id.fullname);
        final TextView position = (TextView) v.findViewById(R.id.position);
        final ImageView profileImage = (ImageView) v.findViewById(R.id.profileImage);


        final Summary summaryFragment = new Summary();
        final Work workFragment = new Work();
        final Educational educationFragment = new Educational();



//        if(preferences.getBoolean("cache",false) == true){
//            String image_cache = preferences.getString("image","");
//            String status_cache = preferences.getString("status","");
//            String school_cache = preferences.getString("school","");
//            String fullname_cache = preferences.getString("fullname","");
//            String companyname = preferences.getString("companyname","");
//            Picasso.get().load(Uri.parse(image_cache)).into(profileImage);
//            fullname.setText(fullname_cache);
//            if(status_cache.equals("Employee")){
//                position.setText(status_cache+" at "+companyname);
//            }else if(status_cache.equals("Student")){
//                position.setText(status_cache+" at "+school_cache);
//            }
//
//
//        }else{
            fullname.setText(fAuth.getCurrentUser().getDisplayName());
            String email = (String) fAuth.getCurrentUser().getEmail();
            preferences.edit().putString("fullname", fAuth.getCurrentUser().getDisplayName()).apply();
            preferences.edit().putString("email", fAuth.getCurrentUser().getEmail()).apply();
            fStore.collection("user").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();

                        if (document.exists()) {

                            String schoolName = document.getString("School");
                            String status = document.getString("Status");
                            String status_new = status.charAt(0) + status.substring(1).toLowerCase();//////////////改
                            String image = document.getString("Image");
                            Picasso.get().load(Uri.parse(image)).into(profileImage);
                            position.setText(status_new+" at "+schoolName);
                            preferences.edit().putString("headline", document.getString("Headline")).apply();
                            preferences.edit().putString("summary", document.getString("Summary")).apply();
                            Log.d(TAG, "testtstatus" + status);/////////
                            //Work Experience
                            if(status.equals("STUDENT")){
                                preferences.edit().putString("jobendyear", document.getString("JobEndYear")).apply();
                                preferences.edit().putString("jobendmonth", document.getString("JobEndMonth")).apply();
                            }
                            preferences.edit().putString("jobyear", document.getString("JobStartYear")).apply();
                            preferences.edit().putString("jobmonth", document.getString("JobStartMonth")).apply();
                            preferences.edit().putString("companyname", document.getString("CompanyName")).apply();
                            preferences.edit().putString("location", document.getString("Location")).apply();
                            preferences.edit().putString("description", document.getString("Description")).apply();
                            preferences.edit().putString("jobtitle", document.getString("JobTitle")).apply();

                            //Education
                            String major = document.getString("Major");
                            String degree = document.getString("Degree");
                            String schoolyear = document.getString("SchoolYear");
                            String schoolmonth = document.getString("SchoolMonth");

                            preferences.edit().putString("school", schoolName).apply();
                            preferences.edit().putString("major", major).apply();
                            preferences.edit().putString("degree", degree).apply();
                            preferences.edit().putString("schoolyear", schoolyear).apply();
                            preferences.edit().putString("schoolmonth", schoolmonth).apply();

                            preferences.edit().putString("status", status_new).apply();
                            preferences.edit().putString("image", image).apply();

                            if(status == "EMPLOYEE"){
                                List<String> group = (List<String>) document.get("ContentForReferral");
                                Gson gson = new Gson();
                                String json = gson.toJson(group);
                                preferences.edit().putString("referral",json).apply();
                            }
                            preferences.edit().putBoolean("cache", true).apply();
                            tabLayout.setupWithViewPager(viewPager);
                            viewPagerAdapter viewPagerAdapter = new viewPagerAdapter(getChildFragmentManager(),0);

                            viewPagerAdapter.addFragment(summaryFragment,"Summary");
                            viewPagerAdapter.addFragment(workFragment,"Work");
                            viewPagerAdapter.addFragment(educationFragment,"Education");
                            viewPager.setAdapter(viewPagerAdapter);

                            tabLayout.getTabAt(0).setIcon(R.drawable.ic_assignment_blue_24dp);
                            tabLayout.getTabAt(1).setIcon(R.drawable.ic_work_blue_24dp);
                            tabLayout.getTabAt(2).setIcon(R.drawable.ic_school_blue_24dp);

                            tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#ffffff"));
                            tabLayout.setTabTextColors(Color.parseColor("#dbdbdb"), Color.parseColor("#ffffff"));

                            tabLayout.getTabAt(0).getIcon().setColorFilter(new PorterDuffColorFilter( Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP));
                            tabLayout.getTabAt(1).getIcon().setColorFilter(new PorterDuffColorFilter( Color.parseColor("#dbdbdb"), PorterDuff.Mode.SRC_ATOP));
                            tabLayout.getTabAt(2).getIcon().setColorFilter(new PorterDuffColorFilter( Color.parseColor("#dbdbdb"), PorterDuff.Mode.SRC_ATOP));


                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    tab.getIcon().setColorFilter(new PorterDuffColorFilter( Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_ATOP));
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                    tab.getIcon().setColorFilter(new PorterDuffColorFilter( Color.parseColor("#dbdbdb"), PorterDuff.Mode.SRC_ATOP));
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {

                                }
                            });
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
//        }
        return v;
    }

    private class viewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public viewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }
}
