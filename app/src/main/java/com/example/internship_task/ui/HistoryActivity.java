package com.example.internship_task.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.internship_task.R;
import com.example.internship_task.adapter.RecyclerAdapter;
import com.example.internship_task.database.DatabaseHandler;

public class HistoryActivity extends AppCompatActivity {
    DatabaseHandler databaseHandler;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        databaseHandler = new DatabaseHandler(this);
         recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RecyclerAdapter(this, databaseHandler.getAllRequests()));

    }

    public void sort(View view) {
        switch (view.getId())
        {
            case R.id.all_btn:
                recyclerView.setAdapter(new RecyclerAdapter(this, databaseHandler.getAllRequests()));
                break;
            case R.id.post_btn:
                recyclerView.setAdapter(new RecyclerAdapter(this, databaseHandler.sortByMethod("post")));
                break;
            case R.id.get_btn:
                recyclerView.setAdapter(new RecyclerAdapter(this, databaseHandler.sortByMethod("get")));
                break;
            default:
                break;
        }
    }

    public void delete(View view) {
        databaseHandler.delete();
        databaseHandler = new DatabaseHandler(this);
        recreate();

    }
}