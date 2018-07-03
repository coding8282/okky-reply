package org.okky.reply.domain.repository.dto;

import lombok.Getter;
import org.okky.reply.domain.model.Reply;

@Getter
public class ReplyReducedDto {
    String id;
    String articleId;
    String replierId;
    String replierName;
    String body;
    String pinMemo;
    boolean accepted;
    boolean pinned;
    long repliedOn;
    Long updatedOn;
    Long acceptedOn;
    Long pinnedOn;

    public ReplyReducedDto(Reply r) {
        this.id = r.getId();
        this.articleId = r.getArticleId();
        this.replierId = r.getReplierId();
        this.replierName = r.getReplierName();
        this.body = r.getBody();
        this.accepted = r.accepted();
        this.pinned = r.pinned();
        this.repliedOn = r.getRepliedOn();
        this.updatedOn = r.getUpdatedOn();
        this.acceptedOn = r.getAcceptedOn();
        // TODO: 2018. 7. 1. 동적 인스턴스를 사용하면 usecase optimal query를 작성하기는 좋지만 JPA의 경우 vo가 null로 로딩되는 경우가 있다. 다루가기 까다로움
        if (r.pinned()) {
            this.pinMemo = r.getPinDetail().getMemo();
            this.pinnedOn = r.getPinDetail().getPinnedOn();
        }
    }
}
