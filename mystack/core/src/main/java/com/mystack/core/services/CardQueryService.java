package com.mystack.core.services;

import java.util.List;

import javax.jcr.RepositoryException;

import org.apache.sling.api.resource.ResourceResolver;

import com.mystack.core.capsules.Card;

public interface CardQueryService {

    public List<Card> getAllExistingCards(String rootPath, ResourceResolver resolver) throws RepositoryException;

}
