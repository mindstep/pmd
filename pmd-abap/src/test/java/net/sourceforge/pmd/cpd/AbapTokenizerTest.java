/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.cpd;

import java.util.Properties;

import org.junit.Test;

import net.sourceforge.pmd.cpd.test.CpdTextComparisonTest;

public class AbapTokenizerTest extends CpdTextComparisonTest {

    public AbapTokenizerTest() {
        super(".abap");
    }

    @Override
    public Tokenizer newTokenizer(Properties properties) {
        return new AnyTokenizer();
    }

    @Override
    protected String getResourcePrefix() {
        return "../lang/abap/cpd/testdata";
    }

    @Test
    public void simpleTest() {
        doTest("hello");
    }

}
