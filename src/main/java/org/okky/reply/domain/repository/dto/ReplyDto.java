package org.okky.reply.domain.repository.dto;

import lombok.Getter;

@Getter
public class ReplyDto {
    String id;
    String articleId;
    String replierId;
    String replierName;
    String body;
    Boolean accepted;
    Long upVoteCount;
    Long downVoteCount;
    Long repliedOn;
    Long updatedOn;
    Long acceptedOn;
    Boolean repliedByMe;
    Boolean upVotedByMe;
    Boolean downVotedByMe;
}
