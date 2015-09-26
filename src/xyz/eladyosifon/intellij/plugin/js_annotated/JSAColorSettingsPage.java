package xyz.eladyosifon.intellij.plugin.js_annotated;

import com.intellij.lang.javascript.JavaScriptSupportLoader;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.AttributesDescriptor;
import com.intellij.openapi.options.colors.ColorDescriptor;
import com.intellij.openapi.options.colors.ColorSettingsPage;
import com.intellij.psi.codeStyle.DisplayPriority;
import com.intellij.psi.codeStyle.DisplayPrioritySortable;
import com.intellij.util.PlatformUtils;
import gnu.trove.THashMap;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Map;

public class JSAColorSettingsPage implements ColorSettingsPage, DisplayPrioritySortable {

  private static final AttributesDescriptor[] ATTRS;
  @NonNls
  private static final Map<String, TextAttributesKey> ADDITIONAL_HIGHLIGHT_DESCRIPTORS;

  static {

    ATTRS = new AttributesDescriptor[]{
      new AttributesDescriptor("Constant", JSASyntaxHighlighter.JS_CONST),
      new AttributesDescriptor("Enum", JSASyntaxHighlighter.JS_ENUM)
    };
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS = new THashMap<String, TextAttributesKey>(2);
    //noinspection ConstantConditions
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS.put("const", JSASyntaxHighlighter.JS_CONST);
    ADDITIONAL_HIGHLIGHT_DESCRIPTORS.put("enum", JSASyntaxHighlighter.JS_ENUM);
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return ATTRS;
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return ADDITIONAL_HIGHLIGHT_DESCRIPTORS;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "JavaScript Annotations";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return JavaScriptSupportLoader.JAVASCRIPT.getIcon();
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return JSASyntaxHighlighter.INSTANCE;
  }

  @NotNull
  public String getDemoText() {
    return
      "\nconst <const>CONST_A</const> = 1;\n" +
        "/**\n" +
        " *\n" +
        " * @const\n" +
        " */\n" +
        "var <const>CONST_B</const> = 2;\n" +
        "/**\n" +
        " *\n" +
        " * @define {number}\n" +
        " */\n" +
        "var <const>DEFINE</const> = 3;\n" +
        "\n" +
        "/**\n" +
        " * @enum\n" +
        " */\n" +
        "var <enum>Enum</enum> = {\n" +
        "  <const>VAR1</const> : 1,\n" +
        "  <const>VAR2</const> : 2,\n" +
        "  <const>VAR3</const> : 3\n" +
        "};\n" +
        "\n" +
        "var My = {};\n" +
        "/**\n" +
        " * \n" +
        " * @const\n" +
        " */\n" +
        "<const>My.CONST</const> = 1;\n" +
        "/**\n" +
        " * @enum\n" +
        " */\n" +
        "<enum>My.Enum</enum> = {\n" +
        "  <const>VAR1</const> : 1,\n" +
        "  <const>VAR2</const> : 2,\n" +
        "  <const>VAR3</const> : 3\n" +
        "};";
  }

  @Override
  public DisplayPriority getPriority() {
    return PlatformUtils.isWebStorm() ? DisplayPriority.KEY_LANGUAGE_SETTINGS : DisplayPriority.LANGUAGE_SETTINGS;
  }
}
