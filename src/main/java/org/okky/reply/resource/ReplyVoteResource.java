package org.okky.reply.resource;

import lombok.AllArgsConstructor;
import org.okky.reply.application.ReplyVoteService;
import org.okky.reply.application.command.ToggleVoteCommand;
import org.okky.reply.domain.repository.VoteMapper;
import org.okky.reply.domain.repository.dto.VoteDto;
import org.okky.reply.domain.repository.dto.VoteStatisticsDto;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@AllArgsConstructor
class ReplyVoteResource {
    ReplyVoteService service;
    VoteMapper mapper;

    @PutMapping(value = "/replies/{replyId}/votes/toggle", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    VoteDto toggleVote(
            @PathVariable String replyId,
            @RequestBody ToggleVoteCommand cmd) {
        cmd.setReplyId(replyId);
        cmd.setVoterId(ContextHelper.getId());
        service.toggleVote(cmd);
        return mapper.selectVoteSummaryByReplyId(replyId, ContextHelper.getId());
    }

    @GetMapping(value = "/members/{memberId}/votes/count", produces = APPLICATION_JSON_VALUE)
    VoteStatisticsDto count(@PathVariable String memberId) {
        return mapper.selectVoteStatisticsByReplierId(memberId);
    }
}
