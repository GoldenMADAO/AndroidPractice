package com.madao.dynamicui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.madao.dynamicui.data.Shakespeare;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_layout);
    }


    public static class DetailsActivity extends Activity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (getResources().getConfiguration().orientation
                    == Configuration.ORIENTATION_LANDSCAPE) {
                finish();
                return;
            }

            if (savedInstanceState == null) {
                DetailsFragment details = new DetailsFragment();
                details.setArguments(getIntent().getExtras());
                getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
            }
        }
    }


    public static class TitlesFragment extends ListFragment {
        boolean mDualPane;
        int mCurCheckPosition = -1;
        private static final String TAG = "Log";

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setListAdapter(new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_activated_1, Shakespeare.TITLES));

            View detailsFrame = getActivity().findViewById(R.id.details);

            mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

            if (savedInstanceState != null) {
                mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
            }

            if (mDualPane) {
                getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                showDetails(mCurCheckPosition);
            }
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
            Toast.makeText(getActivity(), "onSaveInstanceState position " + mCurCheckPosition,
                    Toast.LENGTH_LONG).show();

            outState.putInt("curChoice", mCurCheckPosition);
        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Toast.makeText(getActivity(),
                    "onListItemClick position is " + position, Toast.LENGTH_LONG)
                    .show();

            showDetails(position);
        }

        void showDetails(int index) {
            mCurCheckPosition = index;

            if (index < 0)
                return;

            if (mDualPane) {
                // Don't check the item
                Log.v(TAG, "checked position is " + getListView().getCheckedItemPosition());
                getListView().setItemChecked(index, true);
                Log.v(TAG, "checked position is " + getListView().getCheckedItemPosition());

                DetailsFragment details = (DetailsFragment)
                        getFragmentManager().findFragmentById(R.id.details);
                if (details == null || details.getShownIndex() != index) {
                    details = DetailsFragment.newInstance(index);

                    Toast.makeText(getActivity(),
                            "showDetails dual-pane: create and relplace fragment",
                            Toast.LENGTH_LONG).show();

                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.details, details);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                    ft.commit();
                }

            } else {
                Intent intent = new Intent();
                intent.setClass(getActivity(), DetailsActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        }
    }


    public static class DetailsFragment extends Fragment {
        public static DetailsFragment newInstance(int index) {
            DetailsFragment f = new DetailsFragment();

            Bundle args = new Bundle();
            args.putInt("index", index);
            f.setArguments(args);

            return f;
        }

        public int getShownIndex() {
            return getArguments().getInt("index", 0);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }

            Toast.makeText(getActivity(), "DetailsFragment:onCreateView",
                    Toast.LENGTH_LONG).show();

            ScrollView scroller = new ScrollView(getActivity());
            TextView text = new TextView(getActivity());
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    4, getActivity().getResources().getDisplayMetrics());
            text.setPadding(padding, padding, padding, padding);
            scroller.addView(text);
            text.setText(Shakespeare.DIALOGUE[getShownIndex()]);

            return scroller;
        }
    }

}
