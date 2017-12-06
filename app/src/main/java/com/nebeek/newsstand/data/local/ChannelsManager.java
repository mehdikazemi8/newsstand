package com.nebeek.newsstand.data.local;

import com.nebeek.newsstand.data.models.Channel;

import java.util.HashMap;
import java.util.List;

public class ChannelsManager {

    private static HashMap<String, Channel> channels;
    private static ChannelsManager instance = null;

    private ChannelsManager() {
        channels = new HashMap<>();
    }

    public static ChannelsManager getInstance() {
        if (instance == null) {
            instance = new ChannelsManager();
        }

        return instance;
    }

    public Channel getChannel(String id) {
        if (id == null || id.isEmpty()) {
            return null;
        }

        if (channels.containsKey(id)) {
            return channels.get(id);
        }
        return null;
    }

    public void addChannel(Channel channel) {
        channels.put(channel.getId(), channel);
    }

    public void addAll(List<Channel> channelList) {
        for (Channel channel : channelList) {
            channels.put(channel.getId(), channel);
        }
    }
}
