package com.example.bill.listitemmovetest;

/**
 * Created by Bill on 2017/12/12.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bill on 2017/9/20.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<String> list = new ArrayList<>();

    public MyAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 20; i++) {
            list.add("Hello World!" + (i + 1));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        parent.setClipChildren(false);
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private MyImageView imageView;
        private Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (MyImageView) itemView.findViewById(R.id.image);
            this.button = (Button) itemView.findViewById(R.id.btn);
        }

        private void update(final int position) {
            if (position == 1)
                imageView.setImageResource(R.mipmap.icon_bg_type_1);
            else imageView.setImageResource(R.mipmap.ic_launcher);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int[] location = new int[2];
                    itemView.getLocationOnScreen(location);
                    int y = location[1];

                    myClickListener.onClick(imageView, y, position);
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "" + position, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    MyClickListener myClickListener;

    public void setMyClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface MyClickListener {
        void onClick(View view, int y, int position);
    }

}