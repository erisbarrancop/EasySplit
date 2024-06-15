package com.example.easysplit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.easysplit.Modelos.Usuario;
import com.example.easysplit.R;

import java.util.ArrayList;
import java.util.List;

public class UserCheckboxSpinnerAdapter extends ArrayAdapter<Usuario> {

    private Context context;
    private List<Usuario> usuarios;
    private List<Usuario> selectedUsuarios;

    public UserCheckboxSpinnerAdapter(Context context, List<Usuario> usuarios, List<Usuario> selectedUsuarios) {
        super(context, 0, usuarios);
        this.context = context;
        this.usuarios = usuarios;
        this.selectedUsuarios = selectedUsuarios;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.spinner_text);
        textView.setText(usuarios.get(position).getNombre());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.spinner_dropdown_item, parent, false);
        }

        Usuario usuario = usuarios.get(position);

        TextView textView = convertView.findViewById(R.id.spinner_text);
        CheckBox checkBox = convertView.findViewById(R.id.spinner_checkbox);

        textView.setText(usuario.getNombre());
        checkBox.setChecked(selectedUsuarios.contains(usuario));

        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!selectedUsuarios.contains(usuario)) {
                    selectedUsuarios.add(usuario);
                }
            } else {
                selectedUsuarios.remove(usuario);
            }
        });

        return convertView;
    }

    public List<Usuario> getSelectedUsuarios() {
        return selectedUsuarios;
    }
}
