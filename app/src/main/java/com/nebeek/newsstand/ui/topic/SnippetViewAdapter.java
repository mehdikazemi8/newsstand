package com.nebeek.newsstand.ui.topic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    private String getSourceDate(Snippet snippet) {
        return String.format(context.getString(R.string.template_date_source), snippet.getSource(), snippet.getDate());
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // todo remove

        try {
            Log.d("TAG", "abcd " + items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData().length);
            byte[] result = items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData();

            Bitmap bmp = BitmapFactory.decodeByteArray(
                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData(),
                    0,
                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData().length
            );

            holder.sourcePhoto.setImageBitmap(bmp);
            holder.photo.setImageBitmap(bmp);
//            Glide.with(context).load(bmp).into(holder.sourcePhoto);

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        holder.title.setVisibility(View.GONE);
        holder.title.setText(items.get(position).getTitle());

        holder.date.setText(items.get(position).getDate());
        holder.source.setText(items.get(position).getSource());

        if (items.get(position).getPayload().getMessage().isEmpty()) {
            holder.description.setText(items.get(position).getPayload().getMedia().getCaption());
        } else {
            holder.description.setText(items.get(position).getPayload().getMessage());
        }

        if (position % 2 == 0) {
            holder.photo.setVisibility(View.VISIBLE);
//            Glide.with(context).load("http://lorempixel.com/output/sports-q-c-50-50-5.jpg").into(holder.photo);
        } else {
            holder.photo.setVisibility(View.GONE);
        }

//        Glide.with(context).load("http://lorempixel.com/output/sports-q-c-50-50-5.jpg").into(holder.sourcePhoto);
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
        @BindView(R.id.source)
        TextView source;
        @BindView(R.id.photo)
        ImageView photo;
        @BindView(R.id.source_photo)
        ImageView sourcePhoto;

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
