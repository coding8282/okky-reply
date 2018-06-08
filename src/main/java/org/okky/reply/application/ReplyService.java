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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static lombok.AccessLevel.PRIVATE;
import static org.okky.reply.application.ModelMapper.toReply;
import static org.okky.reply.application.ModelMapper.toReplyWrote;

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
        Reply reply = toReply(cmd);
        repository.save(reply);
        proxy.sendMessage(toReplyWrote(reply));
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
        proxy.sendMessage(new ReplyRemoved(replyId));
    }

    public void removeForce(String replyId) {
        remove(replyId);
    }
}
