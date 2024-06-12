package com.example.easysplit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.easysplit.Modelos.Gasto;
import com.example.easysplit.R;

import java.util.List;

public class GastoAdapter extends ArrayAdapter<Gasto> {

    public GastoAdapter(Context context, List<Gasto> gastos) {
        super(context, 0, gastos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Gasto gasto = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_gasto, parent, false);
        }

        TextView textViewConcepto = convertView.findViewById(R.id.textViewConcepto);
        TextView textViewImporte = convertView.findViewById(R.id.textViewImporte);

        textViewConcepto.setText(gasto.getConcepto());
        textViewImporte.setText(String.valueOf(gasto.getImporte()));

        return convertView;
    }
}
