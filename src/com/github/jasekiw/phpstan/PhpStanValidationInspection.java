package com.github.jasekiw.phpstan;

import com.intellij.openapi.util.NlsSafe;
import com.jetbrains.php.tools.quality.QualityToolValidationInspection;
import org.jetbrains.annotations.NotNull;

import static com.github.jasekiw.phpstan.PhpStanConfigurationBaseManager.PHP_STAN;

@SuppressWarnings("InspectionDescriptionNotFoundInspection")
public class PhpStanValidationInspection extends QualityToolValidationInspection<PhpStanValidationInspection> {

  @Override
  protected @NotNull PhpStanAnnotatorProxy getAnnotator() {
    return PhpStanAnnotatorProxy.INSTANCE;
  }

  @Override
  public @NlsSafe String getToolName() {
    return PHP_STAN;
  }
}
