package com.nebeek.newsstand.ui.topic;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
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
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.subscribes.TopicViewAdapter;
import com.nebeek.newsstand.util.DateManager;
import com.nebeek.newsstand.util.imagehandler.GlideApp;
import com.nebeek.newsstand.util.listener.OnItemSelectedListener;
import com.nebeek.newsstand.util.listener.ShowUrlCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SnippetViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Snippet> items;
    private List<Topic> topics = null;
    private ShowUrlCallback showUrlCallback;
    private Topic parentTopic;
    private OnItemSelectedListener<Topic> relatedTopicSelectedListener;

    public SnippetViewAdapter(Topic parentTopic, List<Topic> topics, List<Snippet> items,
                              ShowUrlCallback showUrlCallback,
                              @Nullable OnItemSelectedListener<Topic> relatedTopicSelectedListener) {
        this.parentTopic = parentTopic;
        this.topics = topics;
        this.items = items;
        this.showUrlCallback = showUrlCallback;
        this.relatedTopicSelectedListener = relatedTopicSelectedListener;
    }

    @Override
    public int getItemViewType(int position) {
        if (topics == null) {
            return 0;
        }

        if (position == items.size() - 2) {
            return 1; // another recyclerView
        } else {
            return 0; // Snippet
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.template_snippet, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.template_list_row, parent, false);
            return new ListViewHolder(view);
        }

    }

    private String getSourceDate(Snippet snippet) {
        return String.format(context.getString(R.string.template_date_source), snippet.getSource().getNames().get(0).getFa(), snippet.getDateCreated());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == 0) {
            bindData(holder, position);
        } else {
            bindRelatedTopics(holder, position);
        }
    }

    private void bindRelatedTopics(RecyclerView.ViewHolder holder, int position) {

        ((ListViewHolder) holder).parentTopicName.setText(
                context.getString(R.string.template_parent_topic_name, parentTopic.getNames().get(0).getFa())
        );
        TopicViewAdapter adapter = new TopicViewAdapter(
                topics,
                relatedTopic -> relatedTopicSelectedListener.onSelect(relatedTopic),
                R.layout.template_topic_browse
        );
        ((ListViewHolder) holder).topics.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        ((ListViewHolder) holder).topics.setLayoutManager(layoutManager);
    }

    private void bindData(RecyclerView.ViewHolder holder, int position) {
        // todo remove
        try {
//            byte[] result = items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData();
//
//            Bitmap bmp = BitmapFactory.decodeByteArray(
//                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData(),
//                    0,
//                    items.get(position).getPayload().getMedia().getPhoto().getSizes().get(0).getBytes().getData().length
//            );

//            GlideApp.with(context).asBitmap().load(bmp).into(holder.sourcePhoto);
            GlideApp.with(context).load(items.get(position).getSource().getImageSets().get(0).getImages().get(0).getData())
                    .circleCrop()
                    .into(((ViewHolder) holder).sourcePhoto);
//            holder.sourcePhoto.setImageBitmap(items.get(position).getSource().getImageSets().get(0).getImages().get(0).getData());
//            holder.photo.setImageBitmap(bmp);
            if (items.get(position).getImageSets().size() > 0) {
                ((ViewHolder) holder).photo.setVisibility(View.VISIBLE);
                GlideApp.with(context).load(items.get(position).getImageSets().get(0).getImages().get(0).getData()).into(((ViewHolder) holder).photo);
            } else {
                ((ViewHolder) holder).photo.setVisibility(View.GONE);
            }

//            Glide.with(context).load(bmp).into(holder.sourcePhoto);

        } catch (NullPointerException e) {
            e.printStackTrace();
            ((ViewHolder) holder).photo.setVisibility(View.GONE);
        }

//        holder.title.setVisibility(View.GONE);
//        holder.title.setText(items.get(position).getTitle());

        ((ViewHolder) holder).date.setText(
                DateManager.convertStringToDate(items.get(position).getDateCreated())
        );
        ((ViewHolder) holder).source.setText(items.get(position).getSource().getNames().get(0).getFa());

        ((ViewHolder) holder).description.setText(items.get(position).getFullText().getFa());

        Linkify.addLinks(((ViewHolder) holder).description, Linkify.WEB_URLS);

        showLinks(((ViewHolder) holder).description, ((ViewHolder) holder).description.getText().toString());

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

        URLSpan[] urlSpans = textView.getUrls();
        SpannableString ss = new SpannableString(message);

        for (int current = 0; current < urlSpans.length; current++) {
            String url = urlSpans[current].getURL();
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
        }

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

    class ListViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.parent_topic_name)
        TextView parentTopicName;
        @BindView(R.id.topics)
        RecyclerView topics;

        public ListViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
