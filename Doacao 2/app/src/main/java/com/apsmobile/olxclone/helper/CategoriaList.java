package com.apsmobile.olxclone.helper;

import com.apsmobile.olxclone.R;
import com.apsmobile.olxclone.model.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaList {

    public static List<Categoria> getList(boolean todas){

        List<Categoria> categoriaList = new ArrayList<>();

        if(todas) categoriaList.add(new Categoria(R.drawable.bg_search_view, "Todas as categorias"));
        categoriaList.add(new Categoria(R.drawable.ic_autos_e_pecas, "Autos e peças"));
        categoriaList.add(new Categoria(R.drawable.ic_imoveis, "Imóveis"));
        categoriaList.add(new Categoria(R.drawable.ic_eletronico_e_celulares, "Eletrônicos e celulares"));
        categoriaList.add(new Categoria(R.drawable.ic_para_a_sua_casa, "Para a sua casa"));
        categoriaList.add(new Categoria(R.drawable.ic_moda_e_beleza, "Moda e beleza"));
        categoriaList.add(new Categoria(R.drawable.ic_esporte_e_lazer, "Esportes e lazer"));
        categoriaList.add(new Categoria(R.drawable.ic_musica_e_hobbies, "Músicas e hobbies"));
        categoriaList.add(new Categoria(R.drawable.ic_artigos_infantis, "Artigos infantis"));
        categoriaList.add(new Categoria(R.drawable.ic_animais_de_estimacao, "Animais de estimação"));


        return categoriaList;
    }

}
