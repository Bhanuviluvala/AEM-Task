package com.mystack.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mystack.core.utils.MyStackUtils;

import lombok.Getter;

@Getter
@Model(adaptables = { Resource.class,
        SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardDetails {

    @Inject
    @Via("request")
    @Named("pagePath")
    private String pagePath;

    private String cardTitle;

    private String cardSubtitle;

    private String cardDescription;

    private String cardImage;

    private String cardCategory;

    private String cardType;

    private String cardStatus;

    @Inject
    private ResourceResolver resourceResolver;

    @PostConstruct
    protected void init() {
        if (resourceResolver != null && pagePath != null) {
            PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            if (pageManager != null) {
                Page page = pageManager.getPage(pagePath);
                if (page != null) {
                    ValueMap properties = page.getProperties();
                    this.cardTitle = properties.get("cardTitle", "");
                    this.cardSubtitle = properties.get("cardSubtitle", "");
                    this.cardDescription = properties.get("cardDescription", "");
                    this.cardImage = properties.get("cardImage", "");
                    this.cardCategory = MyStackUtils.getTagTitleByPath(properties.get("cardCategory", ""),
                            resourceResolver);
                    this.cardType = properties.get("cardType", "");
                    this.cardStatus = properties.get("cardStatus", "");

                }
            }

        }

    }
}
