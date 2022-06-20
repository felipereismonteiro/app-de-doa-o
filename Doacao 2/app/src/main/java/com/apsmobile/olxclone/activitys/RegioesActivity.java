package com.apsmobile.olxclone.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.adapter.RegiaoAdapter;
import com.apsmobile.olxclone.helper.RegioesList;
import com.apsmobile.olxclone.helper.SPFiltro;

public class RegioesActivity extends AppCompatActivity implements RegiaoAdapter.OnClickListener {

    private RecyclerView rv_regioes;
    private Boolean acesso = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regioes);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            acesso = bundle.getBoolean("filtros");
        }

        iniciaComponentes();

        configCliques();

        configRv();

    }

    private void configRv() {
        rv_regioes.setLayoutManager(new LinearLayoutManager(this));
        rv_regioes.setHasFixedSize(true);
        RegiaoAdapter regiaoAdapter = new RegiaoAdapter(RegioesList.getList(SPFiltro.getFiltro(this).getEstado().getUf()), this);
        rv_regioes.setAdapter(regiaoAdapter);
    }

    private void configCliques(){
        findViewById(R.id.ib_voltar).setOnClickListener(v -> finish());
    }

    private void iniciaComponentes(){
        TextView text_toolbar = findViewById(R.id.text_toolbar);
        text_toolbar.setText("Selecione a região");

        rv_regioes = findViewById(R.id.rv_regioes);
    }

    @Override
    public void OnClick(String regiao) {
        if(!regiao.equals("Todas as regiões")){
            SPFiltro.setFiltro(this, "ddd", regiao.substring(4, 6));
            SPFiltro.setFiltro(this, "regiao", regiao);
        }else {
            SPFiltro.setFiltro(this, "ddd", "");
            SPFiltro.setFiltro(this, "regiao", "");
        }

        if(acesso){
            finish();
        }else {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}