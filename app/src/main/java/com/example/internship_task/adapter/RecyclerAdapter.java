package com.example.internship_task.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.internship_task.R;
import com.example.internship_task.model.RequestModel;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyHolder> {
    Context context;
    ArrayList<RequestModel> items;

    public RecyclerAdapter(Context context, ArrayList<RequestModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(context).inflate(R.layout.item_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.lbl_response.setText(items.get(position).getResponse());
        holder.edtUri.setText(items.get(position).getUrl());
        holder.methode.setText(items.get(position).getMethod());
        holder.res_hd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lbl_response.setText(items.get(position).getResponseHeaders());
            }
        });
        holder.body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lbl_response.setText(items.get(position).getResponse());
            }
        });   holder.req_hd_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.lbl_response.setText(items.get(position).getRequestHeaders());
            }
        });
    }




    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView lbl_response;
        EditText edtUri;
        TextView methode;
        Button body;
        Button res_hd_btn;
        Button req_hd_btn;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            lbl_response = itemView.findViewById(R.id.lbl_response);
            edtUri = itemView.findViewById(R.id.edt_uri);
            methode = itemView.findViewById(R.id.methode);
            body = itemView.findViewById(R.id.body_btn);
            res_hd_btn = itemView.findViewById(R.id.res_hd_btn);
            req_hd_btn = itemView.findViewById(R.id.req_hd_btn);

        }

    }
}
