package com.nebeek.newsstand.ui.topic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nebeek.newsstand.R;
import com.nebeek.newsstand.data.models.Snippet;
import com.nebeek.newsstand.util.DateManager;
import com.nebeek.newsstand.util.listener.ShowUrlCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnippetViewAdapter extends RecyclerView.Adapter<SnippetViewAdapter.ViewHolder> {

    private Context context;
    private List<Snippet> items;
    private ShowUrlCallback showUrlCallback;

    public SnippetViewAdapter(List<Snippet> items, ShowUrlCallback showUrlCallback) {
        this.items = items;
        this.showUrlCallback = showUrlCallback;
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
            byte[] result = items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData();

            Bitmap bmp = BitmapFactory.decodeByteArray(
                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData(),
                    0,
                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData().length
            );

//            GlideApp.with(context).asBitmap().load(bmp).into(holder.sourcePhoto);
            holder.sourcePhoto.setImageBitmap(bmp);
            holder.photo.setImageBitmap(bmp);
            holder.photo.setVisibility(View.VISIBLE);
//            Glide.with(context).load(bmp).into(holder.sourcePhoto);

        } catch (NullPointerException e) {
            e.printStackTrace();
            holder.photo.setVisibility(View.GONE);
        }

//        holder.title.setVisibility(View.GONE);
//        holder.title.setText(items.get(position).getTitle());

        holder.date.setText(
                DateManager.convertLongToDate(items.get(position).getPayload().getDate())
        );
        holder.source.setText(items.get(position).getChannel());


        if (items.get(position).getPayload().getMessage().isEmpty()) {
            holder.description.setText(items.get(position).getPayload().getMedia().getCaption());
        } else {
            holder.description.setText(items.get(position).getPayload().getMessage());
        }

        Linkify.addLinks(holder.description, Linkify.WEB_URLS);

        showLinks(holder.description, holder.description.getText().toString());

//        if (position % 2 == 0) {
//            holder.photo.setVisibility(View.VISIBLE);
////            Glide.with(context).load("http://lorempixel.com/output/sports-q-c-50-50-5.jpg").into(holder.photo);
//        } else {
//            holder.photo.setVisibility(View.GONE);
//        }

//        Glide.with(context).load("http://lorempixel.com/output/sports-q-c-50-50-5.jpg").into(holder.sourcePhoto);
    }

    private Pair<Integer, Integer> findStartEnd(String message, String url) {

        int bestLen = 0;
        int start = -1;
        int end = -1;
        int thisLen;
        for (int i = message.length() - 1; i >= 0; i--) {
            int j = url.length() - 1;
            int k = i;
            thisLen = 0;
            while (k >= 0 && j >= 0) {
                if (message.charAt(k) == url.charAt(j)) {
                    j--;
                    k--;
                    thisLen++;
                } else {
                    break;
                }
            }

            if (thisLen > bestLen) {
                bestLen = thisLen;
                start = k + 1;
                end = i;
            }
        }

        return new Pair<>(start, end);
    }

    private void showLinks(TextView textView, String message) {
        if (textView.getUrls().length == 0) {
            return;
        }

        URLSpan urlSpan = textView.getUrls()[0];
        SpannableString ss = new SpannableString(message);
        String url = urlSpan.getURL();

        Pair<Integer, Integer> startEnd = findStartEnd(message, url);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                showUrlCallback.onShowUrl(url);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
            }
        };

        ss.setSpan(clickableSpan, startEnd.first, startEnd.second + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
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
