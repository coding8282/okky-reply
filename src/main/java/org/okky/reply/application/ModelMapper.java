package org.okky.reply.application;

import org.okky.reply.application.command.WriteReplyCommand;
import org.okky.reply.domain.model.Reply;
import org.okky.share.event.ReplyWrote;
import org.springframework.stereotype.Service;

@Service
class ModelMapper {
    ReplyWrote toEvent(Reply reply) {
        return new ReplyWrote(
                reply.getId(),
                reply.getArticleId(),
                reply.getBody(),
                reply.getReplierId(),
                reply.getReplierName(),
                reply.getRepliedOn(),
                reply.getUpdatedOn(),
                reply.getAcceptedOn()
        );
    }

    Reply toModel(WriteReplyCommand cmd) {
        return new Reply(
                cmd.getArticleId(),
                cmd.getBody(),
                cmd.getReplierId(),
                cmd.getReplierName());
    }
}
