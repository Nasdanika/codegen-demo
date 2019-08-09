package org.nasdanika.codegen.demo.tests;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticException;
import org.junit.Test;
import org.nasdanika.codegen.util.ValidatingModelGenerator;
import org.nasdanika.common.MutableContext;
import org.nasdanika.common.ProgressEntry;
import org.nasdanika.common.SimpleMutableContext;
import org.nasdanika.common.resources.Container;
import org.nasdanika.common.resources.FileSystemContainer;

/**
 * Codegen demo tests.
 * @author Pavel
 *
 */
public class CodegenDemoTests {
	
	private static final String TEST_MODELS_BASE_URI = "org.nasdanika.codegen.demo/models/";
	
	/**
	 * Generates a greeting without interpolation.
	 * @throws Exception
	 */
	@Test
	public void testHtmlSpringBootGeneration() throws Exception {
		try {
			ValidatingModelGenerator<String> validatingModelGenerator = new ValidatingModelGenerator<>(TEST_MODELS_BASE_URI+"html-spring-boot.codegen");
			Container<InputStream> fsc = new FileSystemContainer(new File("target/test-output"));
			MutableContext mc = new SimpleMutableContext();
			mc.register(Container.class, fsc);
			mc.put("name", "World");
			
			ProgressEntry pe = new ProgressEntry("Generating basic text", 0);		
			List<String> result = validatingModelGenerator.createWork(mc).execute(pe);
			
			System.out.println(result);
			
			System.out.println(pe);
		} catch (DiagnosticException de) {
			dumpDiagnostic(de.getDiagnostic(), 0);
			throw de;
		}
	}
	
	static void dumpDiagnostic(Diagnostic d, int indent) {
		for (int i=0; i < indent; ++i) {
			System.out.print("    ");
		}
		System.out.println(toString(d));
	    if (d.getChildren() != null) {
	    	d.getChildren().forEach(c -> dumpDiagnostic(c, indent + 1));
	    }
		
	}
	
	static String toString(Diagnostic d) {
		StringBuilder result = new StringBuilder();
		switch (d.getSeverity()) {
		case Diagnostic.OK:
			result.append("OK");
			break;
		case Diagnostic.INFO:
			result.append("INFO");
			break;
		case Diagnostic.WARNING:
			result.append("WARNING");
			break;
		case Diagnostic.ERROR:
			result.append("ERROR");
			break;
		case Diagnostic.CANCEL:
			result.append("CANCEL");
			break;
		default:
			result.append(Integer.toHexString(d.getSeverity()));
			break;
		}

		result.append(" source=");
		result.append(d.getSource());

		result.append(" code=");
		result.append(d.getCode());

		result.append(' ');
		result.append(d.getMessage());

		if (d.getData() != null) {
			result.append(" data=");
			result.append(d.getData());
		}

		return result.toString();
	}	
	
}
