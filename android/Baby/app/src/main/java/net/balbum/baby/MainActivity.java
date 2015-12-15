package net.balbum.baby;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import net.balbum.baby.Util.Config;
import net.balbum.baby.VO.CardListVo;
import net.balbum.baby.VO.GeneralCardVo;
import net.balbum.baby.adapter.CardViewAdapter;
import net.balbum.baby.lib.retrofit.ServiceGenerator;
import net.balbum.baby.lib.retrofit.TaskService;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    RecyclerView recyclerView;
    List<GeneralCardVo> cardGeneralModelList;
    FloatingActionButton fab;
    NavigationView navigationView;
    Toolbar toolbar;
    LinearLayout drawerLayout;
    SharedPreferences sharedPreferences;
    CardViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = this;
        initToolbar();
        initNavigationView();
        initFab();
        getData();
       // initData();

    }

    private void getData() {
        TaskService taskService = ServiceGenerator.createService(TaskService.class);
        Log.d("test", "url 정보: " + taskService.toString() + "URL" + Config.URL);
        Log.d("test", " getCard시작?~");
        taskService.getCard("token", new Callback<CardListVo>() {
            @Override
            public void success(CardListVo cardListVo, Response response) {
                CardListVo cd = cardListVo;
                cardGeneralModelList = cd.cardList;
                Log.d("test", "size~: " + cardGeneralModelList.get(0).cid);
                initView(cardGeneralModelList);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("test", " taskService failure");
            }
        });
    }

    private void initView(List<GeneralCardVo> cardGeneralModelList) {

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(linearLayoutManager);
            adapter = new CardViewAdapter(cardGeneralModelList, context, R.layout.card_general_row_portrait);

        }else {
            StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(sglm);
            adapter = new CardViewAdapter(cardGeneralModelList, context, R.layout.card_general_row_landscape);
            Log.d("test", "landscape");
        }

        recyclerView.setAdapter(adapter);
    }

    private void initFab() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CardWritingActivity.class);
                startActivity(intent);
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void initNavigationView() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawerLayout = (LinearLayout)findViewById(R.id.drawer_calendar);
        drawerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "선택된 날짜로 타임라인 이동~~", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_camara) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {

        if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if ( id == R.id.logout){
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

