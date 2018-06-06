package org.okky.reply.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.okky.share.domain.Aggregate;
import org.okky.share.domain.AssertionConcern;
import org.okky.share.util.JsonUtil;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static lombok.AccessLevel.PROTECTED;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
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
    private String id;

    @Column(name = "ARTICLE_ID", nullable = false, length = 50)
    private String articleId;

    @Column(nullable = false, length = 500)
    @Audited
    private String body;

    @Column(nullable = false)
    private String replierId;

    @Column(nullable = false)
    private String replierName;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private long repliedOn;

    @LastModifiedDate
    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long updatedOn;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    private Long acceptedOn;

    public Reply(String articleId, String body, String replierId, String replierName) {
        setId("r-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setArticleId(articleId);
        setBody(body);
        setReplierId(replierId);
        setReplierName(replierName);
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

    public boolean accepted() {
        return acceptedOn != null;
    }

    // ---------------------------------

    private void setId(String id) {
        AssertionConcern.assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setArticleId(String articleId) {
        AssertionConcern.assertArgNotNull(articleId, "게시글 id는 필수입니다.");
        this.articleId = articleId;
    }

    private void setBody(String body) {
        String trimed = body.trim();
        AssertionConcern.assertArgLength(trimed, 1, 500, format("답글은 %d~%d자까지 가능합니다.", 1, 500));
        this.body = trimed;
    }

    private void setReplierId(String replierId) {
        AssertionConcern.assertArgNotNull(replierId, "답변자 id는 필수입니다.");
        this.replierId = replierId;
    }

    private void setReplierName(String replierName) {
        AssertionConcern.assertArgNotNull(replierName, "답변자 이름은 필수입니다.");
        this.replierName = replierName;
    }
}
