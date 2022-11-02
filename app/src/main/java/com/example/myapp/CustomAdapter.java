package com.example.myapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> listTopics = new ArrayList<>();
    LayoutInflater inflater;

    public CustomAdapter(Context ctx, ArrayList<String> topics){
        this.context = ctx;
        this.listTopics = topics;
        inflater = LayoutInflater.from(ctx);
    }
    @Override
    public int getCount() {
        return listTopics.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {

            view = inflater.inflate(R.layout.list_row, null);
            TextView textview = (TextView) view.findViewById(R.id.txtName);
            ImageView btn = (ImageView) view.findViewById(R.id.start_btn); 
            textview.setText(listTopics.get(i));
            
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewGroup grp = (ViewGroup)view.getParent();
                    TextView txt = (TextView) grp.getChildAt(0);
                    Intent intent  = new Intent(context, QnAPage.class);
                    intent.putExtra("sub", txt.getText().toString());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }
}
