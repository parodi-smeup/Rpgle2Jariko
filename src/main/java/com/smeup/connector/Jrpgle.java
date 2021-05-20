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
		jariko = RunnerKt.getProgram(rpgProgram, systemInterface, Arrays.asList(new DirRpgProgramFinder(rpgDir)));
		if (null != rpgParameters && rpgParameters.length > 0) {
			jariko.singleCall(Arrays.asList(rpgParameters), configuration);
		} else {
			jariko.singleCall(Arrays.asList(""), configuration);
		}
		List<String> results = ((JavaSystemInterface) systemInterface).getConsoleOutput();
		if (results.size() > 0) {
			return results.get(results.size()-1).toString();	
		} else {
			return "";
		}
	}

}
