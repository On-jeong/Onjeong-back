package com.example.onjeong.util;

import com.example.onjeong.profile.domain.Expression;
import com.example.onjeong.profile.domain.Profile;
import org.apache.commons.lang3.RandomStringUtils;


public class ExpressionUtils {

    public static Expression getRandomExpression(Profile profile){
        final Long expressionId= 50L;
        final String expressionContent= RandomStringUtils.random(8, true, true);
        return getExpression(expressionId, expressionContent, profile);
    }


    public static Expression getExpression(Long expressionId, String expressionContent, Profile profile){
        return Expression.builder()
                .expressionId(expressionId)
                .expressionContent(expressionContent)
                .profile(profile)
                .build();
    }
}