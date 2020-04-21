package com.mouhsinbourqaiba.android.storiesfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mouhsinbourqaiba.android.storiesfirebase.action.IFirebaseLoadDone;
import com.mouhsinbourqaiba.android.storiesfirebase.model.Movies;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
        FirebaseApp.initializeApp(this);

        //init firebase
        dbRef = FirebaseDatabase.getInstance().getReference("Movies");

        //init interface
        iFirebaseLoadDone = this;

        //views
        btnLoad = (Button) findViewById(R.id.btn_load);
        btnPause = (Button) findViewById(R.id.btn_pause);
        btnResume = (Button) findViewById(R.id.btn_resume);
        btnReverse = (Button) findViewById(R.id.btn_reverse);

        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories);
        storiesProgressView.setStoryDuration(1000L); // is 1 second

        imageViewStory = (ImageView) findViewById(R.id.image);

        //Event
        imageViewStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.pause();
            }
        });

        btnResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.resume();
            }
        });

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        List<Movies> moviesList = new ArrayList<>();
                        for (DataSnapshot itemSnapShot : dataSnapshot.getChildren()){
                            Movies movie = itemSnapShot.getValue(Movies.class);
                            moviesList.add(movie);
                        }
                        iFirebaseLoadDone.onFirebaseLoadSuccess(moviesList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        iFirebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());
                    }
                });
            }
        });



    }

    @Override
    public void onFirebaseLoadSuccess(final List<Movies> movieList) {
        storiesProgressView.setStoriesCount(movieList.size());
        //Load first image
        Picasso.get().load(movieList.get(0).getImage()).into(imageViewStory, new Callback() {
            @Override
            public void onSuccess() {
                storiesProgressView.startStories();
            }

            @Override
            public void onError(Exception e) {

            }
        });

        //Set event stories
        storiesProgressView.setStoriesListener(new StoriesProgressView.StoriesListener() {
            @Override
            public void onNext() {
                //Display next image
                if(counter < movieList.size()){
                    counter++;
                    Picasso.get().load(movieList.get(counter).getImage()).into(imageViewStory);
                }
            }

            @Override
            public void onPrev() {
                if(counter > 0){
                    counter--;
                    Picasso.get().load(movieList.get(counter).getImage()).into(imageViewStory);
                }

            }

            @Override
            public void onComplete() {
                //Rest counter
                counter = 0 ;
                Toast.makeText(MainActivity.this ,  getString(R.string.message_done) , Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onFirebaseLoadFailed(String message) {
        Toast.makeText(MainActivity.this ,  message , Toast.LENGTH_LONG).show();
    }
}
