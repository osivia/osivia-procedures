package org.osivia.services.procedure.portlet.drools;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.WorkingMemory;
import org.drools.compiler.DroolsError;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderErrors;
import org.drools.rule.*;
import org.drools.rule.Package;
import org.drools.runtime.rule.ConsequenceException;
import org.osivia.services.procedure.portlet.model.ProcedureModel;

import fr.toutatice.portail.cms.nuxeo.api.forms.FormFilterException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.osivia.services.procedure.portlet.drools.Datas;

/**
 * 
 * Exemple de règle 
 * 
import org.osivia.services.procedure.portlet.drools.Context;
import org.osivia.services.procedure.portlet.drools.Datas;


rule "Commandes"
when
  ctx:Context(phase == Context.PREPARE_PHASE)
  datas:Datas ( )
  $total:Number(longValue > 0) from accumulate( $commande:Datas ( $montant : getValue("montantcommande" ) )
                                                                                         from datas.getValue("commandes"),
                                                                                         sum( $montant) )
then
    datas.setValue("commandes","montantcommande",   $total );
end


rule "Referencement 1"

when
  ctx:Context(phase == Context.PREPARE_PHASE)
  datas:Datas (getValue("referencement") == "true" )
then
    datas.setRelevant("code",  true);
end

rule "Referencement 2"

when
  ctx:Context(phase == Context.PREPARE_PHASE)
  datas:Datas (getValue("referencement") == "false")
then
  datas.setRelevant("code",  false);
end



rule "Date dernière commande"

when
  ctx:Context(phase == Context.VALIDATE_PHASE)
  datas:Datas ( getValue("datederniercommande") > getCurrentDate() )
then
  ctx.sendError("Date commande incorrecte");
endv
**/

public class DatasInjection {

	private ProcedureModel model;
	private Map<String, String> globalVariablesValues;
	private String ruleContext;

	public DatasInjection() {
		super();

	}

	public void shouldFire(ProcedureModel model, Map<String, String> globalVariablesValues, String ruleContext)
			throws FormFilterException {
		this.model = model;
		this.globalVariablesValues = globalVariablesValues;
		this.ruleContext = ruleContext;
		
		try {
		RuleBase ruleBase = initialiseDrools();

		WorkingMemory workingMemory = initializeMessageObjects(ruleBase);


			int actualNumberOfRulesFired = workingMemory.fireAllRules();
		} catch (Exception e) {
			if( e.getCause() instanceof FormFilterException)
				throw (FormFilterException) e.getCause();
			else
				throw new FormFilterException(e);
		}

		// assertThat(actualNumberOfRulesFired, is(expectedNumberOfRulesFired));

	}

	private RuleBase initialiseDrools() throws IOException, DroolsParserException {
		PackageBuilder packageBuilder = readRuleFiles();
		return addRulesToWorkingMemory(packageBuilder);
	}

	private PackageBuilder readRuleFiles() throws DroolsParserException, IOException {
		PackageBuilder packageBuilder = new PackageBuilder();

		Reader reader = new StringReader(model.getRules());
		packageBuilder.addPackageFromDrl(reader);

		assertNoRuleErrors(packageBuilder);

		return packageBuilder;
	}

	private RuleBase addRulesToWorkingMemory(PackageBuilder packageBuilder) {
		RuleBase ruleBase = RuleBaseFactory.newRuleBase();
		Package rulesPackage = packageBuilder.getPackage();
		ruleBase.addPackage(rulesPackage);

		return ruleBase;
	}

	private void assertNoRuleErrors(PackageBuilder packageBuilder) {
		PackageBuilderErrors errors = packageBuilder.getErrors();

		if (errors.getErrors().length > 0) {
			StringBuilder errorMessages = new StringBuilder();
			errorMessages.append("Found errors in package builder\n");
			for (int i = 0; i < errors.getErrors().length; i++) {
				DroolsError errorMessage = errors.getErrors()[i];
				errorMessages.append(errorMessage);
				errorMessages.append("\n");
			}
			errorMessages.append("Could not parse knowledge");

			throw new IllegalArgumentException(errorMessages.toString());
		}
	}

	private WorkingMemory initializeMessageObjects(RuleBase ruleBase) {
		WorkingMemory workingMemory = ruleBase.newStatefulSession();
		createHelloWorld(workingMemory);
		return workingMemory;
	}

	private void createHelloWorld(WorkingMemory workingMemory) {
		Context context = new Context(ruleContext);		
		Datas datas = new Datas(globalVariablesValues, model);
		workingMemory.insert(datas);
		workingMemory.insert(context);
	}

}
