package com.call.welny.log;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.call.welny.R;


public class LogsActivity extends AppCompatActivity {

    LogsRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        RecyclerView recyclerView = findViewById(R.id.rv_logs);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LogsRecyclerViewAdapter(this, FileUtils.readFromInternalStorage(this));
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
