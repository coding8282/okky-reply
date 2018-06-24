package org.okky.reply.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.okky.share.domain.ValueObject;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;
import static org.okky.share.domain.AssertionConcern.assertArgLength;
import static org.okky.share.util.JsonUtil.toPrettyJson;

@NoArgsConstructor(access = PROTECTED)
@EqualsAndHashCode
@FieldDefaults(level = PRIVATE)
@Getter
@Embeddable
public class PinDetail implements ValueObject {
    @Column(name = "PIN_MEMO", length = 50)
    String memo;

    @Column(columnDefinition = "BIGINT UNSIGNED")
    long pinnedOn;

    public PinDetail(String memo) {
        setMemo(memo);
        setPinnedOn(currentTimeMillis());
    }

    public static PinDetail sample() {
        String memo = "칭찬 감사해요 ^_^";
        return new PinDetail(memo);
    }

    public static void main(String[] args) {
        System.out.println(toPrettyJson(sample()));
    }

    // ---------------------------------
    private void setMemo(String memo) {
        if (StringUtils.isBlank(memo)) {
            this.memo = null;
        } else {
            String trimed = memo.trim();
            assertArgLength(trimed, 50, format("탈퇴사유는 %d자까지 가능합니다.", 50));
            this.memo = trimed;
        }
    }

    private void setPinnedOn(long pinnedOn) {
        this.pinnedOn = pinnedOn;
    }
}
