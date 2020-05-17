package com.example.taiwanets;

import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListFragment extends Fragment {
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mFirestoreList;
    private FirestorePagingAdapter adapter;
    List<PeopleModel> itemList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_list,container, false);
        firebaseFirestore = FirebaseFirestore.getInstance();
        mFirestoreList = v.findViewById(R.id.firestore);
        //Query
        Query query = firebaseFirestore.collection("user");
        PagedList.Config config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(3)
                .build();
        //RecycleOptions
        FirestorePagingOptions<PeopleModel> options = new FirestorePagingOptions.Builder<PeopleModel>()
                .setQuery(query,config, PeopleModel.class)
                .build();
        adapter = new FirestorePagingAdapter<PeopleModel, PeopleViewHolder>(options){

            @NonNull
            @Override
            public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new PeopleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull PeopleViewHolder holder, int position, @NonNull PeopleModel model) {
                Log.d("USER NAME","" + model.getFullname());
                holder.list_name.setText(model.getFullname());
                Picasso.get().load(Uri.parse(model.getImage())).into(holder.list_image);
                if(model.getStatus().equals("STUDENT")){
                    holder.list_company.setVisibility(View.GONE);
                    holder.list_position.setVisibility(View.GONE);
                    holder.list_school.setText(model.getSchool());
                    holder.list_status.setText(model.getStatus());
                }else{
                    holder.list_school.setVisibility(View.GONE);
                    holder.list_status.setVisibility(View.GONE);
                    holder.list_company.setText(model.getCompanyName());
                    holder.list_position.setText(model.getJobTitle());
                }
            }

            @Override
            protected void onLoadingStateChanged(@NonNull LoadingState state) {
                super.onLoadingStateChanged(state);
                switch (state){
                    case LOADING_MORE:
                        Log.d("PAGING_LOG","LOADING NEXT PAGE");
                    case FINISHED:
                        Log.d("PAGEING_LOG","LOADING FINISHED");
                }
            }
        };
        mFirestoreList.setHasFixedSize(true);
        mFirestoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mFirestoreList.setAdapter(adapter);
        return v;
    }
    private class PeopleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView list_name;
        private TextView list_position;
        private  TextView list_company;
        private TextView list_school;
        private TextView list_status;
        private ImageView list_image;
//        LinearLayout parentlayout;
        public PeopleViewHolder(@NonNull View itemView){
            super(itemView);
            list_position = itemView.findViewById(R.id.list_position);
            list_company = itemView.findViewById(R.id.list_company);
            list_school = itemView.findViewById(R.id.list_school);
            list_status = itemView.findViewById(R.id.list_status);
            list_name = itemView.findViewById(R.id.list_name);
            list_image = itemView.findViewById(R.id.list_image);
//            parentlayout = itemView.findViewById(R.id.parent_layout);
//            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
}
