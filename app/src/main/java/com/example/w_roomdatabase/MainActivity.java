package com.example.w_roomdatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.Insert;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRegister,btnQuery,btnDelete,btnUpdate;
    private EditText editName, editEmail, editUpdateName, editUpdateEmail,editUpdateId;
    private TextView txtId, txtName, txtEmail;
    private UserDatabase userDatabase;
    private String TAG = "hank";
    private User user;
    private  MainVIewModel mainVIewModel;
//    private List<User> userList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        btnQuery = findViewById(R.id.btnQuery);
        btnQuery.setOnClickListener(this);
        btnDelete = findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        editUpdateName = findViewById(R.id.updateName);
        editUpdateEmail = findViewById(R.id.updateEmail);
        editUpdateId = findViewById(R.id.updateId);

        txtId = findViewById(R.id.txtId);
        txtName = findViewById(R.id.txtName);
        txtEmail = findViewById(R.id.txtEmail);


        userDatabase = Room.databaseBuilder(this,UserDatabase.class,"usersdb")
                .allowMainThreadQueries() //刪,修,查可以不用執行續跑,因為有允許主程式跑
                .build();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                insert();
                break;
            case R.id.btnQuery:
                query();
                break;
            case R.id.btnDelete:
                delete();
                break;
            case R.id.btnUpdate:
                update();
                break;
        }
    }

    private void update() {
        int newId = Integer.parseInt(editUpdateId.getText().toString());
        String newName = editUpdateName.getText().toString();
        String newEmail = editUpdateEmail.getText().toString();

        //將使用者輸入的設定到User更新
        User user = new User();
        user.setId(newId);
        user.setName(newName);
        user.setEmail(newEmail);
        userDatabase.userDao().updateUser(user);

        //Query取得使用者的資料,尋訪撈出資料設定Text
        List<User>userList = userDatabase.userDao().getUsers();
        for(User newUser : userList){
            txtId.setText(String.valueOf(newUser.getId()));
            txtName.setText(newUser.getName());
            txtEmail.setText(newUser.getEmail());
        }

    }

    private void delete() {
        int i = 0;
        User user = new User();

        //設定要刪除的id,刪除此筆資料
        user.setId(i);
        Log.v(TAG,"delete():" + "/id:" + user.getId());
        userDatabase.userDao().deleteUser(user);

    }

    private void query() {
        //取得使用者的資料
        List<User> userList = userDatabase.userDao().getUsers();

        //一開始Text都設定為空
            txtId.setText("空");
            txtName.setText("空");
            txtEmail.setText("空");
            Log.v(TAG,"Query =>" +"userList為空");

        //如果有userList不是空的,進來拿資料呈現ui,如果有資料才會進來改,所以沒有的話就只跑上面的為空
        if (userList != null) {
            for (User user : userList) {
                int i = user.getId();
                String name = user.getName();
                String email = user.getEmail();

                String id = String.valueOf(i);
                txtId.setText(id);
                txtName.setText(name);
                txtEmail.setText(email);

                Log.v(TAG, "query()=>" + "id:" + id + "/name:" + name + "/email:" + email);
            }
        }

    }

    private void insert() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();
        user = new User(name,email);
        Log.v(TAG,"insert():" + "/name:" + name + "/email:" + email);
        new InsertAsyncTask(userDatabase.userDao()).execute(user);

    }

    private static class InsertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        InsertAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {
            for(User user :users){
                String email = user.getEmail();
                Log.v("hank","email:" + email);
            }
            Log.v("hank","doInBackground:" );
            userDao.addUser(users[0]);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        UpdateAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            userDao.updateUser(users[0]);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        DeleteAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            Log.v("hank","doInBackground:" );
            userDao.deleteUser(users[0]);
            return null;
        }
    }

    private static class QueryAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao userDao;

        QueryAsyncTask(UserDao userDao) {
            this.userDao = userDao;
        }

        @Override
        protected Void doInBackground(User... users) {

            Log.v("hank","doInBackground:" );
            List<User> userList = userDao.getUsers();
            for(User user : userList){

            }
            return null;
        }
    }




}
