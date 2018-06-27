package org.okky.reply.domain.service;

import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.okky.share.execption.ExternalServiceError;
import org.okky.share.execption.ModelNotExists;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

@Service
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
public class ReplyVoteConstraint {
    RestTemplate template;

    public void checkVoterExists(String voterId) {
        try {
            Boolean existsVoter = template.getForEntity(format("/members/%s/exists", voterId), Boolean.class).getBody();
            if (!existsVoter)
                throw new ModelNotExists(format("해당 회원은 존재하지 않으므로 추천/비추천할 수 없습니다: '%s'", voterId));
        } catch (HttpStatusCodeException e) {
            throw new ExternalServiceError(e.getResponseBodyAsByteArray());
        }
    }
}
