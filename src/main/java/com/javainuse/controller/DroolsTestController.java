package com.javainuse.controller;

import com.javainuse.model.Product;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@RestController
@RequestMapping("/drools")
public class DroolsTestController {
    public String executeDrools() throws DroolsParserException, IOException {

        PackageBuilder packageBuilder = new PackageBuilder();

        String ruleFile = "/com/rule/Rules.drl";
        InputStream resourceAsStream = getClass().getResourceAsStream(ruleFile);

        Reader reader = new InputStreamReader(resourceAsStream);
        packageBuilder.addPackageFromDrl(reader);
        org.drools.core.rule.Package rulesPackage = packageBuilder.getPackage();
        RuleBase ruleBase = RuleBaseFactory.newRuleBase();
        ruleBase.addPackage(rulesPackage);

        WorkingMemory workingMemory = ruleBase.newStatefulSession();

        Product product = new Product();
        product.setType("diamond");

        workingMemory.insert(product);
        workingMemory.fireAllRules();

        return "The discount for the product " + product.getType()
                + " is " + product.getDiscount();
    }

    @GetMapping("/rule")
    public String getRule() throws IOException, DroolsParserException {
        return executeDrools();
    }
}
