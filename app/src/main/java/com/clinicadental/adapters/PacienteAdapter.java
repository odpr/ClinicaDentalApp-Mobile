package com.clinicadental.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.clinicadental.R;
import com.clinicadental.models.Paciente;
import java.util.List;

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.VH> {
    private final List<Paciente> data;
    public PacienteAdapter(List<Paciente> data){ this.data = data; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_paciente, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Paciente p = data.get(pos);
        h.tvNombre.setText(p.getNombreCompleto());
        h.tvDocumento.setText(p.getDocumento());
    }

    @Override
    public int getItemCount() { return data.size(); }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDocumento;
        VH(View v){
            super(v);
            tvNombre = v.findViewById(R.id.tvNombre);
            tvDocumento = v.findViewById(R.id.tvDocumento);
        }
    }
}
