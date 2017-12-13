package com.nebeek.newsstand.ui.subscribes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.util.customview.SquareImageView;
import com.nebeek.newsstand.util.imagehandler.GlideApp;
import com.nebeek.newsstand.util.listener.OnItemSelectedListener;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopicViewAdapter extends RecyclerView.Adapter<TopicViewAdapter.ViewHolder> {

    private Context context;
    private List<Topic> items;
    private OnItemSelectedListener<Topic> onItemSelectedListener;

    public TopicViewAdapter(List<Topic> items, OnItemSelectedListener<Topic> onItemSelectedListener) {
        this.items = items;
        this.onItemSelectedListener = onItemSelectedListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        View view = LayoutInflater.from(context).inflate(R.layout.template_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.content.setText(items.get(position).getNames().get(0));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (items.get(position).getPhotoURL() == null) {
            items.get(position).setPhotoURL(
                    generatePhotoUrl(position)
            );
        }

        GlideApp.with(context).load(items.get(position).getPhotoURL())
//                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.loading_circle)
                .circleCrop()
                .into(holder.photo);

        holder.unreadCount.setText(String.valueOf(
                Math.abs(new Random().nextInt() % 10 + 3)
        ));
    }

    private String generatePhotoUrl(int position) {
        String[] arr = {
                "https://www.tarafdari.com/sites/default/files/contents/user397134/news/photo_2017-12-13_14-27-36_0.jpg",
                "https://www.tarafdari.com/sites/default/files/contents/user28399/news/atletico_madrids_slovenian_goalkeeper_jan_oblak_looks_on_as_he_w_572425.jpg",
                "https://www.tarafdari.com/sites/default/files/contents/user133918/news/139503301015286907948714_1.jpg",
                "https://www.tarafdari.com/sites/default/files/contents/user130292/news/1511542980_935713_1511544259_noticia_normal.jpg"
        };

        return arr[position % 4];
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.photo)
        SquareImageView photo;
        @BindView(R.id.text)
        TextView content;
        @BindView(R.id.unread_count)
        TextView unreadCount;
        @BindView(R.id.followers_count)
        TextView followersCount;

        @OnClick(R.id.root_view)
        public void rootViewOnClick() {
            onItemSelectedListener.onSelect(items.get(getAdapterPosition()));
        }

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
