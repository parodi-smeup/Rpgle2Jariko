package com.smeup.connector;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class JrpgleTest {
	
	@Test
	public void Hello_print_RPGLE() throws Exception {
		final Jrpgle jRpgle = new Jrpgle();
		final String interopPackage = "com.smeup.api";
		final String rpgDirectory = "src/test/resources/rpg";
		final String rpgProgram = "HELLO.rpgle";
		final String expectedResult = "Hello from HELLO.rpgle";
		jRpgle.setup(interopPackage, rpgDirectory);
		final String[] rpgParameters = {};
		final String result = jRpgle.call(rpgProgram, rpgParameters);
		assertEquals(expectedResult, result);
	}
	
	@Test
	public void Fibonacci_calculator_RPGLE() throws Exception {
		final Jrpgle jRpgle = new Jrpgle();
		final String interopPackage = "com.smeup.api";
		final String rpgDirectory = "src/test/resources/rpg";
		final String rpgProgram = "CALCFIB.rpgle";
		final String[] rpgParameters = {"15", "11"};
		jRpgle.setup(interopPackage, rpgDirectory);
		final String result = jRpgle.call(rpgProgram, rpgParameters);
		// expected result (generated by DSPLY rpgle statement)
		assertEquals("610", result.trim());
		// expected only 2nd parameter changed (due to by-reference behaviour)
		assertEquals("15", rpgParameters[0].trim());
		assertEquals("610", rpgParameters[1].trim());
		
	}
	
	@Test(expected = RuntimeException.class)
	public void Div_by_Zero_RPGLE() throws Exception {
		final Jrpgle jRpgle = new Jrpgle();
		final String interopPackage = "com.smeup.api";
		final String rpgDirectory = "src/test/resources/rpg";
		final String rpgProgram = "DIVBY0.rpgle";
		final String[] rpgParameters = {""};
		jRpgle.setup(interopPackage, rpgDirectory);
		jRpgle.call(rpgProgram, rpgParameters);		
	}

}
