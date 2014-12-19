/*******************************************************************************
 * Copyright (c) 2014 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.dsl.uml2.design.tests.plugin.manual;

import static org.junit.Assert.fail;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.google.common.collect.Lists;

/**
 * Check if a service called from an interpreted expression exists.
 */
@RunWith(value = Parameterized.class)
public class ServiceExpressionTests {
	private InterpretedExpression underTest;

	private static Set<String> allDeclaredServices = new HashSet<String>();

	public ServiceExpressionTests(InterpretedExpression expression) {
		this.underTest = expression;
	}

	@Parameters
	public static Collection<Object[]> data() {
		List<Object[]> parameters = Lists.newArrayList();

		// Get all declared services
		Set<Method> allDeclaredMethods = new HashSet<Method>();
		ServiceTestsUtils.collectDeclaredServicesFromUmlDesignerViewpoints(allDeclaredMethods);
		for (Method method : allDeclaredMethods) {
			allDeclaredServices.add(method.getName());
		}

		// Get all services called from interpreted expressions
		Set<InterpretedExpression> allServiceExpressions = new HashSet<InterpretedExpression>();
		ServiceTestsUtils.collectServiceExpressionFromUmlDesignerViewpoints(allServiceExpressions);
		for (InterpretedExpression interpretedExpression : allServiceExpressions) {
			parameters.add(new Object[] {interpretedExpression});
		}
		return parameters;
	}

	@Test
	public void isValidService() {
		String expression = underTest.getExpression();
		String service = ServiceTestsUtils.getServiceCall(expression);

		if (service != null && !allDeclaredServices.contains(service)) {
			fail("Expression : " + underTest.getExpression() + " refers to an invalid service :" + service
					+ ". See :" + underTest.getFeature().getName() + " of object "
					+ EcoreUtil.getURI(underTest.getDeclaration()));
		}
	}
}