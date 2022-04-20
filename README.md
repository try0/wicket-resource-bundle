# wicket-resource-bundle

Bundles the resources placed in the package of the annotated component into one.  

WicketApplication.css ( = HomePage.css + MyPanel.css )  
WicketApplication.js ( = MyPanel.js )  


```java
public class WicketApplication extends WebApplication {

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
		super.init();
    
		// jp.try0.wicket.resource.bundle.BundleResouceManager
		BundleResouceManager manager = new BundleResouceManager(this);
		// Can be added
		// manager.addJavaScriptResourceReference(JQueryResourceReference.getV3());
		manager.register();

	}
}

```

```java
@BundleResource(name = "HomePage.css")
public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	public HomePage(final PageParameters parameters) {
		super(parameters);

		add(new Label("version", getApplication().getFrameworkSettings().getVersion()));

		add(new MyPanel("myPanel"));

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
}
```
