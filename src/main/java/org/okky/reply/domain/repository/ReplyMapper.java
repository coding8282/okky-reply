package org.okky.reply.domain.repository;

import org.apache.ibatis.annotations.Mapper;
import org.okky.reply.domain.repository.dto.ReplyDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface ReplyMapper {
    List<ReplyDto> select(Map<String, Object> params);
    long count(Map<String, Object> params);
}
