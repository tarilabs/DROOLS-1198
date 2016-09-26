/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package com.acme.DROOLS_1198;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.Message;
import org.kie.api.builder.Results;
import org.kie.api.definition.KiePackage;
import org.kie.api.definition.rule.Rule;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleTest {
	static final Logger LOG = LoggerFactory.getLogger(RuleTest.class);
	
	@Test
	public void test() {
		KieServices kieServices = KieServices.Factory.get();
        
		KieContainer kContainer = kieServices.getKieClasspathContainer();
        Results verifyResults = kContainer.verify();
        for (Message m : verifyResults.getMessages()) {
        	LOG.info("{}", m);
        }
        
	    LOG.info("Creating kieBase");
	    KieBase kieBase = kContainer.getKieBase();
        
        LOG.info("There should be rules: ");
        for ( KiePackage kp : kieBase.getKiePackages() ) {
        	for (Rule rule : kp.getRules()) {
        		LOG.info("kp " + kp + " rule " + rule.getName());
        	}
        }

	    LOG.info("Creating kieSession");
	    KieSession session = kieBase.newKieSession();
        
        LOG.info("Populating globals");
        Set<String> check = new HashSet<String>();
        session.setGlobal("controlSet", check);
        
        LOG.info("Now running data and checks");
        
        Alert a = new Alert(null);
        session.insert(a);
        session.fireAllRules();
        assertEquals(0, check.size());
        
        Alert a2 = new Alert("prova$");
        session.insert(a2);
        session.fireAllRules();
        assertEquals(1, check.size());
	}
}