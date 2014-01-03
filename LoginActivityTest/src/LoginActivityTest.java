import com.android.uiautomator.testrunner.UiAutomatorTestCase;

import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiScrollable;
import com.android.uiautomator.core.UiSelector;


/**
 * 
 */

/**
 * @author piotr
 *
 */
public class LoginActivityTest extends UiAutomatorTestCase {

	/**
	 * @param name
	 */
	public LoginActivityTest() {
		super();
	}

	/* (non-Javadoc)
	 * @see com.android.uiautomator.testrunner.UiAutomatorTestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see com.android.uiautomator.testrunner.UiAutomatorTestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	
	public void testActivityInstalled() throws UiObjectNotFoundException {
	      getUiDevice().pressHome();
	      UiObject allAppsButton = new UiObject(new UiSelector().description("Apps"));
	      allAppsButton.clickAndWaitForNewWindow();
	      UiObject appsTab = new UiObject(new UiSelector().text("Apps"));

	}
}
