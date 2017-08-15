package com.madao.loader;

import android.Manifest;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class LoaderCursor extends Activity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();

        FragmentManager fm = getFragmentManager();

        if (fm.findFragmentById(android.R.id.content) == null) {
            CursorLoaderListFragment list = new CursorLoaderListFragment();
            fm.beginTransaction().add(android.R.id.content, list).commit();
        }
    }

    public void checkPermission() {
        final String permission = Manifest.permission.READ_CONTACTS;

        int permissionCheck = checkSelfPermission(permission);
        if (PackageManager.PERMISSION_GRANTED != permissionCheck) {
            if (shouldShowRequestPermissionRationale(permission)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                requestPermissions(new String[]{permission}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
            else {
                requestPermissions(new String[]{permission}, PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do you need to do.
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(this,
                            "Until you grant the permission, we cannot run the app", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public static class CursorLoaderListFragment extends ListFragment
            implements OnQueryTextListener, SearchView.OnCloseListener,
            LoaderManager.LoaderCallbacks<Cursor> {

        SimpleCursorAdapter mAdaptor;
        SearchView mSearchView;
        String mCurFilter;


        static final String[] CONTACT_SUMMARY_PROJECTION = new String[] {
                Contacts._ID,
                Contacts.DISPLAY_NAME,
                Contacts.CONTACT_STATUS,
                Contacts.CONTACT_PRESENCE,
                Contacts.PHOTO_ID,
                Contacts.LOOKUP_KEY,
        };

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setEmptyText("No phone numbers");

            setHasOptionsMenu(true);

            mAdaptor = new SimpleCursorAdapter(getActivity(),
                    android.R.layout.simple_list_item_2,
                    null,
                    new String[] { Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS },
                    new int[] { android.R.id.text1, android.R.id.text2 },
                    0);
            setListAdapter(mAdaptor);

            setListShown(false);

            getLoaderManager().initLoader(0, null, this);
        }


        /*
         * Listener
         */
        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentComplexList", "Item clicked: " + id);
        }

        @Override
        public boolean onClose() {
            if (!TextUtils.isEmpty(mSearchView.getQuery())) {
                mSearchView.setQuery(null, true);
            }
            return true;
        }


        /*
         * Search View
         */
        public static class MySearchView extends SearchView {
            public MySearchView(Context context) {
                super(context);
            }

            @Override
            public void onActionViewCollapsed() {
                setQuery("", false);
                super.onActionViewCollapsed();
            }
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            MenuItem item = menu.add("Search");
            item.setIcon(android.R.drawable.ic_menu_search);
            item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
                    | MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

            mSearchView = new MySearchView(getActivity());
            mSearchView.setOnQueryTextListener(this);
            mSearchView.setOnCloseListener(this);
            mSearchView.setIconifiedByDefault(true);
            item.setActionView(mSearchView);
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            String newFilter = TextUtils.isEmpty(newText) ? null : newText;

            if (mCurFilter == null && newFilter == null) {
                return true;
            }
            if (mCurFilter != null && mCurFilter.equals(newFilter)) {
                return true;
            }

            mCurFilter = newFilter;
            getLoaderManager().restartLoader(0, null, this);
            return true;
        }

        @Override
        public boolean onQueryTextSubmit(String query) {
            return true;
        }


        /*
         * Loader
         */
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Uri baseUri;
            if (mCurFilter != null) {
                baseUri = Uri.withAppendedPath(Contacts.CONTENT_FILTER_URI,
                        Uri.encode(mCurFilter));
            } else {
                baseUri = Contacts.CONTENT_URI;
            }

            String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                    + Contacts.HAS_PHONE_NUMBER + " = 1) AND ("
                    + Contacts.DISPLAY_NAME + " != '' ))";
            return new CursorLoader(getActivity(), baseUri,
                    CONTACT_SUMMARY_PROJECTION, select, null,
                    Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        }

        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mAdaptor.swapCursor(data);

            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }

        public void onLoaderReset(Loader<Cursor> loader) {
            mAdaptor.swapCursor(null);
        }
    }

}
