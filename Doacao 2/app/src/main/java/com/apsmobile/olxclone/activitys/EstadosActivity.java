package com.apsmobile.olxclone.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.adapter.EstadoAdapter;
import com.apsmobile.olxclone.helper.EstadosList;
import com.apsmobile.olxclone.helper.SPFiltro;
import com.apsmobile.olxclone.model.Estado;

public class EstadosActivity extends AppCompatActivity implements EstadoAdapter.OnClickListener {

    private RecyclerView rv_estados;
    private Boolean acesso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            acesso = bundle.getBoolean("filtros");
        }

        iniciaComponentes();

        configRv();

        configCliques();
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void configRv(){
        rv_estados.setLayoutManager(new LinearLayoutManager(this));
        rv_estados.setHasFixedSize(true);
        EstadoAdapter estadoAdapter = new EstadoAdapter(EstadosList.getList(), this);
        rv_estados.setAdapter(estadoAdapter);
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Estados");

        rv_estados = findViewById(R.id.rv_estados);
    }

    @Override
    public void OnClick(Estado estado) {
        if(!estado.getNome().equals("Brasil")){
            SPFiltro.setFiltro(this, "ufEstado", estado.getUf());
            SPFiltro.setFiltro(this, "nomeEstado", estado.getNome());

            if(acesso){
                finish();
            }else {
                startActivity(new Intent(this, RegioesActivity.class));
            }
        }else {
            SPFiltro.setFiltro(this, "ufEstado", "");
            SPFiltro.setFiltro(this, "nomeEstado", "");

            finish();
        }
    }
}