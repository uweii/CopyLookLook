package com.example.copylooklook;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.sharepres.SharePres;
import com.uwei.fragment.MeiriFragment;
import com.uwei.fragment.WangyiFragment;
import com.uwei.fragment.ZhihuFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private Fragment mShowFragment;
    private NavigationView mNavigationView;
    private int curMenuId;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mNavigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        init();
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, mToolbar, R.string.ic_open, R.string.ic_close) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                Log.d("mainactivity", "onDrawerClosed: ");
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("Mainactivity", "onDrawerOpened: ");
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                updateItemCheckedView();
                switch (item.getItemId()) {
                    case R.id.zhihu:
                        item.setChecked(true);
                        if (curMenuId == 0) {
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        } else {
                            //Toast.makeText(MainActivity.this,"点击了知乎",Toast.LENGTH_SHORT).show();
                            mShowFragment = ZhihuFragment.newInstance();
                            SharePres.setMenuId(MainActivity.this, 0);
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            curMenuId = 0;
                            updateMenu();
                            break;
                        }
                    case R.id.wangyi:
                        item.setChecked(true);
                        if (curMenuId == 1) {
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        } else {
                            // Toast.makeText(MainActivity.this, "点击了网易", Toast.LENGTH_SHORT).show();
                            mShowFragment = WangyiFragment.newInstance();
                            SharePres.setMenuId(MainActivity.this, 1);
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            curMenuId = 1;
                            updateMenu();
                            break;
                        }
                    case R.id.meiri:
                        item.setChecked(true);
                        if (curMenuId == 2) {
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            break;
                        } else {
                            //Toast.makeText(MainActivity.this, "点击了每日", Toast.LENGTH_SHORT).show();
                            mShowFragment = MeiriFragment.newInstance();
                            SharePres.setMenuId(MainActivity.this, 2);
                            mDrawerLayout.closeDrawer(Gravity.LEFT);
                            curMenuId = 2;
                            updateMenu();
                            break;
                        }
                    case R.id.myblog:
                        Uri uri = Uri.parse("https://uweii.github.io/");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                        break;

                }
                return true;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    private void updateItemCheckedView() {  //当点击侧滑栏选项时，将其余
        for (int i = 0; i < 3; i++) {     // 选项设置成未选中
            mNavigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    //获取之前的menuid，初始时显示对应的fragment
    private void init() {
        curMenuId = SharePres.getMenuId(this);
        switch (curMenuId) {
            case 0:
                Log.d("init()", "====");
                mNavigationView.getMenu().getItem(0).setChecked(true);
                mShowFragment = ZhihuFragment.newInstance();
                updateMenu();
                break;
            case 1:
                Log.d("init()", "====");
                mNavigationView.getMenu().getItem(1).setChecked(true);
                mShowFragment = WangyiFragment.newInstance();
                updateMenu();
                break;
            case 2:
                Log.d("init()", "====");
                mNavigationView.getMenu().getItem(2).setChecked(true);
                mShowFragment = MeiriFragment.newInstance();
                updateMenu();
                break;
        }
    }

    private void updateMenu() //响应点击按钮
    {
        switch (curMenuId) {
            case 0:
                mToolbar.setTitle("知乎日报");
                break;
            case 1:
                mToolbar.setTitle("网易头条");
                break;
            case 2:
                mToolbar.setTitle("每日看看");
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mShowFragment).commit();
    }


}
