# wicket-resource-bundle

Extends resource bundling of wicket.   
Bundles the resources placed in the package of the annotated component into one.  
wicket-resource-bundle depends on [wicket-core](https://github.com/apache/wicket/tree/master/wicket-core) and [wicketstuff-annotation](https://github.com/wicketstuff/core/tree/master/annotation).  

WicketApplication.css ( = HomePage.css + MyPanel.css )  
WicketApplication.js ( = JQuery + MyPanel.js )  


```java
public class WicketApplication extends WebApplication {

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();
    
		// jp.try0.wicket.resource.bundle.BundleResourceManager
		BundleResourceManager manager = new BundleResourceManager(this);
		// Can be added
		manager.addJavaScriptResourceReference(JQueryResourceReference.getV3());
		manager.register();

	}
}

```

```java
@BundleResource(name = "HomePage.css")
public class HomePage extends WebPage {

	public HomePage(final PageParameters parameters) {
		super(parameters);
		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));
		add(new MyPanel("myPanel"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "HomePage.css")));
	}
}
```

```java
@BundleResources({
	@BundleResource(name = "MyPanel.css"),
	@BundleResource(name = "MyPanel.js") })
public class MyPanel extends Panel {

	public MyPanel(String id) {
		super(id);

		add(new Label("pnlLabel", "MyPanel"));
	}
	
	@Override
	public void renderHead(IHeaderResponse response) {
		super.renderHead(response);
		response.render(CssHeaderItem.forReference(new CssResourceReference(getClass(), "MyPanel.css")));
		response.render(JavaScriptHeaderItem.forReference(new JavaScriptResourceReference(getClass(), "MyPanel.js")));
	}
}
```
