package com.mystack.core.capsules;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    private String cardTitle;

    private String cardSubtitle;

    private String cardDescription;

    private String cardCategory;

    private String cardCategoryKey;

    private String cardImage;

    private String cardType;

    private String cardStaus;

}
