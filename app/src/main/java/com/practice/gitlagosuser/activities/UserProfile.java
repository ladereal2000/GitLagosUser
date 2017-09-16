package com.practice.gitlagosuser.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.practice.gitlagosuser.R;
import com.practice.gitlagosuser.tasks.GitUsersTaskLoader;
import com.practice.gitlagosuser.utils.ImageBlur;
import com.practice.gitlagosuser.utils.InternetConnection;
import com.practice.gitlagosuser.utils.Keys;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks{

    ProgressBar progress;
    LinearLayout no_internet_layout;
    CoordinatorLayout main_layout;
    ImageView iv_cover;

    Context context;

    public static String profile_url, fullnames, user_html_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        initCollapsingToolbar();

        progress=(ProgressBar) findViewById(R.id.progress);
        //hide on load
        main_layout=(CoordinatorLayout) findViewById(R.id.main_layout);
        main_layout.setVisibility(View.GONE);
        //hide on load
        no_internet_layout = (LinearLayout) findViewById(R.id.linear_no_internet);
        no_internet_layout.setVisibility(View.GONE);
        //tool bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        //Make Back button On AppBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;

        if (InternetConnection.checkConnection(getApplicationContext())) {
            getLoaderManager().initLoader(1, null, this).forceLoad();
            progress.setIndeterminate(false);
            progress.setVisibility(View.VISIBLE);

        } else {
            progress.setVisibility(View.GONE);
            no_internet_layout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        MenuItem item = menu.findItem(R.id.action_share);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share :
                String mssg =
                        "Check out this developer @ " + fullnames + " " + user_html_url;
                Intent intents=new Intent(Intent.ACTION_SEND);
                intents.setType("text/plain");
                intents.putExtra(Intent.EXTRA_TEXT, mssg);
                Intent chooseIntent=Intent.createChooser(intents, "Share " + fullnames);
                startActivity(chooseIntent);

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        profile_url = getIntent().getStringExtra(Keys.UserKeys.KEY_URL);
        return new GitUsersTaskLoader(UserProfile.this, profile_url);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        //hide progress
        progress.setVisibility(View.GONE);
        //main UI Visible
        main_layout.setVisibility(View.VISIBLE);

        String jsonString = (String)data;

        updateUIFromJSON(jsonString);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }


    private void updateUIFromJSON(String JSONString){

        final CircleImageView civ_dp = (CircleImageView)findViewById(R.id.profile_pic);
        iv_cover = (ImageView)findViewById(R.id.iv_cover);
        TextView tv_name, tv_username, tv_bio, tv_html_url;
        tv_name = (TextView)findViewById(R.id.names);
        tv_username = (TextView)findViewById(R.id.username);
        tv_bio = (TextView)findViewById(R.id.bio);
        tv_html_url = (TextView)findViewById(R.id.user_htmn);

        try {
            JSONObject userObject = new JSONObject(JSONString);

            String name = userObject.getString(Keys.UserKeys.KEY_NAMES);
            String username = userObject.getString(Keys.UserKeys.KEY_LOGIN);
            String avata_url = userObject.getString(Keys.UserKeys.KEY_AVATAR_URL);
            String html_url = userObject.getString(Keys.UserKeys.KEY_HTML_URL);
            String bio = userObject.getString(Keys.UserKeys.KEY_BIO);

            Picasso.with(context).load(avata_url)
                    .placeholder(R.drawable.placeholder)
                    .into(civ_dp);

            try {
                Glide.with(this).load(avata_url)
                        .asBitmap()
//                    .into((ImageView) findViewById(R.id.backdrop));
                        .into(new SimpleTarget<Bitmap>(){

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Bitmap image=((BitmapDrawable)civ_dp.getDrawable()).getBitmap();
                                Bitmap blurredBitmap = ImageBlur.blur(context, image, 10f);

                                iv_cover.setImageBitmap(blurredBitmap);
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            fullnames = name;
            tv_name.setText(name);
            tv_username.setText(username);
            tv_bio.setText(bio);
            tv_html_url.setText(html_url);
            user_html_url = html_url;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_profile);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar_profile);
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
                    collapsingToolbar.setTitle(getString(R.string.profile_title));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }



}
