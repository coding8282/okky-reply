package org.okky.reply.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.domain.Aggregate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.UUID;

import static javax.persistence.EnumType.STRING;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.reply.domain.model.Voting.DOWN;
import static org.okky.reply.domain.model.Voting.UP;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
@FieldDefaults(level = PRIVATE)
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "U_REPLY_ID_VOTER_ID",
                columnNames = {"REPLY_ID", "VOTER_ID"})
})
public class ReplyVote implements Aggregate {
    @Id
    @Column(length = 50)
    String id;

    @Column(name = "REPLY_ID", nullable = false, length = 50)
    String replyId;

    @Column(name = "VOTER_ID", nullable = false)
    String voterId;

    @Enumerated(STRING)
    @Column(nullable = false, columnDefinition = "CHAR(5)")
    Voting voting;

    @CreatedDate
    @Column(nullable = false, updatable = false, columnDefinition = "BIGINT UNSIGNED")
    long votedOn;

    public ReplyVote(String replyId, String voterId, Voting voting) {
        setId("rv-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 15));
        setReplyId(replyId);
        setVoterId(voterId);
        setVoting(voting);
    }

    public static ReplyVote sample() {
        String replyId = "r-a33";
        String voterId = "m-aa333a33";
        Voting voting = UP;
        return new ReplyVote(replyId, voterId, voting);
    }

    public static void main(String[] args) {
        System.out.println(sample());
    }

    public void reverseDirection() {
        if (voting == UP)
            setVoting(DOWN);
        else
            setVoting(UP);
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
