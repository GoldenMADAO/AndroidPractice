package com.madao.app2;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.FragmentTransaction;

public class MainActivity extends FragmentActivity
    implements HeadlinesFragment.OnHeadlineSelectedListener {
    boolean mDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_articles);

        mDualPane = findViewById(R.id.fragment_container) == null;
        if (!mDualPane) {
            if (savedInstanceState != null) {
                return;
            }

            HeadlinesFragment firstFragment = new HeadlinesFragment();
            firstFragment.setArguments(savedInstanceState);

            getFragmentManager().beginTransaction().add(R.id.fragment_container, firstFragment).commit();
        }
    }

    public void onArticleSelected(int position) {
        ArticleFragment articleFrag = (ArticleFragment)
                getFragmentManager().findFragmentById(R.id.article_fragment);

        if (mDualPane) {
            articleFrag.updateArticleView(position);
        } else {
            ArticleFragment newFragment = new ArticleFragment();
            Bundle args = new Bundle();
            args.putInt(ArticleFragment.ARG_POSITION, position);
            newFragment.setArguments(args);

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_container, newFragment);
            ft.addToBackStack(null);
            ft.commit();
        }
    }

}
