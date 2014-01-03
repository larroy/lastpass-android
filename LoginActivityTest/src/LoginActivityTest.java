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
	      UiObject chrome = new UiObject(new UiSelector().description("Apps"));
	      chrome.clickAndWaitForNewWindow();
	      
	      UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
	      appViews.setAsHorizontalList();
	      
	      
	      UiObject notesApp = appViews.getChildByText(new UiSelector()
	         .className(android.widget.TextView.class.getName()), 
	         "Notes");
	      notesApp.clickAndWaitForNewWindow();
	      
	      getUiDevice().pressMenu();
	      

	      UiObject add_note = new UiObject(new UiSelector().text("Add note"));
	      add_note.click();
	      getUiDevice().swipe(442, 1120, 440, 1120, 800);
	      
	      
	}
}
