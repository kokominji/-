package org.tensorflow.lite.examples.detection.dinner.fragment;

import static androidx.recyclerview.widget.RecyclerView.ViewHolder;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.tensorflow.lite.examples.detection.dinner.R;
import org.tensorflow.lite.examples.detection.dinner.model.ContentDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{

    FirebaseFirestore firestore;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.homefragment_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
        recyclerView.setAdapter(new HomeFragmentRecyclerViewAdapter());

        return view;
    }
    public class HomeFragmentRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder>{

        String uid;
        List<ContentDTO> contentDTOs;
        List<String> contentUidList;

        public HomeFragmentRecyclerViewAdapter(){
            contentDTOs = new ArrayList<>();
            contentUidList = new ArrayList<>();
            firestore = FirebaseFirestore.getInstance();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // 데이터베이스에서 불러오기
            firestore.collection("images").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(value == null) return;

                    contentDTOs.clear();
                    contentUidList.clear();
                    for(DocumentSnapshot snapshot : value.getDocuments()){
                        ContentDTO item = snapshot.toObject(ContentDTO.class);
                        contentDTOs.add(item);
                        contentUidList.add(snapshot.getId());
                    }
                    notifyDataSetChanged();
                }
            });
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe,parent,false);

            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecipeViewHolder recipeViewHolder = ((RecipeViewHolder) holder);

            // 리사이클러뷰 껍데기에 데이터 집어넣기
            recipeViewHolder.detailViewItem_profile_text.setText(contentDTOs.get(position).userId);
            Glide.with(recipeViewHolder.itemView.getContext()).load(contentDTOs.get(position).imageUri).into(recipeViewHolder.detailViewItem_imageView_content);
            recipeViewHolder.detailViewItem_explain_textView.setText(contentDTOs.get(position).explain);
            recipeViewHolder.detailViewItem_profile_image.setImageURI(Uri.parse(contentDTOs.get(position).imageUri));
            recipeViewHolder.detailViewItem_recipe_material_textView.setText(contentDTOs.get(position).material);
            recipeViewHolder.detailViewItem_date_textView.setText(new SimpleDateFormat("yyyy-MM-dd").format(contentDTOs.get(position).timestamp));
        }

        @Override
        public int getItemCount() {
            return contentDTOs.size();
        }

        // 리사이클러뷰 껍데기 선언하는 느낌
        private class RecipeViewHolder extends RecyclerView.ViewHolder{
            ImageView detailViewItem_profile_image;
            TextView detailViewItem_profile_text;
            ImageView detailViewItem_imageView_content;
            TextView detailViewItem_explain_textView;
            TextView detailViewItem_recipe_material_textView;
            TextView detailViewItem_date_textView;
            public RecipeViewHolder(@NonNull View view) {
                super(view);
                detailViewItem_profile_image = (ImageView) view.findViewById(R.id.detailViewItem_profile_image);
                detailViewItem_profile_text = (TextView) view.findViewById(R.id.detailViewItem_profile_textView);
                detailViewItem_imageView_content = (ImageView) view.findViewById(R.id.detailViewItem_imageView_content);
                detailViewItem_explain_textView = (TextView) view.findViewById(R.id.detailViewItem_explain_textView);
                detailViewItem_recipe_material_textView = (TextView) view.findViewById(R.id.detailViewItem_recipe_material_textView);
                detailViewItem_date_textView = (TextView) view.findViewById(R.id.detailViewItem_date_textView);
            }
        }
    }

}