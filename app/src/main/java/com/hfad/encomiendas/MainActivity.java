package com.hfad.encomiendas;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper myDb;
    private EditText etDescripcion, etPeso;
    private EditText etRemitenteNombre, etRemitenteTelefono;
    private EditText etDestinatarioNombre, etDestinatarioTelefono, etDestinatarioDireccion;
    private Button btnGuardar, btnConsultar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDb = new DatabaseHelper(this);

        etDescripcion = findViewById(R.id.etDescripcion);
        etPeso = findViewById(R.id.etPeso);
        etRemitenteNombre = findViewById(R.id.etRemitenteNombre);
        etRemitenteTelefono = findViewById(R.id.etRemitenteTelefono);
        etDestinatarioNombre = findViewById(R.id.etDestinatarioNombre);
        etDestinatarioTelefono = findViewById(R.id.etDestinatarioTelefono);
        etDestinatarioDireccion = findViewById(R.id.etDestinatarioDireccion);
        btnGuardar = findViewById(R.id.btnGuardar);
        btnConsultar = findViewById(R.id.btnConsultar);

        btnGuardar.setOnClickListener(v -> guardarEncomienda());
        btnConsultar.setOnClickListener(v -> consultarEncomiendas());
    }

    private void guardarEncomienda() {
        String descripcion = etDescripcion.getText().toString().trim();
        String pesoTexto = etPeso.getText().toString().trim();
        String remNombre = etRemitenteNombre.getText().toString().trim();
        String remTelefono = etRemitenteTelefono.getText().toString().trim();
        String destNombre = etDestinatarioNombre.getText().toString().trim();
        String destTelefono = etDestinatarioTelefono.getText().toString().trim();
        String destDireccion = etDestinatarioDireccion.getText().toString().trim();

        // Validar que no haya campos vacíos
        if (descripcion.isEmpty() || pesoTexto.isEmpty() || remNombre.isEmpty()
                || remTelefono.isEmpty() || destNombre.isEmpty()
                || destTelefono.isEmpty() || destDireccion.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_LONG).show();
            return;
        }

        // Validar que el peso sea un número
        double peso;
        try {
            peso = Double.parseDouble(pesoTexto);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "El peso debe ser un número válido", Toast.LENGTH_LONG).show();
            return;
        }

        boolean insertado = myDb.insertData(descripcion, peso, remNombre, remTelefono,
                destNombre, destTelefono, destDireccion);

        if (insertado) {
            Toast.makeText(this, "Encomienda guardada", Toast.LENGTH_LONG).show();
            limpiarCampos();
        } else {
            Toast.makeText(this, "No se pudo guardar", Toast.LENGTH_LONG).show();
        }
    }

    private void consultarEncomiendas() {
        Cursor res = myDb.getAllData();

        if (res.getCount() == 0) {
            res.close();
            mostrarMensaje("Aviso", "No hay encomiendas guardadas");
            return;
        }

        StringBuilder buffer = new StringBuilder();
        while (res.moveToNext()) {
            buffer.append("ID: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_ID))).append("\n");
            buffer.append("Descripción: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPCION))).append("\n");
            buffer.append("Peso (kg): ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_PESO))).append("\n");
            buffer.append("Remitente: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_REM_NOMBRE))).append("\n");
            buffer.append("Tel. remitente: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_REM_TELEFONO))).append("\n");
            buffer.append("Destinatario: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_DEST_NOMBRE))).append("\n");
            buffer.append("Tel. destinatario: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_DEST_TELEFONO))).append("\n");
            buffer.append("Dirección: ").append(res.getString(
                    res.getColumnIndexOrThrow(DatabaseHelper.COL_DEST_DIRECCION))).append("\n\n");
        }
        res.close();

        mostrarMensaje("Encomiendas guardadas", buffer.toString());
    }

    private void mostrarMensaje(String titulo, String mensaje) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.show();
    }

    private void limpiarCampos() {
        etDescripcion.setText("");
        etPeso.setText("");
        etRemitenteNombre.setText("");
        etRemitenteTelefono.setText("");
        etDestinatarioNombre.setText("");
        etDestinatarioTelefono.setText("");
        etDestinatarioDireccion.setText("");
    }
}