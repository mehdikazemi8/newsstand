package com.nebeek.newsstand.ui.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nebeek.newsstand.R;
import com.nebeek.newsstand.data.models.Keyword;
import com.nebeek.newsstand.util.customview.SquareImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeywordViewAdapter extends RecyclerView.Adapter<KeywordViewAdapter.ViewHolder> {

    private Context context;
    private List<Keyword> items;

    public KeywordViewAdapter(List<Keyword> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.template_keyword, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.content.setText(items.get(position).getContent());
        Glide.with(context).load(items.get(position).getPhotoURL()).into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        SquareImageView photo;
        @BindView(R.id.content)
        TextView content;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
