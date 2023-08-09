package com.example.solver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class E_Adapter extends RecyclerView.Adapter<E_Adapter.MyViewHolder> {

    Context context;
    ArrayList<String> equation;
    private OnClickListener onClickListener;

    public E_Adapter(Context context, ArrayList<String> equation){
        this.context = context;
        this.equation = equation;
    }

    @NonNull
    @Override
    public E_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =LayoutInflater.from(context);
        View view =inflater.inflate(R.layout.recycler_view_row, parent,false);

        return new E_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull E_Adapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.text.setText(equation.get(position));
        String eq = equation.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, eq);
                }
            }
        });
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(int position, String eq);
    }

    @Override
    public int getItemCount() {
        return equation.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.textView);
        }
    }
}
