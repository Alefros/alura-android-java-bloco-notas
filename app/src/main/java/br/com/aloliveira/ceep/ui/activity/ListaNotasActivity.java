package br.com.aloliveira.ceep.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.aloliveira.ceep.R;
import br.com.aloliveira.ceep.dao.NotaDAO;
import br.com.aloliveira.ceep.model.Nota;
import br.com.aloliveira.ceep.ui.recyclerview.adapter.ListaNotasAdapter;
import br.com.aloliveira.ceep.ui.recyclerview.adapter.listener.OnItemClickListener;

import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.CHAVE_NOTA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.CHAVE_POSICAO;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.POSICAO_INVALIDA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.REQUEST_CODE_ALTERA_NOTA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.RESULT_CODE_INSERE_NOTA;

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
                startActivityForResult(formularioNota, RESULT_CODE_INSERE_NOTA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (ehResultadoInsereNota(requestCode, data)) {
            if(resultadoOk(resultCode)){
                adicionaNotaRecebida(data);
            }
        }
        if (ehResultadoAlteraNota(requestCode, data)) {

            if(resultadoOk(resultCode)){
                Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
                int posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA);

                if (ehPosicaoValida(posicaoRecebida)) {
                    altera(notaRecebida, posicaoRecebida);
                } else {
                    Toast.makeText(this, R.string.messageErrorChangeNote, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void altera(Nota nota, int posicao) {
        new NotaDAO().altera(posicao, nota);
        adapter.altera(posicao, nota);
    }

    private boolean ehPosicaoValida(int posicaoRecebida) {
        return posicaoRecebida > POSICAO_INVALIDA;
    }

    private boolean ehResultadoAlteraNota(int requestCode, @Nullable Intent data) {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data);
    }

    private boolean temNota(@Nullable Intent data) {
        return data.hasExtra(CHAVE_NOTA);
    }

    private boolean resultadoOk(int resultCode) {
        return resultCode == Activity.RESULT_OK;
    }

    private boolean ehCodigoRequisicaoAlteraNota(int requestCode) {
        return requestCode == REQUEST_CODE_ALTERA_NOTA;
    }

    private void adicionaNotaRecebida(@Nullable Intent data) {
        Nota notaRecebida = (Nota) data.getSerializableExtra(CHAVE_NOTA);
        new NotaDAO().insere(notaRecebida);
        adapter.adiciona(notaRecebida);
    }

    private boolean ehResultadoInsereNota(int requestCode, @Nullable Intent data) {
        return (requestCode == RESULT_CODE_INSERE_NOTA) && (temNota(data));
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
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(Nota nota, int posicao) {
                vaiParaFormularioNotaAltera(nota, posicao);
            }
        });
    }

    private void vaiParaFormularioNotaAltera(Nota nota, int posicao) {
        Intent abreFormularioComNota = new Intent(ListaNotasActivity.this, FormularioNotaActivity.class);
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota);
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao);
        startActivityForResult(abreFormularioComNota, REQUEST_CODE_ALTERA_NOTA);
    }

    private List<Nota> carregaTodasNotas() {
        NotaDAO notaDAO = new NotaDAO();

        for (int i = 1; i < 11; i++) {
            notaDAO.insere(new Nota("Titulo " + i,
                    "Descriçao " + i));
        }

        return notaDAO.todos();
    }
}