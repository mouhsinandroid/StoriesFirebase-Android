package com.mouhsinbourqaiba.android.storiesfirebase.action;

import com.mouhsinbourqaiba.android.storiesfirebase.model.Movies;

import java.util.List;

public interface IFirebaseLoadDone {
    void onFirebaseLoadSuccess(List<Movies> movieList);
    void onFirebaseLoadFailed(String message);

}
