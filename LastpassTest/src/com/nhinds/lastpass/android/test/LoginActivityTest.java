package com.nhinds.lastpass.android.test;



import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.nhinds.lastpass.android.LoginActivity;
import android.util.Log;


public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {
    private Solo solo;

    public LoginActivityTest() {
      super(LoginActivity.class);
    }

    @Override
    public void setUp() throws Exception {
      solo = new Solo(getInstrumentation(), getActivity());
    }


    @Override
    public void tearDown() throws Exception {
      solo.finishOpenedActivities();
    }
    
    public void testNothing() throws Exception {
    	Log.i(this.getClass().getSimpleName(), "testNothing");
    }
}
