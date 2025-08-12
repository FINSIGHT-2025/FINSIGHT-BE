package com.finsight.backend.domain.enumerate.handler;


import com.finsight.backend.domain.enumerate.NewsSentiment;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(NewsSentiment.class)
public class NewsSentimentHandler extends GenericEnumTypeHandler<NewsSentiment>{
    public NewsSentimentHandler(){
        super(NewsSentiment.class);
    }
}