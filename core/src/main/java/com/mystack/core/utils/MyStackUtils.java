package com.mystack.core.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.service.component.annotations.Component;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

@Component
public class MyStackUtils {

    public static String getTagTitleByPath(String tagPath, ResourceResolver resourceResolver) {
        if (tagPath != null && !tagPath.isEmpty()) {
            TagManager manager = resourceResolver.adaptTo(TagManager.class);
            Tag tagTitle = manager.resolve(tagPath);
            return tagTitle.getTitle();
        }
        return "";
    }

    public static Map<String, String> getTagTitles(String[] tagpaths, ResourceResolver resolver) {
        Map<String, String> tagTitles = new LinkedHashMap<String, String>();
        tagTitles.put("select", "Select");
        for (String tagpath : tagpaths) {
            TagManager manager = resolver.adaptTo(TagManager.class);
            Tag tagTitle = manager.resolve(tagpath);
            tagTitles.put(tagTitle.getTagID().split("/")[1], getTagTitleByPath(tagpath, resolver));
        }
        return tagTitles;
    }

    public static String getTagId(String tagId, ResourceResolver resolver) {
        TagManager manager = resolver.adaptTo(TagManager.class);
        Tag tagTitle = manager.resolve(tagId);
        return tagTitle.getTagID().split("/")[1];
    }

}
