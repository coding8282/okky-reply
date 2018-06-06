package org.okky.reply.application;

import lombok.AllArgsConstructor;
import org.okky.reply.application.command.ModifyReplyCommand;
import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.domain.repository.ReplyRepository;
import org.okky.reply.domain.service.ReplyConstraint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class ReplyService {
    private ReplyRepository repository;
    private ReplyConstraint constraint;

    public void write(WriteReplyCommand cmd) {
        String articleId = cmd.getArticleId();

        constraint.checkArticleExists(articleId);
        constraint.rejectWriteIfArticleBlocked(articleId);
        Reply reply = ModelMapper.toReply(cmd);
        repository.save(reply);
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
    }

    public void removeForce(String replyId) {
        remove(replyId);
    }
}
