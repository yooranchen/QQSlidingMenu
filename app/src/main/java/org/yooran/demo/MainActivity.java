package org.yooran.demo;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.yooran.demo.view.SlidingMenu;


public class MainActivity extends ActionBarActivity {

    private SlidingMenu mSlidingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlidingMenu = (SlidingMenu) findViewById(R.id.slidingMenu);
    }

    /**
     * 控制i菜单
     *
     * @param view
     */
    public void toggleMenu(View view) {
        mSlidingMenu.toggle();
    }
}
