package com.example.w_roomdatabase;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MainVIewModel extends ViewModel {
    private MutableLiveData<List<User>> userData;


    public MutableLiveData<List<User>> getUserData() {
        return userData;
    }
}
