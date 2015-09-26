package xyz.eladyosifon.intellij.plugin.js_annotated;

import com.intellij.lang.annotation.Annotation;
import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class JSAAnnotator implements Annotator {

  private static final String CONST_KEYWORD = "JS:CONST_KEYWORD";
  private static final String JSDOC_TAG_NAME = "JS:DOC_TAG_NAME";
  private static final String VARIABLE = "JS:VARIABLE";
  private static final String IDENTIFIER = "JS:IDENTIFIER";
  private static final String PROPERTY = "JS:PROPERTY";
  private static final String EQ = "JS:EQ";
  private static final String ASSIGNMENT_EXPRESSION = "JS:ASSIGNMENT_EXPRESSION";
  private static final String REFERENCE_EXPRESSION = "JS:REFERENCE_EXPRESSION";

  private static boolean isKeyword(LeafPsiElement leafPsiElement) {
    return CONST_KEYWORD.equals(leafPsiElement.getElementType().toString()) || "const".equals(leafPsiElement.getText());
  }

  @Nullable
  private static TextAttributesKey isJSDocAnnotation(LeafPsiElement leafPsiElement) {
    if (JSDOC_TAG_NAME.equals(leafPsiElement.getElementType().toString())) {
      if ("@enum".equals(leafPsiElement.getText())) {
        return JSASyntaxHighlighter.JS_ENUM;
      } else if ("@const".equals(leafPsiElement.getText()) || "@define".equals(leafPsiElement.getText())) {
        return JSASyntaxHighlighter.JS_CONST;
      }
    }
    return null;
  }

  @NotNull
  protected static PsiElement[] findAnnotationVariables(PsiElement psiElement) {
    PsiElement identifier = null;
    PsiComment jsDocBlock = PsiTreeUtil.getParentOfType(psiElement, PsiComment.class);
    PsiElement next = getNextSiblingOfType(jsDocBlock, VARIABLE);
    if (next == null) {
      String[] types = new String[]{ASSIGNMENT_EXPRESSION, REFERENCE_EXPRESSION};
      for (String type : types) {
        next = getNextSiblingOfType(jsDocBlock, type);
        if (next != null) {
          return PsiTreeUtil.collectElements(next, new PsiElementFilter() {
            @Override
            public boolean isAccepted(PsiElement psiElement) {
              return IDENTIFIER.equals(psiElement.getNode().getElementType().toString()) && !PROPERTY.equals(psiElement.getParent().getNode().getElementType().toString());
            }
          });
        }
      }
    } else {
      PsiElement eq = getNextSiblingOfType(next.getFirstChild(), EQ);
      identifier = PsiTreeUtil.prevVisibleLeaf(eq);
    }

    return new PsiElement[]{identifier};
  }

  protected static void updateReferences(final AnnotationHolder annotationHolder, PsiElement variable, final TextAttributesKey textAttributes) {
    Collection<PsiReference> references;
    do {
      references = ReferencesSearch.search(variable).findAll();
    } while (references.isEmpty() && (variable = variable.getParent()) != null);

    for (PsiReference reference : references) {
      PsiElement referenceElement = reference.getElement();
      PsiElement[] collectElements = PsiTreeUtil.collectElements(referenceElement, new PsiElementFilter() {
        @Override
        public boolean isAccepted(PsiElement psiElement) {
          return IDENTIFIER.equals(psiElement.getNode().getElementType().toString());
        }
      });
      for (PsiElement collectElement : collectElements) {
        highlight(annotationHolder, collectElement, textAttributes);
      }
    }
  }

  private static void highlight(AnnotationHolder annotationHolder, PsiElement variable, TextAttributesKey textAttributes) {
    Annotation annotation = annotationHolder.createInfoAnnotation(variable, null);
    annotation.setTextAttributes(textAttributes);
  }

  @Nullable
  @Contract("null, _ -> null")
  public static PsiElement getNextSiblingOfType(@Nullable PsiElement sibling, String elementTypeString) {
    if (sibling == null) {
      return null;
    } else {
      for (PsiElement child = sibling.getNextSibling(); child != null; child = child.getNextSibling()) {
        if (elementTypeString.equals(child.getNode().getElementType().toString())) {
          return child;
        }
      }

      return null;
    }
  }

  @Override
  public void annotate(@NotNull PsiElement psiElement, final AnnotationHolder annotationHolder) {

    if (psiElement instanceof LeafPsiElement) {

      LeafPsiElement leafPsiElement = (LeafPsiElement) psiElement;
      if (isKeyword(leafPsiElement)) {
        PsiElement variablePsi = psiElement.getNextSibling().getNextSibling().getFirstChild(); // psi-> whitespace -> variable v identifier
        if (variablePsi != null) {
          highlight(annotationHolder, variablePsi, JSASyntaxHighlighter.JS_CONST);
          updateReferences(annotationHolder, variablePsi, JSASyntaxHighlighter.JS_CONST);
        }
        return;
      }

      //TODO: add support for enum constants
      TextAttributesKey textAttributesKey = isJSDocAnnotation(leafPsiElement);
      if (textAttributesKey == JSASyntaxHighlighter.JS_ENUM || textAttributesKey == JSASyntaxHighlighter.JS_CONST) {
        PsiElement[] variables = findAnnotationVariables(leafPsiElement);
        for (PsiElement variablePsi : variables) {
          highlight(annotationHolder, variablePsi, textAttributesKey);
          updateReferences(annotationHolder, variablePsi, textAttributesKey);
        }
      }

    }
  }
}
