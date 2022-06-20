package com.apsmobile.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.model.Estado;

import java.util.List;

public class EstadoAdapter extends RecyclerView.Adapter<EstadoAdapter.MyViewHolder> {

    private final List<Estado> estadoList;
    private final OnClickListener onClickListener;

    public EstadoAdapter(List<Estado> estadoList, OnClickListener onClickListener) {
        this.estadoList = estadoList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.estado_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Estado estado = estadoList.get(position);

        holder.text_estado.setText(estado.getNome());

        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(estado));
    }

    @Override
    public int getItemCount() {
        return estadoList.size();
    }

    public interface OnClickListener{
        void OnClick(Estado estado);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView text_estado;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            text_estado = itemView.findViewById(R.id.text_estado);
        }
    }
}
