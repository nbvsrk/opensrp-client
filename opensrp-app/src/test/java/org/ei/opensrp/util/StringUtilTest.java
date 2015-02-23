package org.ei.opensrp.util;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilTest {

    @Test
    public void shouldCapitalize() throws Exception {
        Assert.assertEquals("Abc", StringUtil.humanize("abc"));
        Assert.assertEquals("Abc", StringUtil.humanize("Abc"));
    }

    @Test
    public void shouldReplaceUnderscoreWithSpace() throws Exception {
        Assert.assertEquals("Abc def", StringUtil.humanize("abc_def"));
        Assert.assertEquals("Abc def", StringUtil.humanize("Abc_def"));
    }

    @Test
    public void shouldHandleEmptyAndNull() throws Exception {
        Assert.assertEquals("", StringUtil.humanize(""));
        Assert.assertEquals(null, StringUtil.humanize(null));
    }
}
