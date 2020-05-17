package com.example.taiwanets;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class intro extends AppCompatActivity {
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;
    private Button buttonOnboardingAction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);
        buttonOnboardingAction = findViewById(R.id.buttonOnboardingAction);
        setupOnboardingItems();

        final ViewPager2 onboardingViewPager = findViewById(R.id.onboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);
        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);

        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(onboardingViewPager.getCurrentItem()+1<onboardingAdapter.getItemCount()){
                    onboardingViewPager.setCurrentItem(onboardingViewPager.getCurrentItem() + 1);
                }else{
                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
                    finish();
                }
            }
        });

    }

    private void setupOnboardingItems(){
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem itemPurpose = new OnboardingItem();
        itemPurpose.setTitle("For Taiwanese Student in USA");
        itemPurpose.setDescription(" The platform that helps Taiwanese students who pursue degree in USA to get the job more efficient by employee referrals.");
        itemPurpose.setImage(R.drawable.social_networking);

        OnboardingItem itemDetail = new OnboardingItem();
        itemDetail.setTitle("Employee Referrals Opportunity from Senior");
        itemDetail.setDescription("Hiring via referral is cheaper, faster, and more effective. Go ahead to build a network with senior and write a employee referral cover letter to senior");
        itemDetail.setImage(R.drawable.social_networking);

        OnboardingItem itemResult = new OnboardingItem();
        itemResult.setTitle("Higher Opportunity to Get the Inverview");
        itemResult.setDescription("Employee referrals have the highest applicant to hire conversion rate – only 7% apply but this accounts for 40% of all hires.");
        itemResult.setImage(R.drawable.social_networking);

        onboardingItems.add(itemPurpose);
        onboardingItems.add(itemDetail);
        onboardingItems.add(itemResult);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators(){
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i = 0; i<indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for(int i= 0; i<childCount; i++){
            ImageView imageView = (ImageView)layoutOnboardingIndicators.getChildAt(i);
            if(i ==index){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
        if(index == onboardingAdapter.getItemCount() -1){
            buttonOnboardingAction.setText("START");
        }else{
            buttonOnboardingAction.setText("Next");
        }
    }
}
