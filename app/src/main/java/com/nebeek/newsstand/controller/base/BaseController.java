package com.nebeek.newsstand.controller.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.view.View;

public abstract class BaseController extends RefWatchingController {
    private boolean mActive = false;

    public BaseController() {
        setRetainViewMode(RetainViewMode.RETAIN_DETACH);
    }

    public BaseController(Bundle args) {
    }

//    protected ActionBar getActionBar() {
//        ActionBarInterface actionBarInterface = ((ActionBarInterface)getActivity());
//        return actionBarInterface.getSupportActionBar();
//    }
//
//    protected void setActionBar(Toolbar toolbar) {
//        ActionBarInterface actionBarInterface = ((ActionBarInterface)getActivity());
//        actionBarInterface.setSupportActionBar(toolbar);
//    }
//
//    protected DrawerLayout getDrawerLayout() {
//        DrawerLayoutProvider drawerLayoutProvider = ((DrawerLayoutProvider)getActivity());
//        return drawerLayoutProvider.getDrawerLayout();
//    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        // Default settings to reset Activity UI state to each time a new controller is loaded.
//        DrawerLayout drawerLayout = getDrawerLayout();
//        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Set the default behavior to be back navigation.
                getRouter().handleBack();
                return true;
        }
        return false;
    }

    @Override
    protected void onDestroyView(View view) {
        super.onDestroyView(view);
        setActive(false);
        // Note: in a real application you may wish to unbind your view references by
        // overriding this method and setting each reference to null. This releases the Views
        // that are on the back-stack and saves memory.
    }

    protected void setActive(boolean active) {
        mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }
}