package org.okky.reply.application;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.application.command.ModifyReplyCommand;
import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.okky.reply.domain.service.ReplyProxy;
import org.okky.share.event.ReplyRemoved;
import org.okky.share.event.ReplyWrote;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;

@Service
@Transactional
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplyService {
    ReplyRepository repository;
    ReplyConstraint constraint;
    ReplyProxy proxy;

    public void write(WriteReplyCommand cmd) {
        String articleId = cmd.getArticleId();

        constraint.checkArticleExists(articleId);
        constraint.rejectWriteIfArticleBlocked(articleId);
        Reply reply = new Reply(
                cmd.getArticleId(),
                cmd.getBody(),
                cmd.getReplierId(),
                cmd.getReplierName());
        repository.save(reply);
        proxy.sendEvent(new ReplyWrote(
                reply.getId(),
                reply.getArticleId(),
                reply.getBody(),
                reply.getReplierId(),
                reply.getReplierName(),
                reply.getRepliedOn(),
                reply.getUpdatedOn(),
                reply.getAcceptedOn()
        ));
    }

    @PreAuthorize("@replySecurityInspector.isThisWriter(#cmd.replyId)")
    public void modify(ModifyReplyCommand cmd) {
        Reply reply = constraint.checkExistsAndGet(cmd.getReplyId());
        reply.modify(cmd.getBody());
    }

    @PreAuthorize("@replySecurityInspector.isThisWriter(#replyId)")
    public void toggleAccept(String replyId) {
        Reply reply = constraint.checkExistsAndGet(replyId);
        reply.toggleAccept();
    }

    @PreAuthorize("@replySecurityInspector.isThisWriter(#replyId)")
    public void remove(String replyId) {
        Reply reply = constraint.checkExistsAndGet(replyId);
        repository.delete(reply);
        proxy.sendEvent(new ReplyRemoved(replyId));
    }

    public void removeForce(String replyId) {
        remove(replyId);
    }
}
