package org.okky.reply.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ToggleVoteCommand {
    String replyId;
    String voterId;
    String direction;
}
