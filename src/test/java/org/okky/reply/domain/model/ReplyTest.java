package org.okky.reply.domain.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.*;

public class ReplyTest {
    @Test
    public void new_최초에는_pin_상태가_아님() {
        Reply reply = fixture();

        assertFalse("최초 생성시에는 답글이 고정된 상태가 아니다.", reply.pinned());
    }

    @Test
    public void new_최초에는_pin_상태가_아니므로_해당_날짜도_없음() {
        Reply reply = fixture();

        assertNull("최초 생성시에는 답글이 고정된 상태가 아니므로 해당 날짜는 null이어야 한다.", reply.getPinnedOn());
    }

    @Test
    public void togglePin_pin_후에는_해당_날짜가_존재하는_것을_확인() {
        Reply reply = fixture();
        reply.togglePin();

        assertTrue("한 번 토글 후에는 상태가 true여야 한다.", reply.pinned());
        assertThat("pin 후에는 답글이 고정된 날짜가 존재해야 한다.", reply.getPinnedOn(), is(notNullValue()));
    }

    @Test
    public void pin_토글_확인() {
        Reply reply = fixture();
        assertFalse("최초에는 답글이 고정된 상태가 아니므로 false여야 한다.", reply.pinned());
        assertNull("최초에는 답글이 고정된 상태가 아니므로 날짜는 null이어야 한다.", reply.getPinnedOn());

        reply.togglePin();
        assertTrue("한 번 토글 후에는 true여야 한다.", reply.pinned());
        assertNotNull("한 번 토글 후에는 날짜는 null이 아니어야 한다.", reply.getPinnedOn());

        reply.togglePin();
        assertFalse("다시 토글 후에는 false여야 한다.", reply.pinned());
        assertNull("다시 토글 후에는 날짜는 null이어야 한다.", reply.getPinnedOn());
    }

    @Test
    public void choice_토글_확인() {
        Reply reply = fixture();
        assertFalse(reply.accepted());

        reply.toggleAccept();
        assertTrue(reply.accepted());

        reply.toggleAccept();
        assertFalse(reply.accepted());
    }

    @Test
    public void 수정_확인() {
        Reply reply = fixture();
        reply.modify("답변 드립니다2222");

        assertThat(reply.getBody(), is("답변 드립니다2222"));
    }

    @Test
    public void 채택_전에는_날짜가_null이고_후에는_날짜_존재_확인() {
        Reply reply = fixture();
        assertNull(reply.getAcceptedOn());

        reply.toggleAccept();
        assertNotNull(reply.getAcceptedOn());
    }

    // ----------------------------
    private Reply fixture() {
        String id = "a1";
        String articleId = "a03";
        String body = "답변 드립니다..";
        String replierId = "m-334455";
        String replierName = "coding8282";
        return new Reply(articleId, body, replierId, replierName);
    }
}