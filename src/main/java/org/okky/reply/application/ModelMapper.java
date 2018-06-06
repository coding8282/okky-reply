package org.okky.reply.application;

import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;

public interface ModelMapper {
    static Reply toReply(WriteReplyCommand cmd) {
        return new Reply(
                cmd.getArticleId(),
                cmd.getBody(),
                cmd.getReplierId(),
                cmd.getReplierName());
    }
}
