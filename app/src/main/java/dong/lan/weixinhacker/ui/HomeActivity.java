package dong.lan.weixinhacker.ui;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import dong.lan.microserver.AppServer.AndroidMicroServer;
import dong.lan.weixinhacker.R;
import dong.lan.weixinhacker.ui.base.BaseActivity;
import dong.lan.weixinhacker.ui.fragment.HackingFragment;
import dong.lan.weixinhacker.ui.fragment.HistoryFragment;
import dong.lan.sqlcipher.SPHelper;

import static dong.lan.weixinhacker.ui.base.BaseFragment.KEY_TITTLE;

public class HomeActivity extends BaseActivity {

    private Fragment[] tabs = new Fragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tabs[0] = HackingFragment.newInstance("破解导出");
        tabs[1] = HistoryFragment.newInstance("导出记录");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_web_link:
                String pwd = SPHelper.instance().getString("pwd");
                if (TextUtils.isEmpty(pwd)) {
                    dialog("尚未进行过微信聊天几率破解");
                    return true;
                }
                AndroidMicroServer.pair = pwd;
                startActivity(new Intent(this, WebLinkActivity.class));
                break;
        }
        return true;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return tabs[position];
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).getArguments().getString(KEY_TITTLE);
        }
    }
}
