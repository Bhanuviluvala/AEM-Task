package com.mystack.core.serviceimplementation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mystack.core.capsules.Card;
import com.mystack.core.services.CardQueryService;
import com.mystack.core.utils.MyStackUtils;

@Component(service = CardQueryService.class)
public class CardQueryServiceImpl implements CardQueryService {

    @Override
    public List<Card> getAllExistingCards(String rootPath, ResourceResolver resolver) {

        QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
        Session session = resolver.adaptTo(Session.class);

        List<Card> cards = new ArrayList<>();

        Map<String, String> map = new HashMap<String, String>();

        map.put("path", rootPath);
        map.put("path.flat", "true");
        map.put("type", "cq:Page");
        map.put("orderby", "@jcr:lastModified");
        map.put("orderby.sort", "desc");

        Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
        SearchResult result = query.getResult();

        for (Hit hit : result.getHits()) {
            String path;
            try {
                path = hit.getPath();
                PageManager pageManager = resolver.adaptTo(PageManager.class);
                Page page = pageManager.getPage(path);
                if (page != null) {
                    ValueMap properties = page.getProperties();
                    String cardTitle = properties.get("cardTitle", "");
                    String cardSubtitle = properties.get("cardSubtitle", "");
                    String cardDescription = properties.get("cardDescription", "");
                    String cardImage = properties.get("cardImage", "");
                    String cardCategory = MyStackUtils.getTagTitleByPath(properties.get("cardCategory", ""),
                            resolver);
                    String cardCategoryKey = MyStackUtils.getTagId(properties.get("cardCategory", ""),
                            resolver);
                    String cardType = properties.get("cardType", "");
                    String cardStatus = properties.get("cardStatus", "");
                    cards.add(new Card(cardTitle, cardSubtitle, cardDescription, cardCategory, cardCategoryKey,
                            cardImage, cardType,
                            cardStatus));
                }
            } catch (RepositoryException e) {
                e.printStackTrace();
            }
        }

        return cards;
    }

}
