package com.mouhsinbourqaiba.android.storiesfirebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mouhsinbourqaiba.android.storiesfirebase.action.IFirebaseLoadDone;
import com.mouhsinbourqaiba.android.storiesfirebase.model.Movies;

import java.util.List;

import jp.shts.android.storiesprogressview.StoriesProgressView;

public class MainActivity extends AppCompatActivity implements IFirebaseLoadDone {
    private static String TAG = MainActivity.class.getSimpleName();

    private StoriesProgressView storiesProgressView;
    private ImageView imageViewStory;
    private Button btnLoad , btnPause , btnResume , btnReverse ;
    private int counter = 0;
    private DatabaseReference dbRef;
    private IFirebaseLoadDone iFirebaseLoadDone;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Movies");
        iFirebaseLoadDone = this;
    }

    @Override
    public void onFirebaseLoadSuccess(List<Movies> movieList) {

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

    }
}
