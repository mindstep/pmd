/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.cpd;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.LanguageRegistry;
import net.sourceforge.pmd.lang.LanguageVersion;
import net.sourceforge.pmd.lang.scala.ScalaLanguageHandler;
import net.sourceforge.pmd.lang.scala.ScalaLanguageModule;

import scala.collection.Iterator;
import scala.meta.Dialect;
import scala.meta.inputs.Input;
import scala.meta.internal.tokenizers.ScalametaTokenizer;
import scala.meta.tokens.Token;

/**
 * Scala Tokenizer class. Uses the Scala Meta Tokenizer.
 */
public class ScalaTokenizer implements Tokenizer {

    /**
     * Denotes the version of the scala dialect to use. Based on the values in
     * {@linkplain ScalaLanguageModule#getVersions()}
     */
    public static final String SCALA_VERSION_PROPERTY = "scala_version";
    private final Dialect dialect;

    /**
     * Create the Tokenizer using properties from the system environment.
     */
    public ScalaTokenizer() {
        this(System.getProperties());
    }

    /**
     * Create the Tokenizer given a set of properties.
     * 
     * @param properties
     *            the {@linkplain Properties} object to use
     */
    public ScalaTokenizer(Properties properties) {
        String scalaVersion = properties.getProperty(SCALA_VERSION_PROPERTY);
        LanguageVersion langVer;
        if (scalaVersion == null) {
            langVer = LanguageRegistry.getLanguage(ScalaLanguageModule.NAME).getDefaultVersion();
        } else {
            langVer = LanguageRegistry.getLanguage(ScalaLanguageModule.NAME).getVersion(scalaVersion);
        }
        dialect = ((ScalaLanguageHandler) langVer.getLanguageVersionHandler()).getDialect();
    }

    @Override
    public void tokenize(SourceCode sourceCode, Tokens tokenEntries) throws IOException {
        String filename = sourceCode.getFileName();
        // create the full code file
        String fullCode = StringUtils.join(sourceCode.getCode(), "\n");

        // create the input file for scala
        Input.VirtualFile vf = new Input.VirtualFile(filename, fullCode);
        ScalametaTokenizer tokenizer = new ScalametaTokenizer(vf, dialect);

        // tokenize with a filter
        scala.meta.tokens.Tokens tokens = tokenizer.tokenize();
        ScalaTokenFilter filter = new ScalaTokenFilter(tokens.iterator());

        Token token;
        while ((token = filter.getNextToken()) != null) {
            String tokenText = token.text() != null ? token.text() : token.name();
            TokenEntry cpdToken = new TokenEntry(tokenText, filename, token.pos().startLine());
            tokenEntries.add(cpdToken);
        }
    }

    /**
     * Token Filter skips un-helpful tokens to only register important tokens
     * and patterns.
     */
    private static class ScalaTokenFilter {
        Iterator<Token> tokenIter;

        ScalaTokenFilter(Iterator<Token> iterator) {
            this.tokenIter = iterator.iterator();
        }

        Token getNextToken() {
            if (!tokenIter.hasNext()) {
                return null;
            }

            Token token;
            do {
                token = tokenIter.next();
            } while (token != null && skipToken(token) && tokenIter.hasNext());

            return token;
        }

        private boolean skipToken(Token token) {
            boolean skip = false;
            if (token.text() != null) {
                // skip any token that is whitespaces
                skip |= token instanceof Token.Space || token instanceof Token.Tab
                        || token instanceof Token.CR || token instanceof Token.LF
                        || token instanceof Token.FF || token instanceof Token.LFLF;
            }
            return skip;
        }
    }
}
