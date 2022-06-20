package com.apsmobile.olxclone.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.activitys.CategoriasActivity;
import com.apsmobile.olxclone.activitys.DetalhesAnuncioActivity;
import com.apsmobile.olxclone.activitys.EstadosActivity;
import com.apsmobile.olxclone.activitys.FiltrosActivity;
import com.apsmobile.olxclone.activitys.FormAnuncioActivity;
import com.apsmobile.olxclone.adapter.AnuncioAdapter;
import com.apsmobile.olxclone.autenticao.LoginActivity;
import com.apsmobile.olxclone.helper.FirebaseHelper;
import com.apsmobile.olxclone.helper.SPFiltro;
import com.apsmobile.olxclone.model.Anuncio;
import com.apsmobile.olxclone.model.Categoria;
import com.apsmobile.olxclone.model.Filtro;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements AnuncioAdapter.OnClickListener {

    private final int REQUEST_CATEGORIA = 100;

    private AnuncioAdapter anuncioAdapter;
    private final List<Anuncio> anuncioList = new ArrayList<>();

    private RecyclerView rv_anuncios;
    private ProgressBar progressBar;
    private TextView text_info;

    private Filtro filtro = new Filtro();

    private SearchView search_view;
    private EditText edit_search_view;

    private Button btn_filtros;
    private Button btn_categorias;
    private Button btn_regioes;

    private Button btn_novo_anuncio;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        iniciaComponentes(view);

        configRV();

        configCliques();

        configSearchView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        configFiltros();
    }

    private void configSearchView(){
        edit_search_view = search_view.findViewById(R.id.search_src_text);

        search_view.findViewById(R.id.search_close_btn).setOnClickListener(v -> {
            limparPesquisa();
        });

        search_view.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SPFiltro.setFiltro(requireActivity(), "pesquisa", query);

                configFiltros();

                ocultarTeclado();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    private void limparPesquisa(){
        search_view.clearFocus();

        edit_search_view.setText("");

        SPFiltro.setFiltro(requireActivity(), "pesquisa", "");

        configFiltros();

        ocultarTeclado();
    }

    private void configFiltros(){
        filtro = SPFiltro.getFiltro(requireActivity());

        if(!filtro.getEstado().getRegiao().isEmpty()){
            btn_regioes.setText(filtro.getEstado().getRegiao());
        }else {
            btn_regioes.setText("Regiões");
        }

        if(!filtro.getCategoria().isEmpty()){
            btn_categorias.setText(filtro.getCategoria());
        }else {
            btn_categorias.setText("Categorias");
        }

        recuperaAnuncios();
    }

    private void recuperaAnuncios() {
        DatabaseReference anunciosRef = FirebaseHelper.getDatabaseReference()
                .child("anuncios_publicos");
        anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    anuncioList.clear();
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        Anuncio anuncio = ds.getValue(Anuncio.class);
                        anuncioList.add(anuncio);
                    }

                    // Filtro por categoria
                    if(!filtro.getCategoria().isEmpty()){
                        if(!filtro.getCategoria().equals("Todas as categorias")){
                            for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                                if(!anuncio.getCategoria().equals(filtro.getCategoria())){
                                    anuncioList.remove(anuncio);
                                }
                            }
                        }
                    }

                    // Filtro por Estado
                    if(!filtro.getEstado().getUf().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getLocal().getUf().contains(filtro.getEstado().getUf())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    // Filtro por DDD
                    if(!filtro.getEstado().getDdd().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getLocal().getUf().equals(filtro.getEstado().getUf())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    // Filtro por nome pesquisado
                    if(!filtro.getPesquisa().isEmpty()){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(!anuncio.getTitulo().toLowerCase().contains(filtro.getPesquisa().toLowerCase())){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    // Filtro por valor minimo
                    if(filtro.getValorMin() > 0){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(anuncio.getValor() < filtro.getValorMin()){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    // Filtro por valor maximo
                    if(filtro.getValorMax() > 0){
                        for (Anuncio anuncio : new ArrayList<>(anuncioList)){
                            if(anuncio.getValor() > filtro.getValorMax()){
                                anuncioList.remove(anuncio);
                            }
                        }
                    }

                    if(anuncioList.isEmpty()){
                        text_info.setText("Nenhum anúncio encontrado.");
                    }else {
                        text_info.setText("");
                    }

                } else {
                    text_info.setText("Nenhum anúncio cadastrado.");
                }

                progressBar.setVisibility(View.GONE);
                Collections.reverse(anuncioList);
                anuncioAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void configRV() {
        rv_anuncios.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_anuncios.setHasFixedSize(true);
        anuncioAdapter = new AnuncioAdapter(anuncioList, this);
        rv_anuncios.setAdapter(anuncioAdapter);
    }

    private void configCliques() {
        btn_novo_anuncio.setOnClickListener(v -> {
            if (FirebaseHelper.getAutenticado()) {
                startActivity(new Intent(getActivity(), FormAnuncioActivity.class));
            } else {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }
        });

        btn_categorias.setOnClickListener(v -> {
            Intent intent = new Intent(requireActivity(), CategoriasActivity.class);
            intent.putExtra("todas", true);
            startActivityForResult(intent, REQUEST_CATEGORIA);
        });

        btn_filtros.setOnClickListener(v -> startActivity(new Intent(requireActivity(), FiltrosActivity.class)));

        btn_regioes.setOnClickListener(v -> startActivity(new Intent(requireActivity(), EstadosActivity.class)));
    }

    private void iniciaComponentes(View view) {
        btn_novo_anuncio = view.findViewById(R.id.btn_novo_anuncio);
        rv_anuncios = view.findViewById(R.id.rv_anuncios);
        progressBar = view.findViewById(R.id.progressBar);
        text_info = view.findViewById(R.id.text_info);
        btn_filtros = view.findViewById(R.id.btn_filtros);
        btn_categorias = view.findViewById(R.id.btn_categorias);
        btn_regioes = view.findViewById(R.id.btn_regioes);
        search_view = view.findViewById(R.id.search_view);
    }

    private void ocultarTeclado(){
        InputMethodManager inputMethodManager = (InputMethodManager)
                getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(search_view.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void OnClick(Anuncio anuncio) {
        Intent intent = new Intent(requireContext(), DetalhesAnuncioActivity.class);
        intent.putExtra("anuncioSelecionado", anuncio);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == REQUEST_CATEGORIA){
                Categoria categoriaSelecionada = (Categoria) data.getExtras().getSerializable("categoriaSelecionada");
                SPFiltro.setFiltro(requireActivity(), "categoria", categoriaSelecionada.getNome());

                configFiltros();
            }
        }
    }
}