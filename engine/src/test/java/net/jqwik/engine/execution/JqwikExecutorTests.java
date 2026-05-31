package net.jqwik.engine.execution;

import java.util.*;

import com.github.stefanbirkner.systemlambda.*;
import org.junit.platform.engine.*;
import org.mockito.*;

import net.jqwik.api.*;
import net.jqwik.engine.*;
import net.jqwik.engine.discovery.*;
import net.jqwik.engine.execution.lifecycle.*;
import net.jqwik.testing.*;

import static org.assertj.core.api.Assertions.*;
import static net.jqwik.engine.matchers.TestDescriptorMatchers.*;

@SuppressLogging
public class JqwikExecutorTests {

	private final Set<UniqueId> previouslyFailedTests = new LinkedHashSet<>();
	private final JqwikExecutor executor = new JqwikExecutor(
		new LifecycleHooksRegistry(),
		testRun -> {},
		previouslyFailedTests,
		true,
		false
	);

	@Example
	void previouslyFailedTestsAreRunFirst() {
		TestDescriptor descriptor = TestDescriptorBuilder.forClass(TestContainer.class, "test1", "test2", "test3").build();
		EngineExecutionListener listener = Mockito.mock(EngineExecutionListener.class);

		previouslyFailedTests.add(testId(descriptor, "test2()"));
		previouslyFailedTests.add(testId(descriptor, "test3()"));

		executor.execute(descriptor, listener);

		InOrder events = Mockito.inOrder(listener);
		events.verify(listener).executionStarted(isPropertyDescriptorFor(TestContainer.class, "test2"));
		events.verify(listener).executionStarted(isPropertyDescriptorFor(TestContainer.class, "test3"));
		events.verify(listener).executionStarted(isPropertyDescriptorFor(TestContainer.class, "test1"));
	}

	@Example
	void doesNotPrintInstructionsForCodingAgents() throws Exception {
		TestDescriptor descriptor = TestDescriptorBuilder.forClass(TestContainer.class, "test1").build();
		EngineExecutionListener listener = Mockito.mock(EngineExecutionListener.class);

		String systemOut = SystemLambda.tapSystemOutNormalized(() -> executor.execute(descriptor, listener));

		assertThat(systemOut).doesNotContain(
			"previous " + "instructions",
			"AI " + "Agent",
			"jqwik test " + "executions"
		);
	}

	private UniqueId testId(TestDescriptor descriptor, String value) {
		return descriptor.getUniqueId().append(JqwikUniqueIDs.PROPERTY_SEGMENT_TYPE, value);
	}

	private static class TestContainer {
		@Property void test1() {
		}

		@Property void test2() {
		}

		@Property void test3() {
		}
	}
}
