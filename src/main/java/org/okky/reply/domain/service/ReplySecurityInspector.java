package org.okky.reply.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.resource.ContextHolder;
import org.springframework.stereotype.Service;

import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplySecurityInspector {
    ReplyConstraint constraint;
    ContextHolder holder;

    public boolean isThisWriter(String replyId) {
        Reply reply = constraint.checkExistsAndGet(replyId);
        String replierId = reply.getReplierId();
        String requesterId = holder.getId();
        return requesterId.equals(replierId);
    }
}
