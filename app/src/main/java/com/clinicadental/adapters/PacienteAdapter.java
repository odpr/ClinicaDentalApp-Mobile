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

public class PacienteAdapter extends RecyclerView.Adapter<PacienteAdapter.PacienteViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Paciente paciente);
    }

    private final List<Paciente> pacientes;
    private final OnItemClickListener listener;

    public PacienteAdapter(List<Paciente> pacientes, OnItemClickListener listener) {
        this.pacientes = pacientes;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PacienteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_paciente, parent, false);
        return new PacienteViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PacienteViewHolder holder, int position) {
        Paciente p = pacientes.get(position);

        holder.tvNombre.setText(p.getNombreCompleto());

        holder.tvTelefono.setText(
                (p.getTelefono() != null && !p.getTelefono().isEmpty())
                        ? "Tel: " + p.getTelefono()
                        : "Tel: (no registrado)"
        );

        holder.tvEmail.setText(
                (p.getEmail() != null && !p.getEmail().isEmpty())
                        ? "Email: " + p.getEmail()
                        : "Email: (no registrado)"
        );

        holder.tvDireccion.setText(
                (p.getDireccion() != null && !p.getDireccion().isEmpty())
                        ? "Dirección: " + p.getDireccion()
                        : "Dirección: (sin especificar)"
        );

        holder.tvFechaNacimiento.setText(
                (p.getFechaNacimiento() != null && !p.getFechaNacimiento().isEmpty())
                        ? "Nacimiento: " + p.getFechaNacimiento()
                        : "Nacimiento: (sin fecha)"
        );

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(p);
        });
    }

    @Override
    public int getItemCount() {
        return pacientes != null ? pacientes.size() : 0;
    }

    static class PacienteViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvTelefono, tvEmail, tvDireccion, tvFechaNacimiento;

        public PacienteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvTelefono = itemView.findViewById(R.id.tvTelefono);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
            tvFechaNacimiento = itemView.findViewById(R.id.tvFechaNacimiento);
        }
    }
}
