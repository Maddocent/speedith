package speedith.core.reasoning.rules

import speedith.core.reasoning.args.{MultipleRuleArgs, ZoneArg, RuleArg}
import speedith.core.reasoning.{InferenceRule, RuleApplicationInstruction, RuleApplicationResult, Goals}
import java.util.Locale
import speedith.core.i18n.Translations
import speedith.core.lang.SpiderDiagram
import scala.collection.JavaConversions._
import speedith.core.reasoning.rules.transformers.CopyShadingTransformer
import speedith.core.reasoning.rules.instructions.SelectZonesInstruction

class CopyShading extends SimpleInferenceRule[MultipleRuleArgs] {
  def getInferenceRule: InferenceRule[MultipleRuleArgs] = this

  def getInferenceRuleName: String = "copy_shading"

  def getDescription(locale: Locale): String = Translations.i18n(locale, "COPY_SHADING_DESCRIPTION")

  def getPrettyName(locale: Locale): String = Translations.i18n(locale, "COPY_SHADING_PRETTY_NAME")

  def getCategory(locale: Locale): String = Translations.i18n(locale, "INF_RULE_CATEGORY_HETEROGENEOUS")

  def getArgumentType: Class[MultipleRuleArgs] = classOf[MultipleRuleArgs]

  def getInstructions: RuleApplicationInstruction[MultipleRuleArgs] = {
    new SelectZonesInstruction()
  }

  def apply(args: RuleArg, goals: Goals): RuleApplicationResult = {
    apply(getTypedRuleArgs(args), goals)
  }

  private def apply(argsUntyped: MultipleRuleArgs, goals: Goals): RuleApplicationResult = {
    val zoneArgs = argsUntyped.getRuleArgs.map(_.asInstanceOf[ZoneArg])
    val newSubgoals = goals.getGoals.toList.toArray[SpiderDiagram]
    val targetSubgoal = SimpleInferenceRule.getSubgoal(zoneArgs(0), goals)
    val indexOfParent = targetSubgoal.getParentIndexOf(zoneArgs(0).getSubDiagramIndex)
    newSubgoals(zoneArgs(0).getSubgoalIndex) = targetSubgoal.transform(CopyShadingTransformer(indexOfParent, zoneArgs))
    new RuleApplicationResult(Goals.createGoalsFrom(seqAsJavaList(newSubgoals)))
  }
}
