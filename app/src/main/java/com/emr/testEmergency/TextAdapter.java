package com.emr.testEmergency;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.emr.testEmergency.services.*;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class TextAdapter extends RecyclerView.Adapter<TextAdapter.TextViewHolder> {
    private static List<String> textList;
    private static long lastClickTime = 0;
    private static Context context;

    public TextAdapter(Context context, List<String> textList) {
        this.textList = textList;
        this.context = context;
    }

    @NonNull
    @Override
    public TextViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new TextViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextViewHolder holder, int position) {
        holder.textView.setText(textList.get(position));
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return textList.size();
    }

    static class TextViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public TextViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }

        public void bind(int position) {
            textView.setOnClickListener(v -> {
                long clickTime = System.currentTimeMillis();
                if (clickTime - lastClickTime < 300) {
                    onItemDoubleClick(position);
                }
                lastClickTime = clickTime;
            });

            textView.setOnLongClickListener(v -> {
                String selectedItem = textList.get(position);
                onItemLongClick(selectedItem);
                return true;
            });
        }

        private void onItemLongClick(String selectedItem) {new services().makeCall(context,selectedItem);}

        private void onItemDoubleClick(int position) {new services().sendMessage(context, textList.get(position));}

    }
}