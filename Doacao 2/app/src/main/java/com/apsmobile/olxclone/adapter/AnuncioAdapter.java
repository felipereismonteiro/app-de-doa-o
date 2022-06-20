package com.apsmobile.olxclone.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.helper.GetMask;
import com.apsmobile.olxclone.model.Anuncio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AnuncioAdapter extends RecyclerView.Adapter<AnuncioAdapter.MyViewHolder> {

    private final List<Anuncio> anuncioList;
    private final OnClickListener onClickListener;

    public AnuncioAdapter(List<Anuncio> anuncioList, OnClickListener onClickListener) {
        this.anuncioList = anuncioList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anuncio_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Anuncio anuncio = anuncioList.get(position);

        for (int i = 0; i < anuncio.getUrlImagens().size(); i++) {
            if (anuncio.getUrlImagens().get(i).getIndex() == 0) {
                Picasso.get().load(anuncio.getUrlImagens().get(i).getCaminhoImagem()).into(holder.img_anuncio);
            }
        }

        holder.text_titulo.setText(anuncio.getTitulo());
        holder.text_valor.setText("R$ " + GetMask.getValor(anuncio.getValor()));
        holder.text_local.setText(GetMask.getDate(anuncio.getDataPublicacao(), 4) + ", " + anuncio.getLocal().getBairro());

        holder.itemView.setOnClickListener(v -> onClickListener.OnClick(anuncio));

    }

    @Override
    public int getItemCount() {
        return anuncioList.size();
    }

    public interface OnClickListener {
        void OnClick(Anuncio anuncio);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img_anuncio;
        TextView text_titulo;
        TextView text_valor;
        TextView text_local;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_anuncio = itemView.findViewById(R.id.img_anuncio);
            text_titulo = itemView.findViewById(R.id.text_titulo);
            text_valor = itemView.findViewById(R.id.text_valor);
            text_local = itemView.findViewById(R.id.text_local);
        }
    }

}
