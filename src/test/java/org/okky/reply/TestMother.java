package org.okky.reply;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class TestMother {
    @Rule
    public ExpectedException expectedExcetption = ExpectedException.none();

    protected void expect(Class<? extends Exception> ex) {
        expectedExcetption.expect(ex);
    }

    protected void expect(Class<? extends Exception> ex, String message) {
        expectedExcetption.expect(ex);
        expectedExcetption.expectMessage(message);
    }
}
