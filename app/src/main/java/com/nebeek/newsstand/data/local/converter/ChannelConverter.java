package com.nebeek.newsstand.data.local.converter;

import android.arch.persistence.room.TypeConverter;

import com.nebeek.newsstand.data.models.AppImage;
import com.nebeek.newsstand.data.models.AppImageSet;
import com.nebeek.newsstand.data.models.AppText;
import com.nebeek.newsstand.data.models.Channel;

public class ChannelConverter {

    @TypeConverter
    public static String toString(Channel channel) {
        if (channel == null) {
            return null;
        }

        return channel.getNames().get(0).getFa() + "###" +
                channel.getId() + "###" +
                channel.getLikes() + "###" +
                (channel.getImageSets() == null ? null :
                        channel.getImageSets().get(0).getImages() == null ? null :
                                channel.getImageSets().get(0).getImages().get(0).getData());
    }

    @TypeConverter
    public static Channel toChannel(String str) {
        if (str == null) {
            return null;
        }

        Channel channel = new Channel();
        String[] tokens = str.split("###");

        channel.getNames().add(new AppText(tokens[0]));
        channel.setId(tokens[1]);
        channel.setLikes(tokens[2]);
        channel.getImageSets().add(new AppImageSet());
        channel.getImageSets().get(0).getImages().add(new AppImage(tokens[3]));
        return channel;
    }
}
