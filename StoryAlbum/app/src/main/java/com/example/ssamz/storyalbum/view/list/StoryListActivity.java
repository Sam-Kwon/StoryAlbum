package com.example.ssamz.storyalbum.view.list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

import com.example.ssamz.storyalbum.R;
import com.example.ssamz.storyalbum.adapter.StoryListAdapter;
import com.example.ssamz.storyalbum.data.StoryData;
import com.example.ssamz.storyalbum.database.DbManager;
import com.example.ssamz.storyalbum.view.camera.CameraActivity;
import com.example.ssamz.storyalbum.view.edit.EditActivity;

import java.util.ArrayList;

public class StoryListActivity extends AppCompatActivity implements StoryListContractor.View, ExpandableListView.OnChildClickListener, SearchView.OnQueryTextListener, SwipeDismissListViewTouchListener.DismissCallbacks, AdapterView.OnItemLongClickListener {
    static final String TAG = StoryListActivity.class.getSimpleName();

    Toolbar mToolbar;
    StoryListPresenter mPresenter;
    StoryListModel mModel;

    ExpandableListView mList;
    StoryListAdapter mAdapter;

    ArrayList<ArrayList<StoryData>> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mToolbar = (Toolbar) findViewById(R.id.list_toolbar);
        setSupportActionBar(mToolbar);
        setToolbarTitle(0);

        //DB 초기화
        DbManager.getInstance().init(this);

        //Listener 초기화
        setListener();
    }

    void setListener() {
        //List
        mList = (ExpandableListView) findViewById(R.id.list_story);
        mAdapter = new StoryListAdapter(this);
        mList.setAdapter(mAdapter);
        mList.setOnChildClickListener(this);
        mList.setOnItemLongClickListener(this);

        // connect to presenter.
        mPresenter = new StoryListPresenter();
        mPresenter.setView(this);

        mModel = new StoryListModel();
        mPresenter.setModel(mModel);

        // connect adapter to presenter.
        mAdapter.setPresenter(mPresenter);
        mPresenter.setAdapterModel(mModel);

        //swipe to dismiss
        SwipeDismissListViewTouchListener touchListener = new SwipeDismissListViewTouchListener(mList, this);
        mList.setOnTouchListener(touchListener);
        mList.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    protected void onResume() {
        super.onResume();

        mPresenter.getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_list, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            MenuItemCompat.setOnActionExpandListener(searchItem, new MenuItemCompat.OnActionExpandListener() {

                @Override
                public boolean onMenuItemActionExpand(MenuItem item) {
                    return true;
                }

                @Override
                public boolean onMenuItemActionCollapse(MenuItem item) {
                    mPresenter.getDataBySearch(null);
                    return true;
                }
            });
        }
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(this, CameraActivity.class);

                startActivity(intent);

                return true;

            case R.id.action_search:
                return true;

            case R.id.action_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void updateData(ArrayList<ArrayList<StoryData>> list) {
        mData = list;
        mAdapter.setData(mData);

    }

    void setToolbarTitle(int storyCount) {
        if (storyCount < 1)
            getSupportActionBar().setTitle(" 스토리 " );
        else
            getSupportActionBar().setTitle(" 스토리 (" + storyCount + ")");
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        mPresenter.setCurrentData(groupPosition, childPosition);

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra(EditActivity.MODE_EDIT, true);
        startActivity(intent);

        return false;
    }


    /* /
    * SearchView callback
    * */
    @Override
    public boolean onQueryTextSubmit(String query) {
        mPresenter.getDataBySearch(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchTxt) {
        return false;
    }
    /* /
    * SearchView callback end...
    * */




    /* /
    * Swipe to dismiss callback
    * */
    @Override
    public boolean canDismiss(SwipeDismissListViewTouchListener.ExpandableListViewPos position) {
        return false;
    }

    @Override
    public void onDismiss(ExpandableListView listView, SwipeDismissListViewTouchListener.ExpandableListViewPos[] reverseSortedPositions) {
        for (int i = 0; i < reverseSortedPositions.length; i++) {
            mAdapter.deleteChild(reverseSortedPositions[i].headerPos, reverseSortedPositions[i].childPos);
        }
    }
    /* /
    * Swipe to dismiss callback end...
    * */


    /* /
    * long click to dismiss callback end...
    * */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
            final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
            final int childPosition = ExpandableListView.getPackedPositionChild(id);

            Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.snackbar_text_about_delete), Snackbar.LENGTH_LONG)
                    .setAction("dele", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPresenter.deleteChild(groupPosition, childPosition);
                        }
                    })
                    .setActionTextColor(Color.RED)
                    .show();
            return true;
        }
        return false;
    }
}
