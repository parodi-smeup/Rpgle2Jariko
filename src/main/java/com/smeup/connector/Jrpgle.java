package com.smeup.connector;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import com.smeup.rpgparser.execution.CommandLineProgram;
import com.smeup.rpgparser.execution.Configuration;
import com.smeup.rpgparser.execution.RunnerKt;
import com.smeup.rpgparser.interpreter.SystemInterface;
import com.smeup.rpgparser.jvminterop.JavaSystemInterface;
import com.smeup.rpgparser.rpginterop.DirRpgProgramFinder;

public class Jrpgle {

	private CommandLineProgram jariko;
	private SystemInterface systemInterface;
	private File rpgDir;
	private Configuration configuration;

	public String setup(String javaInteropPackage, String rpgDirectory) throws Exception {
		systemInterface = new JavaSystemInterface();
		((JavaSystemInterface) systemInterface).addJavaInteropPackage(javaInteropPackage);
		rpgDir = new File(rpgDirectory.trim());
		configuration = new Configuration();
		
		return "";
	}

	public String call(String rpgProgram, String[] rpgParameters) throws Exception {
		String returnValue = "";
		jariko = RunnerKt.getProgram(rpgProgram, systemInterface, Arrays.asList(new DirRpgProgramFinder(rpgDir)));

		List<String> parameters = Arrays.asList(rpgParameters);
		
		// Retrieve 'by-reference' parameters
		parameters = jariko.singleCall(parameters, configuration).getParmsList();
		for(int i=0; i<parameters.size(); i++) {
			rpgParameters[i] = parameters.get(i);
		}
		
		List<String> results = ((JavaSystemInterface) systemInterface).getConsoleOutput();
		if (results.size() > 0) {
			returnValue = results.get(results.size()-1).toString();	
		}
		
		return returnValue;
	}

}
