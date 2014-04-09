package org.hackillinois.android.people;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SimpleCursorAdapter;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.database.DatabaseTable;
import org.hackillinois.android.models.people.Person;

import java.util.List;

public class HackersFragment extends ListFragment {

    private PeopleListAdapter mPeopleListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mPeopleListAdapter = new PeopleListAdapter(getActivity());
        setListAdapter(mPeopleListAdapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        setListShown(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAndSetData();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.people, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void notifyDataReady() {
        getAndSetData();
    }

    private void getAndSetData() {
        List<Person> people = ((MainActivity)getActivity()).getPeople();
        if (people != null) {
            mPeopleListAdapter.setData(people);
            if (isResumed()) {
                setListShown(true);
            } else {
                setListShownNoAnimation(true);
            }
        }
    }

    public void showResults(Cursor c, String query) {
        int count = c.getCount();
        String countString = getResources().getQuantityString(R.plurals.search_results,
                count, new Object[] {count, query});

        String [] from = new String[] {DatabaseTable.COL_NAME,
                DatabaseTable.COL_EMAIL};

        int[] to = new int[] {R.id.name, R.id.email};
        SimpleCursorAdapter names = new SimpleCursorAdapter(getActivity(), R.layout.query_result_item, c, from, to);
        setListAdapter(names);
    }
}
