package com.example.bimosektiw.bimotakehome;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bimosektiw.bimotakehome.adapter.UserAdapter2;
import com.example.bimosektiw.bimotakehome.model.User;
import com.example.bimosektiw.bimotakehome.model.UserList;
import com.example.bimosektiw.bimotakehome.service.RestApiBuilder;
import com.example.bimosektiw.bimotakehome.service.RestApiService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    private UserAdapter2 adapter2;
    private PublishSubject<String> subject;
    private List<User> user;
    private int i = 2;
    private int x = 2;

    @BindView(R.id.recycler_user_list) RecyclerView recyclerView;
    @BindView(R.id.edit_text) EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplication());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter2);

        //edittext handle change to fast
        subject = PublishSubject.create();
        subject.debounce(100, TimeUnit.MILLISECONDS)
                .onBackpressureLatest()
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                    }
                });
        editText.addTextChangedListener(searchWatcher);

        searchButton();
    }

    private void getUsersData(final String textSearch, int page, int per_page) {

        RestApiService apiService = new RestApiBuilder().getService();
        Call<UserList> userListCall = apiService.getUserList(textSearch,page,per_page);
        userListCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.isSuccessful()) {
                    final UserList userList = response.body();
                    if(userList.getTotalCount() != 0){
                        user = userList.getItems();
                        adapter2 = new UserAdapter2(userList.getItems());
                        recyclerView.setAdapter(adapter2);
                        recyclerView.setVisibility(View.VISIBLE);
                        recyclerView.addOnScrollListener(scrollData(2));
                        x=i+1;
                    }
                    else{
                        recyclerView.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this,
                                "Data not found, please search with another word",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {

                    System.out.println("Request not successfull");
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Request failed. Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getNewUsersData(final String textSearch, int page, int per_page) {

        RestApiService apiService = new RestApiBuilder().getService();
        Call<UserList> userListCall = apiService.getUserList(textSearch,page,per_page);
        userListCall.enqueue(new Callback<UserList>() {
            @Override
            public void onResponse(Call<UserList> call, Response<UserList> response) {
                if (response.isSuccessful()) {
                    final UserList userList = response.body();
                    if(userList != null){
                        for(int i = 0; i<5; i++){
                            User uss = new User();
                            uss.setLogin(userList.getItems().get(i).getLogin());
                            uss.setAvatarUrl(userList.getItems().get(i).getAvatarUrl());
                            user.add(uss);
                        }
                        adapter2.notifyDataSetChanged();
                    }
                }
                else {
                    System.out.println("Request not successfull");
                }
            }

            @Override
            public void onFailure(Call<UserList> call, Throwable t) {
                Toast.makeText(MainActivity.this,
                        "Request failed. Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private EndlessOnScrollListener scrollData(final int page) {
        return new EndlessOnScrollListener() {
            @Override
            public void onLoadMore() {
                if(x != i){
                    getNewUsersData(editText.getText().toString(), x, 15);
                    x += 1;
                }else{
                    getNewUsersData(editText.getText().toString(), page, 15);

                }
            }
        };
    }

    private void searchButton(){
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getUsersData(editText.getText().toString(),1,15);
                }
                return false;
            }
        });
    }

    private final TextWatcher searchWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            subject.onNext(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() == 0){
                recyclerView.setVisibility(View.GONE);
            }
            else{
                getUsersData(editText.getText().toString(),1,10);
            }
        }
    };
}
