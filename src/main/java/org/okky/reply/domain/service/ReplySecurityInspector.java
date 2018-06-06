package org.okky.reply.domain.service;

import lombok.AllArgsConstructor;
import org.okky.reply.domain.model.Reply;
import org.okky.reply.resource.ContextHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ReplySecurityInspector {
    private ReplyConstraint constraint;

    public boolean isThisWriter(String replyId) {
        Reply reply = constraint.checkExistsAndGet(replyId);
        String replierId = reply.getReplierId();
        String requesterId = ContextHelper.getId();
        return requesterId.equals(replierId);
    }
}
