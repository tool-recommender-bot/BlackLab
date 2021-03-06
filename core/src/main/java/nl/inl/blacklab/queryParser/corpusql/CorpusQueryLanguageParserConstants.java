/* Generated By:JavaCC: Do not edit this line. CorpusQueryLanguageParserConstants.java */
package nl.inl.blacklab.queryParser.corpusql;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface CorpusQueryLanguageParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 5;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 6;
  /** RegularExpression Id. */
  int WITHIN = 7;
  /** RegularExpression Id. */
  int CONTAINING = 8;
  /** RegularExpression Id. */
  int NAME = 9;
  /** RegularExpression Id. */
  int FLAGS = 10;
  /** RegularExpression Id. */
  int NUMBER = 11;
  /** RegularExpression Id. */
  int QUOTED_STRING = 12;
  /** RegularExpression Id. */
  int SINGLE_QUOTED_STRING = 13;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<SINGLE_LINE_COMMENT>",
    "<MULTI_LINE_COMMENT>",
    "\"within\"",
    "\"containing\"",
    "<NAME>",
    "<FLAGS>",
    "<NUMBER>",
    "<QUOTED_STRING>",
    "<SINGLE_QUOTED_STRING>",
    "\"::\"",
    "\"=\"",
    "\"!=\"",
    "\"(\"",
    "\")\"",
    "\"!\"",
    "\".\"",
    "\"<\"",
    "\"/\"",
    "\">\"",
    "\"*\"",
    "\"+\"",
    "\"?\"",
    "\"{\"",
    "\"}\"",
    "\",\"",
    "\"&\"",
    "\"|\"",
    "\"->\"",
    "\":\"",
    "\"[\"",
    "\"]\"",
  };

}
