package org.okky.reply.domain.repository.dto;

import lombok.Getter;

@Getter
public class VoteDto {
    String repyId;
    Long upVoteCount;
    Long downVoteCount;
    Boolean upVotedByMe;
    Boolean downVotedByMe;
}
