package org.nasdanika.html.springbootdemo;

import java.net.URL;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.nasdanika.common.PrintStreamProgressMonitor;
import org.nasdanika.common.ProgressMonitor;
import org.nasdanika.html.Select;
import org.nasdanika.html.Tag;
import org.nasdanika.html.app.Action;
import org.nasdanika.html.app.Application;
import org.nasdanika.html.app.ApplicationBuilder;
import org.nasdanika.html.app.ViewGenerator;
import org.nasdanika.html.app.impl.ActionApplicationBuilder;
import org.nasdanika.html.app.impl.BootstrapContainerApplication;
import org.nasdanika.html.app.impl.ContentAction;
import org.nasdanika.html.bootstrap.BootstrapFactory;
import org.nasdanika.html.bootstrap.Color;
import org.nasdanika.html.bootstrap.Container;
import org.nasdanika.html.bootstrap.InputGroup;
import org.nasdanika.html.bootstrap.Placement;
import org.nasdanika.html.bootstrap.Theme;
import org.nasdanika.html.fontawesome.FontAwesomeFactory;
import org.nasdanika.html.jstree.JsTreeFactory;
import org.nasdanika.html.knockout.KnockoutFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@RequestMapping("/")
	public String home() throws Exception {
		Application app = new BootstrapContainerApplication(Theme.Litera, false) {
			
			{
				container.border(Color.DANGER);
				header.border(Color.DANGER).background(Color.PRIMARY);
				navigationBar.border(Color.DANGER);
				navigationPanel.border(Color.DANGER).widthAuto();
				footer.border(Color.DANGER);
				contentPanel.border(Color.DANGER);
			}
			
		};
		
		app.header("Header")
			.navigationBar("Navigation bar")
			.navigationPanel("Navigation panel")
			.contentPanel("Content")
			.footer("Footer");
		
		return app.toString();
	}
	
	@RequestMapping("/action.html")
	public String actionApp() throws Exception {
		JSONObject json = new JSONObject(new JSONTokener(new URL("https://www.nasdanika.org/products/html/2.0.0-SNAPSHOT/test-dumps/app/action/action.json").openStream()));
		ContentAction contentAction = new ContentAction(json.toMap());
		Application app = new BootstrapContainerApplication(Theme.Default, true) {
			
			{
				header.background(Color.PRIMARY);
				navigationBar.background(Color.LIGHT).text().color(Color.DARK);
				
				footer.background(Color.SECONDARY);
				
				navigationPanel.widthAuto().border(Color.DEFAULT, Placement.RIGHT);
				contentRow.toHTMLElement().style("min-height", "500px");
				container.border(Color.DEFAULT).margin().top(1);

				// Theme select at the bottom for experimentation.
				BootstrapFactory factory = BootstrapFactory.INSTANCE;
				Select select = factory.themeSelect(Theme.Default);
				InputGroup selectInputGroup = factory.inputGroup();
				selectInputGroup.prepend("Select Bootstrap theme");
				selectInputGroup.input(select);
				Container themeSelectorContainer = factory.container();
				themeSelectorContainer.row().col(selectInputGroup).margin().top(2);
				getHTMLPage().body(themeSelectorContainer);
								
				FontAwesomeFactory.INSTANCE.cdn(getHTMLPage());
				JsTreeFactory.INSTANCE.cdn(getHTMLPage());
				KnockoutFactory.INSTANCE.cdn(getHTMLPage());
				
			}
			
		};
		
		Action activeAction = contentAction.getChildren().get(0).getChildren().get(0);
		ApplicationBuilder appBuilder = new ActionApplicationBuilder(activeAction) {
			
			@Override
			protected Object generateHeader(ViewGenerator viewGenerator, ProgressMonitor progressMonitor) {
				return ((Tag) super.generateHeader(viewGenerator, progressMonitor)).addClass("text-dark").style().text().decoration().none();
			}
		};							
		ProgressMonitor progressMonitor = new PrintStreamProgressMonitor();
		appBuilder.build(app, progressMonitor);
		return app.toString();
	}

}
