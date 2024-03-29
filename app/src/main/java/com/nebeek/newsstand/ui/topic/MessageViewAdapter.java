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
import com.nebeek.newsstand.controller.base.BaseMessageListPresenter;
import com.nebeek.newsstand.data.models.TelegramMessage;
import com.nebeek.newsstand.data.models.Topic;
import com.nebeek.newsstand.ui.subscribes.TopicViewAdapter;
import com.nebeek.newsstand.util.imagehandler.GlideApp;
import com.nebeek.newsstand.util.listener.OnItemSelectedListener;
import com.nebeek.newsstand.util.listener.ShowUrlCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TopicViewAdapter topicAdapter;
    private Context context;
    private List<Topic> topics = null;
    private ShowUrlCallback showUrlCallback;
    private Topic parentTopic;
    private OnItemSelectedListener<Topic> relatedTopicSelectedListener;
    private BaseMessageListPresenter messageListPresenter;
    private boolean showDeleteButton = false;

    public MessageViewAdapter(Topic parentTopic, List<Topic> topics,
                              ShowUrlCallback showUrlCallback,
                              @Nullable OnItemSelectedListener<Topic> relatedTopicSelectedListener,
                              BaseMessageListPresenter messageListPresenter) {
        this.parentTopic = parentTopic;
        // todo remove this, just for now it must be hidden
//        this.topics = topics;
        this.topics = null;
        this.showUrlCallback = showUrlCallback;
        this.relatedTopicSelectedListener = relatedTopicSelectedListener;
        this.messageListPresenter = messageListPresenter;
    }

    public MessageViewAdapter(Topic parentTopic, List<Topic> topics,
                              ShowUrlCallback showUrlCallback,
                              @Nullable OnItemSelectedListener<Topic> relatedTopicSelectedListener,
                              BaseMessageListPresenter messageListPresenter,
                              boolean showDeleteButton) {
        this.parentTopic = parentTopic;
        this.topics = null;
        this.showUrlCallback = showUrlCallback;
        this.relatedTopicSelectedListener = relatedTopicSelectedListener;
        this.messageListPresenter = messageListPresenter;
        this.showDeleteButton = showDeleteButton;
    }

    @Override
    public int getItemViewType(int position) {
        if (topics == null) {
            return 0;
        }

        if (position == messageListPresenter.getMessagesCount() - 2) {
            return 1; // another recyclerView
        } else {
            return 0; // TelegramMessage
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();

        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.template_message, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.template_list_row, parent, false);
            return new ListViewHolder(view);
        }

    }

    private String getSourceDate(TelegramMessage telegramMessage) {
        return String.format(context.getString(R.string.template_date_source), telegramMessage.getSource().getNames().get(0).getFa(), telegramMessage.getDateCreated());
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
        topicAdapter = new TopicViewAdapter(
                topics,
                relatedTopic -> relatedTopicSelectedListener.onSelect(relatedTopic),
                R.layout.template_topic_browse
        );
        ((ListViewHolder) holder).topics.setAdapter(topicAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true);
        ((ListViewHolder) holder).topics.setLayoutManager(layoutManager);
    }

    private void bindData(RecyclerView.ViewHolder holder, int position) {
        // todo remove
        messageListPresenter.onBindRowViewAtPosition(position, (MessageRowView) holder, showDeleteButton);
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
        return messageListPresenter.getMessagesCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements MessageRowView {

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
        @BindView(R.id.bookmark_button)
        TextView bookmarkButton;
        @BindView(R.id.like_button)
        TextView likeButton;
        @BindView(R.id.play_button)
        ImageView playButton;
        @BindView(R.id.like_count)
        TextView likeCount;
        @BindView(R.id.delete_button)
        TextView deleteButton;

        @Override
        public void removeBookmarkButton() {
            bookmarkButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void addBookmarkButton() {
            bookmarkButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void removeDeleteButton() {
            deleteButton.setVisibility(View.INVISIBLE);
        }

        @Override
        public void addDeleteButton() {
            deleteButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void setLikeCount(Integer likeCountInt) {
            likeCount.setText(String.format("+%d", Math.max(1, likeCountInt)));
        }

        @Override
        public void showLikeCount() {
            likeCount.setVisibility(View.VISIBLE);
        }

        @Override
        public void hideLikeCount() {
            likeCount.setVisibility(View.INVISIBLE);
            likeCount.setText("");
        }

        @Override
        public void showPlayButton() {
            playButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void hidePlayButton() {
            playButton.setVisibility(View.GONE);
        }

        @Override
        public void showBookmark() {
            bookmarkButton.setText(context.getString(R.string.icon_bookmark_full));
        }

        @Override
        public void hideBookmark() {
            bookmarkButton.setText(context.getString(R.string.icon_bookmark_empty));
        }

        @OnClick(R.id.delete_button)
        public void deleteOnClick() {
            messageListPresenter.deleteBookmark(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }

        @OnClick(R.id.play_button)
        public void playButtonOnClick() {
            messageListPresenter.playButtonOnClick(getAdapterPosition());
        }

        @OnClick(R.id.bookmark_button)
        public void bookmarkOnClick() {
            if (bookmarkButton.getText().toString().equals(context.getString(R.string.icon_bookmark_full))) {
                messageListPresenter.removeBookmark(getAdapterPosition());
                bookmarkButton.setText(context.getString(R.string.icon_bookmark_empty));
            } else {
                messageListPresenter.addBookmark(getAdapterPosition());
                bookmarkButton.setText(context.getString(R.string.icon_bookmark_full));
            }
        }

        @OnClick(R.id.like_button)
        public void likeOnClick() {
            if (likeButton.getText().toString().equals(context.getString(R.string.thumb_up_full))) {
                return;
            }

            messageListPresenter.likeMessage(getAdapterPosition());
//            notifyItemChanged(getAdapterPosition());
            likeButton.setText(context.getString(R.string.thumb_up_full));

            // todo
            showLikeCount();

            String likeStr = likeCount.getText().toString();
            if (likeStr == null || likeStr.isEmpty()) {
                likeStr = "+1";
            } else {
                likeStr = "+" + (Integer.parseInt(likeStr.substring(1)) + 1);
            }
            likeCount.setText(likeStr);
        }

        public ViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }

        @Override
        public void makePhotoVisible() {
            photo.setVisibility(View.VISIBLE);
        }

        @Override
        public void makePhotoInvisible() {
            photo.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
        }

        @Override
        public void showLikeIcon() {
            likeButton.setText(context.getString(R.string.thumb_up_full));
        }

        @Override
        public void hideLikeIcon() {
            likeButton.setText(context.getString(R.string.thumb_up_empty));
        }

        @Override
        public void setTitle(String title) {
            this.title.setText(title);
        }

        @Override
        public void setSourcePhoto(String sourcePhotoUrl) {
            try {
                sourcePhoto.setVisibility(View.VISIBLE);
                GlideApp.with(context)
                        .load(sourcePhotoUrl)
                        .circleCrop()
                        .into(sourcePhoto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setPhoto(int width, int height, String photoUrl) {
            try {
                photo.setVisibility(View.VISIBLE);
                GlideApp.with(context)
                        .load(photoUrl)
                        .placeholder(R.drawable.loading)
                        .override(width, height)
                        .into(photo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void setSource(String source) {
            this.source.setText(source);
        }

        @Override
        public void setDate(String date) {
            this.date.setText(date);
        }

        @Override
        public void setDescription(String description) {
            this.description.setText(description);

            Linkify.addLinks(this.description, Linkify.WEB_URLS);
            showLinks(this.description, description);
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

    public void updateRelatedTopics(List<Topic> topicList) {
        topics.clear();
        topics.addAll(topicList);
        topicAdapter.notifyDataSetChanged();
    }
}
