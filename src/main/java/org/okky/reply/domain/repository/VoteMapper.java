package org.okky.reply.domain.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.okky.reply.domain.repository.dto.VoteDto;
import org.okky.reply.domain.repository.dto.VoteStatisticsDto;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface VoteMapper {
    VoteDto selectVoteSummaryByReplyId(@Param("replyId") String replyId, @Param("myId") String myId);
    VoteStatisticsDto selectVoteStatisticsByReplierId(@Param("replierId") String replierId);
}
