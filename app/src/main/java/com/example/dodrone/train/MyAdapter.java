package com.example.dodrone.train;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dodrone.R;

import java.io.File;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    private Context context;
    //private IndexListener indexListener;

    Bundle mSaverInstanceState;
    ArrayList<Column> columnArrayList;
    CameraFragment cameraFragment;

    public MyAdapter(Context context, ArrayList<Column> columnArrayList) {
        this.context = context;
        this.columnArrayList = columnArrayList;
    }

    /*
    //set callBack method from MainActivity
    public void setCallBackListener(IndexListener indexListener) {
        this.indexListener = indexListener;
    }*/

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Column column = columnArrayList.get(position);
        holder.labelName.setText(column.label);
        holder.guidePic.setImageResource(column.guidePics);
        holder.previewPic.setImageBitmap(column.bitmap);
        //holder.previewPic.setImageDrawable(column.d);
        //holder.previewPic.setImageDrawable(column.d);
        //((MyViewHolder) holder).bind(indexListener);
    }

    @Override
    public int getItemCount() {
        return columnArrayList.size();
    }

    public int getIndex() {
        return columnArrayList.indexOf(this);
    }

    /*
    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }
*/
    public static class MyViewHolder extends RecyclerView.ViewHolder implements FragmentManager.OnBackStackChangedListener {

        TextView labelName;
        ImageView guidePic;
        ImageView previewPic;
        File photoFile;
        Button camBtn;
        Context mContext;
        public int viewIndex;
        //private IndexListener indexListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            labelName = itemView.findViewById(R.id.label);
            guidePic = itemView.findViewById(R.id.guidepic);
            previewPic = itemView.findViewById(R.id.previewPic);
            camBtn = itemView.findViewById(R.id.camBtn);

            CameraFragment cameraFragment = new CameraFragment();
            mContext = itemView.getContext();



            camBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MyAdapter", "Camera Fragment inflate");
                    Bundle indexBundle = new Bundle(1);
                    indexBundle.putInt("index", getAdapterPosition());
                    cameraFragment.setArguments(indexBundle);
                    pushFragment(cameraFragment, mContext);
                    viewIndex = getAdapterPosition();
                    Log.d("whatswrong", "camBtn index: "+mContext+"\n"+viewIndex);

                    //indexListener.callBack(getAdapterPosition());
                }
            });



        }

        @Override
        public void onBackStackChanged() {
        }

        /*
        public void bind(IndexListener indexListener) {
            this.indexListener = indexListener; //전달받은 콜백을 해당 뷰홀더의 멤버로 갖고 있음
        }*/

        public void pushFragment(CameraFragment newFragment, Context context){

            FragmentTransaction transaction = ((FragmentActivity)context).getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.camFrame, newFragment);
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();


        }
    }
}
