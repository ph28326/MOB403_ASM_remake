package com.mastercoding.asmremake.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mastercoding.asmremake.Model.Phone;
import com.mastercoding.asmremake.R;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.ViewHolder> {
    private List<Phone> list;

    private Callback callback;

    public PhoneAdapter(List<Phone> list, Callback callback) {
        this.list = list;
        this.callback = callback;
    }

    public void setTableItems(List<Phone> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PhoneAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneAdapter.ViewHolder holder, int position) {
        Phone model = list.get(position);
        if (model == null) {
            return;
        }
        holder.txtPrice.setText(String.valueOf(model.getPrice()));
        holder.txtName.setText(model.getName());
        holder.txtBrand.setText(model.getBrand());
        holder.txtQuantiny.setText(String.valueOf(model.getQuantity()));
//        Picasso.get().load(album.getUrl()).into(holder.img);
        holder.imgEdit.setOnClickListener(view -> {
            callback.editPhone(model);
        });
        holder.imgDelete.setOnClickListener(view -> {
            callback.deletePhone(model);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtName;
        private TextView txtBrand;
        private TextView txtPrice;
        private TextView txtQuantiny;
        private ImageView imgEdit;
        private ImageView imgDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtBrand = itemView.findViewById(R.id.txtBrand);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtQuantiny = itemView.findViewById(R.id.txtQuantiny);
            imgDelete = (ImageView) itemView.findViewById(R.id.imgDelete);
            imgEdit = (ImageView) itemView.findViewById(R.id.imgEdit);
        }
    }

    public interface Callback {
        void editPhone(Phone model);

        void deletePhone(Phone model);
    }

}
