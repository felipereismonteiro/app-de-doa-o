package com.apsmobile.olxclone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.activitys.DetalhesAnuncioActivity;
import com.apsmobile.olxclone.adapter.AnuncioAdapter;
import com.apsmobile.olxclone.autenticao.LoginActivity;
import com.apsmobile.olxclone.helper.FirebaseHelper;
import com.apsmobile.olxclone.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FavoritosFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private AnuncioAdapter anuncioAdapter;
    private final List<Anuncio> anuncioList = new ArrayList<>();

    private RecyclerView rv_anuncios;
    private ProgressBar progressBar;
    private TextView text_info;
    private Button btn_logar;

    private final List<String> favoritosList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favoritos, container, false);

        iniciaComponentes(view);

        configRV();

        configCliques();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        recuperaFavoritos();
    }

    private void configCliques() {
        btn_logar.setOnClickListener(v -> startActivity(new Intent(requireContext(), LoginActivity.class)));
    }

    private void recuperaFavoritos() {
        if (FirebaseHelper.getAutenticado()) {
            DatabaseReference favoritosRef = FirebaseHelper.getDatabaseReference()
                    .child("favoritos")
                    .child(FirebaseHelper.getIdFirebase());
            favoritosRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        favoritosList.clear();
                        anuncioList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            favoritosList.add(ds.getValue(String.class));
                        }
                        text_info.setText("");
                    } else {
                        text_info.setText("Nenhum anúncio favoritado.");
                    }
                    progressBar.setVisibility(View.GONE);
                    anuncioAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            text_info.setText("");
            progressBar.setVisibility(View.GONE);
            btn_logar.setVisibility(View.VISIBLE);
        }
    }

    private void configRV() {
        rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_anuncios.setHasFixedSize(true);
        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rv_anuncios.setAdapter(anuncioAdapter);
    }

    private void iniciaComponentes(View view) {
        rv_anuncios = view.findViewById(R.id.rv_anuncios);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
        btn_logar = view.findViewById(R.id.btn_logar);
    }

    @Override
    public void OnClick(Anuncio anuncio) {
        Intent intent = new Intent(requireContext(), DetalhesAnuncioActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);
        startActivity(intent);
    }
}