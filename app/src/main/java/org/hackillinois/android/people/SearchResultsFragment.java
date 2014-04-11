package org.hackillinois.android.people;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.SearchView;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.hackillinois.android.MainActivity;
import org.hackillinois.android.R;
import org.hackillinois.android.database.DatabaseTable;
import org.hackillinois.android.models.people.Person;
import org.hackillinois.android.utils.Utils;

public class SearchResultsFragment extends ListFragment {

    private static final String ARG_QUERY = "QUERY_ARG";
    private static final String TAG = "SearchResultsFragment";

    SparseArray<Person> personSparseArray;
    private PeopleListAdapter mPeopleListAdapter;
    private DatabaseTable databaseTable;
    String [] from = new String[] {DatabaseTable.COL_NAME,
            DatabaseTable.COL_EMAIL};
    int[] to = new int[] {R.id.name, R.id.email};

    public static SearchResultsFragment newInstance(String query) {
        Bundle args = new Bundle();
        SearchResultsFragment fragment = new SearchResultsFragment();
        args.putString(ARG_QUERY, query);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(false);
        mPeopleListAdapter = new PeopleListAdapter(getActivity());
        setListAdapter(mPeopleListAdapter);
        databaseTable = new DatabaseTable(getActivity());
        personSparseArray = ((MainActivity) getActivity()).getiOSLookup();
        (getActivity()).setTitle(getString(R.string.search_results));
        String query = getArguments().getString(ARG_QUERY, "");
        if (query != null && !query.isEmpty()) {
            showResults(query);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setEmptyText(getString(R.string.loading_data_error));
        setListShown(true);
        getListView().setClipToPadding(false);
        Utils.setInsets(getActivity(), getListView());
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Person person = (Person) l.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), ProfileViewActivity.class);
        intent.putExtra("person", person);
        startActivity(intent);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.people, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                setSuggestionsAdapter(searchView, s);
                return true;
            }
        });
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

    private void setSuggestionsAdapter(SearchView searchView, String query) {
        if (databaseTable != null) {
            Cursor c = databaseTable.getHackerMatches(query, null);
            if (getActivity() != null) {
                SimpleCursorAdapter names = new SimpleCursorAdapter(getActivity(), R.layout.query_result_item, c, from, to, 0);
                searchView.setSuggestionsAdapter(names);
            }
        }
    }

    private void showResults(String query) {
        Cursor c = databaseTable.getHackerMatches(query, null);
        if (c != null) {
            while (!c.isAfterLast()) {
                int key = c.getInt(1);
                Person person = personSparseArray.get(key);
                mPeopleListAdapter.add(person);
                c.moveToNext();
            }
        }
    }
}
