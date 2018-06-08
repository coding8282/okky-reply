package org.okky.reply.application;

import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;
import org.okky.share.event.ReplyWrote;

public interface ModelMapper {
    static Reply toReply(WriteReplyCommand cmd) {
        return new Reply(
                cmd.getArticleId(),
                cmd.getBody(),
                cmd.getReplierId(),
                cmd.getReplierName());
    }

    static ReplyWrote toReplyWrote(Reply r) {
        return new ReplyWrote(
                r.getId(),
                r.getArticleId(),
                r.getBody(),
                r.getReplierId(),
                r.getReplierName(),
                r.getRepliedOn(),
                r.getUpdatedOn(),
                r.getAcceptedOn()
        );
    }
}
