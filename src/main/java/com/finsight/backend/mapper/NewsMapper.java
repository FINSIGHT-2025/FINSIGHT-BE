package com.finsight.backend.mapper;

import com.finsight.backend.dto.response.KeywordResponseDTO;
import com.finsight.backend.vo.NewsVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface NewsMapper {
    void insertNews(NewsVO news);

    void insertNewsProduct(@Param("newsId") String newsId, @Param("productCode") String productCode);

    void insertKeyword(@Param("keyword") String keyword);

    void insertNewsKeyword(@Param("newsId") String newsId, @Param("keywordId") Long keywordId);


    Boolean existsNewsId(@Param("newsId") String newsId);

    Long selectKeywordId(@Param("keyword") String keyword); // 키워드로 keyword_id 조회

    List<KeywordResponseDTO> findTopKeywords(); // 상위 10개 키워드 조회

    List<NewsVO> findNewsByKeywordId(Long keywordId); // keyword_id로 뉴스 목록 조회

    List<NewsVO> findNewsByProductCode(String productCode); // 상품 코드로 뉴스 목록 조회


}
