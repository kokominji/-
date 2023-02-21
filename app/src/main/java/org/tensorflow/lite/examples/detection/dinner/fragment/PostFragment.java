package org.tensorflow.lite.examples.detection.dinner.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import org.tensorflow.lite.examples.detection.dinner.R;
import org.tensorflow.lite.examples.detection.dinner.model.ContentDTO;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PostFragment extends Fragment {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    FirebaseStorage firebaseStorage = null;

    Uri ImageUri = null;
    Button call_img_btn;
    Button upload_btn;
    EditText post_text;
    EditText material_text;
    ImageView imageView;
    View viewprofile;
    private int pickImageFromAlbum = 0;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);
        viewprofile = inflater.inflate(R.layout.fragment_post, container, false);

        firestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        auth = FirebaseAuth.getInstance();

        call_img_btn = view.findViewById(R.id.call_img_btn);
        imageView = view.findViewById(R.id.imgview);
        upload_btn = view.findViewById(R.id.upload_btn);
        post_text = view.findViewById(R.id.post_text);
        material_text = view.findViewById(R.id.material_text);


        // 이미지 불러오기 버튼
        call_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the album
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,pickImageFromAlbum);
            }
        });


        // 업로드 버튼
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                funImageUpload();
            }
        });
        return view;
    }


    // 앨범에서 이미지 선택할때 ImageUri에 uri저장
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == pickImageFromAlbum){
            // this is path to the selected image
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }


    // 업로드 관련 함수
    private void funImageUpload(){

        Date today = new Date();
        SimpleDateFormat timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss");
        String timeString = timestamp.format(today);
        String imgFileName = "IMAGE_"+timeString+"_.png";
        StorageReference storageRef = firebaseStorage.getReference().child("images").child(imgFileName);

        // 데이터베이스에 데이터 추가
        storageRef.putFile(ImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        ContentDTO contentDTO = new ContentDTO();

                        contentDTO.imageUri = uri.toString();
                        contentDTO.uid = auth.getCurrentUser().getUid();
                        contentDTO.userId = auth.getCurrentUser().getEmail();
                        contentDTO.explain = post_text.getText().toString();
                        contentDTO.material = material_text.getText().toString();
                        contentDTO.timestamp = System.currentTimeMillis();
                        firestore.collection("images").document().set(contentDTO);

                    }
                });
            }
        });
    }
}
