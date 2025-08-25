package com.mystack.core.models;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.RepositoryException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.mystack.core.capsules.Card;
import com.mystack.core.services.CardQueryService;
import com.mystack.core.utils.MyStackUtils;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;

import lombok.Getter;

@Model(adaptables = { Resource.class,
        SlingHttpServletRequest.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CardListingModel {

    @Getter
    @ValueMapValue
    private String filterTitle;

    @ValueMapValue
    private String cardRootPath;

    @Getter
    @ValueMapValue
    private String sortTitle;

    @ValueMapValue
    private String[] cardsortTag;

    @Getter
    @ValueMapValue
    private String categoryTitle;

    @ValueMapValue
    private String[] categoryTag;

    @Getter
    @ValueMapValue
    private String noresultsText;

    @Getter
    @ValueMapValue
    private String errorText;

    @Getter
    private List<Card> cards;

    @Getter
    private Map<String, String> sortDropDownList;

    @Getter
    private Map<String, String> categoryDropDownList;

    @OSGiService
    private CardQueryService cardQueryService;

    @Inject
    private ResourceResolver resolver;

    @PostConstruct
    protected void init() {

        try {
            if (cardRootPath != null && !cardRootPath.isEmpty())
                cards = cardQueryService.getAllExistingCards(cardRootPath, resolver);
            if (categoryTag != null) {
                categoryDropDownList = MyStackUtils.getTagTitles(categoryTag, resolver);
            }
            if (cardsortTag != null) {
                sortDropDownList = MyStackUtils.getTagTitles(cardsortTag, resolver);
            }
        } catch (RepositoryException e) {
            e.printStackTrace();
        }
    }

}
