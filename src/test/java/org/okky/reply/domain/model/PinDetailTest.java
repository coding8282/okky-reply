package org.okky.reply.domain.model;

import org.junit.Test;
import org.okky.reply.TestMother;
import org.okky.share.execption.BadArgument;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class PinDetailTest extends TestMother {
    @Test
    public void new_생성_시_고정된_날짜가_존재해야_함() {
        String memo = "칭찬 감사해요 ^_^";
        PinDetail detail = new PinDetail(memo);

        assertNotNull("생성 시에는 고정된 날짜가 존재해야 한다.", detail.getPinnedOn());
    }

    @Test
    public void new_메모는_null을_허용() {
        String memo = null;
        PinDetail detail = new PinDetail(memo);

        assertNull("메모는 null을 허용한다.", detail.getMemo());
    }

    @Test
    public void new_메모가_빈문자열이면_null로_저장() {
        String memo = "";
        PinDetail detail = new PinDetail(memo);

        assertNull("메모는 빈문자열로만 이루어져 있으면 null로 저장한다.", detail.getMemo());
    }

    @Test
    public void new_메모가_공백문자열만_있으면_null로_저장() {
        String memo = " \t ";
        PinDetail detail = new PinDetail(memo);

        assertNull("메모가 공백문자열로만 이루어져 있으면 null로 저장한다.", detail.getMemo());
    }

    @Test
    public void new_메모가_51자라면_예외() {
        expect(BadArgument.class, "탈퇴사유는 50자까지 가능합니다.");

        String memo = "안녕하세요1안녕하세요1안녕하세요1안녕하세요1안녕하세요1안녕하세요1안녕하세요1안녕하세요1234";
        new PinDetail(memo);
    }

    @Test
    public void new_메모가_50자_확인() {
        String memo = "안녕하세요 여러분들 꼭 읽어보세요 좋은 말씀 써놓으셔서 제가 댓글 고정하겠습니당~~~~~~";
        PinDetail detail = new PinDetail(memo);

        assertThat("정확히 50자여야 한다.", detail.getMemo(), is("안녕하세요 여러분들 꼭 읽어보세요 좋은 말씀 써놓으셔서 제가 댓글 고정하겠습니당~~~~~~"));
    }

    @Test
    public void new_메모가_trim이_되는지_확인() {
        String memo = "\n 말씀 감사드려요~~~ 고정할게요^_^ \t\t\n";
        PinDetail detail = new PinDetail(memo);

        assertThat("trim이 되어야 한다.", detail.getMemo(), is("말씀 감사드려요~~~ 고정할게요^_^"));
    }
}