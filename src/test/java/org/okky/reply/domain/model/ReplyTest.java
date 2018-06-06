package org.okky.reply.domain.model;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class ReplyTest {
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