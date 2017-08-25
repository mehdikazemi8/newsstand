package com.nebeek.newsstand.ui.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.data.models.Snippet;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnippetViewAdapter extends RecyclerView.Adapter<SnippetViewAdapter.ViewHolder> {

    private Context context;
    private List<Snippet> items;

    public SnippetViewAdapter(List<Snippet> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.template_snippet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(items.get(position).getTitle());
        holder.date.setText(items.get(position).getDate());
        holder.description.setText(items.get(position).getDescription());
        Glide.with(context).load(items.get(position).getPhotoURLs().get(0)).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.photo)
        ImageView photo;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
