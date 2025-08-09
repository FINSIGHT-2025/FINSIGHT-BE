package com.finsight.backend.mapper;

import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.vo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;


@Mapper
public interface NewsMapper {
    void insertNews(NewsVO news);

    void insertNewsProduct(@Param("newsId") String newsId, @Param("productCode") String productCode, @Param("newsProductCategory") String newsProductCategory);

    void insertKeyword(@Param("keyword") String keyword);

    void insertNewsKeyword(@Param("newsId") String newsId, @Param("keywordId") Long keywordId);


    boolean existsNewsId(@Param("newsId") String newsId);

    boolean existsNewsTitle(@Param("newsTitle") String newsTitle);

    Long selectKeywordId(@Param("keyword") String keyword); // 키워드로 keyword_id 조회

    List<KeywordVO> findTopKeywords(); // 상위 10개 키워드 조회

    List<NewsVO> findNewsByKeywordId(Long keywordId); // keyword_id로 뉴스 목록 조회

    List<NewsVO> findNewsByProductCode(String productCode); // 상품 코드로 뉴스 목록 조회

    List<NewsProductVO> findProductInfoByKeywordId(Long keywordId);


//    임시 Fund mapper
    List<TempFundVO> selectAllFunds();

    List<TempEtfVO> selectAllEtfs();

    List<String> findNewsSentimentByProductCode(String productCode);

}
