/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.abap;

import net.sourceforge.pmd.lang.BaseLanguageModule;

/**
 * Language Module for Abap language.
 */
public class AbapLanguageModule extends BaseLanguageModule {

    /** The name. */
    public static final String NAME = "Abap";
    /** The terse name. */
    public static final String TERSE_NAME = "abap";

    /**
     * Create a new instance of Abap Language Module.
     */
    public AbapLanguageModule() {
        super(NAME, null, TERSE_NAME, "abap");
        addVersion("1", null, true);
    }
}
