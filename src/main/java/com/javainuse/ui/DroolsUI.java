package com.javainuse.ui;

import com.javainuse.controller.DroolsTestController;
import com.javainuse.model.Product;
import org.drools.compiler.compiler.DroolsParserException;
import org.drools.compiler.compiler.PackageBuilder;
import org.drools.core.RuleBase;
import org.drools.core.RuleBaseFactory;
import org.drools.core.WorkingMemory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class DroolsUI {

    public static String executeDrools() throws DroolsParserException, IOException {

        PackageBuilder packageBuilder = new PackageBuilder();

        String ruleFile = "/com/rule/Rules.drl";
        InputStream resourceAsStream = DroolsUI.class.getResourceAsStream(ruleFile);

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

    private static void initFrame() {
        JFrame frame = new JFrame();
        frame.setTitle("Drools UI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setLayout(new FlowLayout());

        //
        final JTextArea textArea = new JTextArea();
        textArea.setColumns(10);

        final JLabel label = new JLabel();

        JButton button = new JButton("Submit!");
        button.addActionListener(new ActionListener() {

             @Override
             public void actionPerformed(ActionEvent e) {
                 try {
                     label.setText(executeDrools());
                     textArea.setText("");
                 } catch (DroolsParserException ex) {
                     ex.printStackTrace();
                 } catch (IOException ex) {
                     ex.printStackTrace();
                 }
             }
         });
        //

        frame.add(textArea);
        frame.add(button);
        frame.add(label);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        initFrame();
    }
}
