package org.okky.reply.domain.model;

import org.okky.share.execption.BadArgument;

import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.okky.share.domain.AssertionConcern.assertArgNotNull;

public enum VotingDirection {
    UP,
    DOWN,;

    public static VotingDirection parse(String value) {
        try {
            assertArgNotNull(value, "투표 방향은 필수입니다.");
            return valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            String possibleValues = String.join(",", Arrays
                    .stream(values())
                    .map(VotingDirection::name)
                    .collect(Collectors.toList()));
            throw new BadArgument(format("'%s는 지원하지 않는 투표 방향입니다. %s만 가능합니다.'", value, possibleValues));
        }
    }
}
