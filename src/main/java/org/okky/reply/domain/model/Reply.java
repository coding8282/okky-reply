package org.okky.reply.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.envers.Audited;
import org.okky.share.domain.Aggregate;
import org.okky.share.util.JsonUtil;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.share.domain.AssertionConcern.assertArgLength;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = PRIVATE)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "REPLY",
        indexes = {
                @Index(name = "I_ARTICLE_ID", columnList = "ARTICLE_ID"),
        }
)
public class Reply implements Aggregate {
    @Id
    @Column(length = 50)
    String id;

    @Column(name = "ARTICLE_ID", nullable = false, length = 50)
    String articleId;

    @Column(nullable = false, length = 500)
    @Audited
    String body;

    @Column(nullable = false)
    String replierId;

    @Column(nullable = false)
    String replierName;

    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    long repliedOn;

    @LastModifiedDate
    @Column(columnDefinition = "BIGINT UNSIGNED")
    Long updatedOn;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    Long acceptedOn;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    Long pinnedOn;

    public Reply(String articleId, String body, String replierId, String replierName) {
        setId("r-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setArticleId(articleId);
        setBody(body);
        setReplierId(replierId);
        setReplierName(replierName);
        setRepliedOn(currentTimeMillis());
        setPinnedOn(null);
    }

    public static Reply sample() {
        String articleId = "a03";
        String body = "답변 드립니다..";
        String replierId = "m-334455";
        String replierName = "coding8282";
        return new Reply(articleId, body, replierId, replierName);
    }

    public static void main(String[] args) {
        System.out.println(JsonUtil.toPrettyJson(sample()));
    }

    public void modify(String body) {
        setBody(body);
    }

    public void toggleAccept() {
        if (accepted())
            acceptedOn = null;
        else
            acceptedOn = currentTimeMillis();
    }

    public void pin() {
        setPinnedOn(currentTimeMillis());
    }

    public void unpin() {
        setPinnedOn(null);
    }

    public void togglePin() {
        if (pinned())
            unpin();
        else
            pin();
    }

    public boolean accepted() {
        return acceptedOn != null;
    }

    public boolean pinned() {
        return pinnedOn != null;
    }

    // ---------------------------------

    private void setId(String id) {
        assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setArticleId(String articleId) {
        assertArgNotNull(articleId, "게시글 id는 필수입니다.");
        this.articleId = articleId;
    }

    private void setBody(String body) {
        String trimed = body.trim();
        assertArgLength(trimed, 1, 500, format("답글은 %d~%d자까지 가능합니다.", 1, 500));
        this.body = trimed;
    }

    private void setReplierId(String replierId) {
        assertArgNotNull(replierId, "답변자 id는 필수입니다.");
        this.replierId = replierId;
    }

    private void setReplierName(String replierName) {
        assertArgNotNull(replierName, "답변자 이름은 필수입니다.");
        this.replierName = replierName;
    }

    private void setRepliedOn(long repliedOn) {
        this.repliedOn = repliedOn;
    }

    private void setPinnedOn(Long pinnedOn) {
        this.pinnedOn = pinnedOn;
    }
}
