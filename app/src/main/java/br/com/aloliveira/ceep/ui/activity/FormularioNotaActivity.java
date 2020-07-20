package br.com.aloliveira.ceep.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import br.com.aloliveira.ceep.R;
import br.com.aloliveira.ceep.model.Nota;

import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.CHAVE_NOTA;
import static br.com.aloliveira.ceep.ui.activity.ListaNotasConstantes.RESULT_CODE_NOTA_CRIADA;

public class FormularioNotaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_nota);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_formulario_nota_salva, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (ehMenuSalvaNota(item)) {
            Nota novaNota = criaNota();
            devolveNotaCriada(novaNota);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean ehMenuSalvaNota(@NonNull MenuItem item) {
        return item.getItemId() == R.id.menu_formulario_nota_salva_item;
    }

    private void devolveNotaCriada(Nota nota) {
        Intent resultadoInsercao = new Intent();
        resultadoInsercao.putExtra(CHAVE_NOTA, nota);
        setResult(RESULT_CODE_NOTA_CRIADA, resultadoInsercao);
    }

    private Nota criaNota() {
        TextView titulo = findViewById(R.id.formulario_nota_titulo);
        TextView descricao = findViewById(R.id.formulario_nota_descricao);
        return new Nota(titulo.getText().toString(),
                descricao.getText().toString());
    }
}