package br.com.aloliveira.ceep.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.com.aloliveira.ceep.R;
import br.com.aloliveira.ceep.model.Nota;

@Deprecated
public class ListaNotasAdapter extends BaseAdapter {

    private final Context context;
    private final List<Nota> notas;

    public ListaNotasAdapter(Context context, List<Nota> notas) {
        this.context = context;
        this.notas = notas;
    }

    @Override
    public int getCount() {
        return notas.size();
    }

    @Override
    public Nota getItem(int posicao) {
        return notas.get(posicao);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int posicao, View view, ViewGroup viewGroup) {
        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_nota, viewGroup, false);
        Nota nota = notas.get(posicao);

        TextView titulo = viewCriada.findViewById(R.id.item_nota_titulo);
        titulo.setText(nota.getTitulo());

        TextView descricao = viewCriada.findViewById(R.id.item_nota_descricao);
        descricao.setText(nota.getDescricao());

        return viewCriada;
    }
}
