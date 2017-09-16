package com.practice.gitlagosuser.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.practice.gitlagosuser.R;
import com.practice.gitlagosuser.adapter.IAdapterListener.IUserAdapterListener;
import com.practice.gitlagosuser.adapter.UsersAdapter;
import com.practice.gitlagosuser.models.GitUsers;
import com.practice.gitlagosuser.tasks.GitUsersTaskLoader;
import com.practice.gitlagosuser.utils.InternetConnection;
import com.practice.gitlagosuser.utils.Keys;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks, IUserAdapterListener{


    private List<GitUsers> list;
    private RecyclerView recyclerView;
    private UsersAdapter usersAdapter;
    ProgressBar progress;
    CoordinatorLayout main_layout;
    LinearLayout no_internet_layout;
    Context context;
    ImageView imageView_cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        initCollapsingToolbar();
        //hide on load
        main_layout.setVisibility(View.GONE);
        //hide on load
        no_internet_layout.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        list = new ArrayList<>();
//        usersAdapter = new UsersAdapter(this, list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
         /*recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));*/
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
//        recyclerView.setAdapter(usersAdapter);

        context = this;

        /*Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.bg);
        final Bitmap blurredBitmap = ImageBlur.blur(context, bitmap);

        try {
            Glide.with(this).load(R.drawable.bg)
                    .asBitmap()
//                    .into((ImageView) findViewById(R.id.backdrop));
                    .into(new SimpleTarget<Bitmap>(){

                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            imageView_cover.setImageBitmap(blurredBitmap);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        if (InternetConnection.checkConnection(getApplicationContext())) {
            getLoaderManager().initLoader(1, null, this).forceLoad();
            progress.setIndeterminate(false);
            progress.setVisibility(View.VISIBLE);

        } else {
            progress.setVisibility(View.GONE);
            no_internet_layout.setVisibility(View.VISIBLE);
        }
    }

    private void initialize(){
        progress=(ProgressBar) findViewById(R.id.progress);
        main_layout=(CoordinatorLayout) findViewById(R.id.cl_main_layout);
        imageView_cover = (ImageView) findViewById(R.id.backdrop);
        no_internet_layout = (LinearLayout) findViewById(R.id.linear_no_internet);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
    }


    @Override
    public void onUserClickedClicked(int position) {
        GitUsers user = list.get(position);
        Intent profile_intent = new Intent(this, UserProfile.class);
        profile_intent.putExtra(Keys.UserKeys.KEY_URL, user.getProfileUrl());

        startActivity(profile_intent);
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new GitUsersTaskLoader(MainActivity.this);

    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        //hide progress
        progress.setVisibility(View.GONE);
        //main UI Visible
        main_layout.setVisibility(View.VISIBLE);

        String jsonString = (String)data;
        getGitUsersFromJSON(jsonString);

//        usersAdapter.notifyDataSetChanged();
        usersAdapter = new UsersAdapter(this, list, this);
        recyclerView.setAdapter(usersAdapter);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }




    private void getGitUsersFromJSON(String JSONString){
        try {
            JSONObject rootObject = new JSONObject(JSONString);
            JSONArray jsonArray = rootObject
                    .getJSONArray(Keys.UserKeys.KEY_USER_ROOT_ARRAY);

            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = jsonObject.getInt(Keys.UserKeys.KEY_ID);
                String username = jsonObject.getString(Keys.UserKeys.KEY_LOGIN);
                String score = jsonObject.getString(Keys.UserKeys.KEY_SCORE);
                String avata_url = jsonObject.getString(Keys.UserKeys.KEY_AVATAR_URL);
                String html_url = jsonObject.getString(Keys.UserKeys.KEY_HTML_URL);
                String profile_url = jsonObject.getString(Keys.UserKeys.KEY_URL);

                GitUsers gitUsers = new GitUsers(id, username, score, avata_url, html_url, profile_url);
                list.add(gitUsers);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.backdrop_title));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
   /* public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }*/

    /**
     * Converting dp to pixel
     */
  /*  private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }*/

}
