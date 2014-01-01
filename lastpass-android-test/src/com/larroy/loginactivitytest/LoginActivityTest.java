package com.larroy.loginactivitytest;

import com.nhinds.lastpass.android;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

import android.test.ActivityInstrumentationTestCase2;


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
}
