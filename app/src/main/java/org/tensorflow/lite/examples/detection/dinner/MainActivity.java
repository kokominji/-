package org.tensorflow.lite.examples.detection.dinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import org.tensorflow.lite.examples.detection.dinner.camera.FoodActivity;
import org.tensorflow.lite.examples.detection.dinner.fragment.HomeFragment;
import org.tensorflow.lite.examples.detection.dinner.fragment.PeopleFragment;
import org.tensorflow.lite.examples.detection.dinner.fragment.PostFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bnv_main = findViewById(R.id.mainactivity_bottomnavigationview);

        getSupportFragmentManager().beginTransaction().add(R.id.mainactivity_framelayout, new PeopleFragment()).commit();

        // 네비게이션 화면 전환
        bnv_main.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_Recipe:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new HomeFragment()).commit();
                        break;

                    case R.id.action_Chatting:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new PeopleFragment()).commit();
                        break;

                    case R.id.action_Upload:
                        getSupportFragmentManager().beginTransaction().replace(R.id.mainactivity_framelayout, new PostFragment()).commit();
                        break;
                    case R.id.action_food:
                        Intent intent = new Intent(MainActivity.this, FoodActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
}

/*
프래그먼트들의 부모 액티비티라 보면됨
프래그먼트의 이동만 관할
 */