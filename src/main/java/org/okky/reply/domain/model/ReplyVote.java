package org.okky.reply.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.okky.share.domain.Aggregate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class ReplyVote implements Aggregate {
    @Id
    @Column(length = 50)
    private String id;

    @Column(nullable = false, length = 50)
    private String replyId;

    @Column(nullable = false)
    private String voterId;

    @Enumerated(STRING)
    @Column(nullable = false, columnDefinition = "CHAR(5)")
    private Voting voting;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    private long votedOn;

    public ReplyVote(String replyId, String voterId, Voting voting) {
        setId("rv-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setReplyId(replyId);
        setVoterId(voterId);
        setVoting(voting);
    }

    public static ReplyVote sample() {
        String replyId = "r-a33";
        String voterId = "m-aa333a33";
        Voting voting = Voting.UP;
        return new ReplyVote(replyId, voterId, voting);
    }

    public static void main(String[] args) {
        System.out.println(sample());
    }

    public boolean isSameDirection(Voting voting) {
        return this.voting == voting;
    }

    // ------------------------
    private void setId(String id) {
        assertArgNotNull(id, "id는 필수입니다.");
        this.id = id;
    }

    private void setReplyId(String replyId) {
        assertArgNotNull(replyId, "답글 id는 필수입니다.");
        this.replyId = replyId;
    }

    private void setVoterId(String voterId) {
        assertArgNotNull(voterId, "투표자는 필수입니다.");
        this.voterId = voterId;
    }

    private void setVoting(Voting voting) {
        assertArgNotNull(voting, "투표는 필수입니다.");
        this.voting = voting;
    }
}
