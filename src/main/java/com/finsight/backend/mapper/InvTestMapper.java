//2025-07-30 JY
package com.finsight.backend.mapper;

import com.finsight.backend.vo.InvTestVO;
import org.apache.ibatis.annotations.Mapper;


@Mapper // MyBatis Mapper 인터페이스임을 선언함.
public interface InvTestMapper {
    void insert(InvTestVO invTestVO);
}
