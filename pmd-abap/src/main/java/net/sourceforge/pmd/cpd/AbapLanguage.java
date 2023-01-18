/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.cpd;

import java.util.regex.Pattern;

/**
 * @author oinume@gmail.com
 */
public class AbapLanguage extends AbstractLanguage {

    public AbapLanguage() {
        super("Abap", "abap", 
            new AnyTokenizer(
                Pattern.compile(
                    ".*|\n"
                ), ""
            ), ".abap"
        );
    }
}
