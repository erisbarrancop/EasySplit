package com.example.easysplit.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.easysplit.Modelos.Plan;
import com.example.easysplit.R;

import java.util.List;

public class PlanAdapter extends BaseAdapter {

    private Context context;
    private List<Plan> plans;

    public PlanAdapter(Context context, List<Plan> plans) {
        this.context = context;
        this.plans = plans;
    }

    @Override
    public int getCount() {
        return plans.size();
    }

    @Override
    public Object getItem(int position) {
        return plans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return plans.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.plan_item, parent, false);
        }

        Plan plan = (Plan) getItem(position);

        TextView nombreTextView = convertView.findViewById(R.id.plan_nombre);
        TextView monedaTextView = convertView.findViewById(R.id.plan_moneda);

        nombreTextView.setText(plan.getNombre());
        monedaTextView.setText(plan.getMoneda());

        return convertView;
    }
}
