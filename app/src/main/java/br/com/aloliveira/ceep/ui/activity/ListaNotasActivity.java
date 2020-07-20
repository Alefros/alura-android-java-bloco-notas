package br.com.aloliveira.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aloliveira.ceep.R;
import br.com.aloliveira.ceep.dao.NotaDAO;
import br.com.aloliveira.ceep.model.Nota;
import br.com.aloliveira.ceep.ui.recyclerview.adapter.ListaNotasAdapter;

import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.CHAVE_NOTA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.RESULT_CODE_NOTA_CRIADA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.RESULT_CODE_NOTA_ENVIADA;

public class ListaNotasActivity extends AppCompatActivity {

    private ListaNotasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_notas);
        List<Nota> notas = carregaTodasNotas();
        configuraRecyclerView(notas);
        chamaFormularioNota();
    }

    private void chamaFormularioNota() {
        TextView insereNota = findViewById(R.id.lista_notas_insere_nota);
        insereNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent formularioNota = new Intent(ListaNotasActivity.this,
                        FormularioNotaActivity.class);
                startActivityForResult(formularioNota, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(verificaIntentRecebida(requestCode, resultCode, data)){
            adicionaNotaRecebida(data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void adicionaNotaRecebida(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean verificaIntentRecebida(int requestCode, int resultCode, @Nullable Intent data) {
        return (requestCode == RESULT_CODE_NOTA_ENVIADA) && (resultCode == RESULT_CODE_NOTA_CRIADA) && (data.hasExtra(CHAVE_NOTA));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void configuraRecyclerView(List<Nota> notas) {
        RecyclerView listaNotas = findViewById(R.id.lista_notas_recyclerview);
        configuraAdapter(listaNotas, notas);
    }

    private void configuraAdapter(RecyclerView listaNotas, List<Nota> notas) {
        adapter = new ListaNotasAdapter(this, notas);
        listaNotas.setAdapter(adapter);
    }

    private List<Nota> carregaTodasNotas() {
        NotaDAO notaDAO = new NotaDAO();
        return notaDAO.todos();
    }
}