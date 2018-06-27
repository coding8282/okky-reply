package org.okky.reply.domain.repository.dto;

import lombok.Getter;

@Getter
public class ReplyDto {
    String id;
    String articleId;
    String replierId;
    String replierName;
    String body;
    String pinMemo;
    boolean accepted;
    boolean pinned;
    long upVoteCount;
    long downVoteCount;
    long repliedOn;
    long updatedOn;
    long acceptedOn;
    long pinnedOn;
    boolean repliedByMe;
    boolean upVotedByMe;
    boolean downVotedByMe;
}
