/*
 * Copyright 2015-2016 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.junit.gen5.engine.junit4;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.data.Index.atIndex;
import static org.junit.gen5.engine.ExecutionEventConditions.*;
import static org.junit.gen5.engine.TestExecutionResultConditions.causeMessage;
import static org.junit.gen5.engine.TestPlanSpecification.*;

import org.junit.gen5.api.Test;
import org.junit.gen5.engine.EngineAwareTestDescriptor;
import org.junit.gen5.engine.ExecutionEventRecordingEngineExecutionListener;
import org.junit.gen5.engine.ExecutionRequest;
import org.junit.gen5.engine.junit4.samples.PlainJUnit4TestCaseWithSingleTestWhichFails;
import org.junit.gen5.engine.junit4.samples.PlainJUnit4TestCaseWithTwoTests;

class JUnit4TestEngineClassExecutionTests {

	ExecutionEventRecordingEngineExecutionListener listener = new ExecutionEventRecordingEngineExecutionListener();

	@Test
	void executesPlainJUnit4TestCaseWithSingleTestWhichFails() {
		Class<?> testClass = PlainJUnit4TestCaseWithSingleTestWhichFails.class;

		execute(testClass);

		// @formatter:off
		assertThat(listener.getExecutionEvents())
			.hasSize(4)
			.has(allOf(container(testClass.getName()), started()), atIndex(0))
			.has(allOf(test("failingTest"), started()), atIndex(1))
			.has(allOf(test("failingTest"), finishedWithFailure(causeMessage("this test should fail"))), atIndex(2))
			.has(allOf(container(testClass.getName()), finishedSuccessfully()), atIndex(3));
		// @formatter:on
	}

	@Test
	void executesPlainJUnit4TestCaseWithTwoTests() {
		Class<?> testClass = PlainJUnit4TestCaseWithTwoTests.class;

		execute(testClass);

		// @formatter:off
		assertThat(listener.getExecutionEvents())
			.hasSize(6)
			.has(allOf(container(testClass.getName()), started()), atIndex(0))
			.has(allOf(test("failingTest"), started()), atIndex(1))
			.has(allOf(test("failingTest"), finishedWithFailure(causeMessage("this test should fail"))), atIndex(2))
			.has(allOf(test("successfulTest"), started()), atIndex(3))
			.has(allOf(test("successfulTest"), finishedSuccessfully()), atIndex(4))
			.has(allOf(container(testClass.getName()), finishedSuccessfully()), atIndex(5));
		// @formatter:on
	}

	private void execute(Class<?> testClass) {
		JUnit4TestEngine engine = new JUnit4TestEngine();
		EngineAwareTestDescriptor engineTestDescriptor = engine.discoverTests(build(forClass(testClass)));
		engine.execute(new ExecutionRequest(engineTestDescriptor, listener));
	}
}
