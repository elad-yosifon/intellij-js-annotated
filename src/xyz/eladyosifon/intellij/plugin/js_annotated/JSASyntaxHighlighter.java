package xyz.eladyosifon.intellij.plugin.js_annotated;

import com.intellij.lang.Language;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase;
import com.intellij.openapi.fileTypes.SyntaxHighlighterFactory;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NotNull;

public class JSASyntaxHighlighter extends SyntaxHighlighterBase {

  public static final TextAttributesKey JS_CONST;
  public static final TextAttributesKey JS_ENUM;

  public static final JSASyntaxHighlighter INSTANCE = new JSASyntaxHighlighter();

  private static SyntaxHighlighter syntaxHighlighter;

  static {
    JS_CONST = TextAttributesKey.createTextAttributesKey("JS.CONST", DefaultLanguageHighlighterColors.CONSTANT);
    JS_ENUM = TextAttributesKey.createTextAttributesKey("JS.ENUM", TextAttributesKey.createTextAttributesKey("JS.CLASS", DefaultLanguageHighlighterColors.CLASS_NAME));
    syntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(Language.findLanguageByID("JavaScript"), null, null);
  }

  @NotNull
  @Override
  public Lexer getHighlightingLexer() {
    return syntaxHighlighter.getHighlightingLexer();
  }

  @NotNull
  @Override
  public TextAttributesKey[] getTokenHighlights(IElementType iElementType) {
    return syntaxHighlighter.getTokenHighlights(iElementType);
  }
}
