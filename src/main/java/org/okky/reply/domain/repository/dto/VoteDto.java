package org.okky.reply.domain.repository.dto;

import lombok.Getter;

@Getter
public class VoteDto {
    String replyId;
    Long upVoteCount;
    Long downVoteCount;
    Boolean upVotedByMe;
    Boolean downVotedByMe;
}
