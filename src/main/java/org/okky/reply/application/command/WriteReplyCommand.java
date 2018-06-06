package org.okky.reply.application.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class WriteReplyCommand {
    String articleId;
    String replierId;
    String replierName;
    String body;
}
