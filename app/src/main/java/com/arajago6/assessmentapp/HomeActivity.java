package com.arajago6.assessmentapp;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.arajago6.assessmentapp.data_model.ActionData;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Menu menu;
    private ImageSwitcher imgSwitcher;
    private CardView cButton, cFeatured;
    private RecyclerView recList;
    private Boolean isSearchActive = false;
    private ArrayList<ActionData> actionDataList = new ArrayList<ActionData>();
    private Resources res;
    private String sArrResName="";
    private String[] sArray;

    @Override
    @SuppressWarnings("ResourceType")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Setting up the image banner slider
        imgSwitcher = (ImageSwitcher) findViewById(R.id.imgSwitcher);
        imgSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                myView.setLayoutParams(new
                        ImageSwitcher.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                return myView;
            }
        });

        // Adding some animation and listener for banner transition
        Animation in = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        imgSwitcher.setInAnimation(in);

        imgSwitcher.setImageResource(R.drawable.banner_1);

        imgSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgSwitcher.setImageResource(R.drawable.banner_2);
            }
        });

        // Get handles to needed UI elements
        cButton = (CardView) findViewById(R.id.card_button);
        cFeatured = (CardView) findViewById(R.id.card_featured);
        recList = (RecyclerView) findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llmanager = new LinearLayoutManager(this);
        llmanager.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llmanager);
        res = getResources();

        // Get action data from string array and create an ActionData container
        TypedArray actionResources = getResources().obtainTypedArray(R.array.all_actions);
        TypedArray itemDef;

        for (int i = 0; i < actionResources.length(); i++) {
            int resId = actionResources.getResourceId(i, -1);
            if (resId < 0) {
                continue;
            }

            itemDef = res.obtainTypedArray(resId);

            ActionData aData = new ActionData();
            aData.setTitle(itemDef.getString(0));
            aData.setSubTitle(itemDef.getString(1));
            aData.setCubeCount(Integer.parseInt(itemDef.getString(2)));
            aData.setImgName("feat"+Integer.toString(i+1));
            actionDataList.add(aData);
        }

        final ActionAdapter aAdapter = new ActionAdapter(actionDataList);

        // Button listener - Sparks UI change and sets up adapter with ActionData container
        final Button gobtn = (Button) findViewById(R.id.go_button);
        gobtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                imgSwitcher.setVisibility(View.GONE);
                cButton.setVisibility(View.GONE);
                cFeatured.setVisibility(View.GONE);
                isSearchActive=true;
                recList.setAdapter(aAdapter);
                recList.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isSearchActive==true){
            recList.setVisibility(View.GONE);
            cFeatured.setVisibility(View.VISIBLE);
            cButton.setVisibility(View.VISIBLE);
            imgSwitcher.setVisibility(View.VISIBLE);
            isSearchActive=false;
        }
        else{
            super.onBackPressed();
        }
    }

    public void onCardButtonClicked(View view){
        Toast.makeText(getApplicationContext(), "View all actions button clicked", Toast.LENGTH_SHORT).show();
    }

    // onClick methods for featured actions
    public void intentToOneCubeActivity(View view){
        Intent intent = new Intent(getBaseContext(), OpenGLCubeActivity.class);
        intent.putExtra("baseCubeCount",1);
        startActivity(intent);
    }

    public void intentToEightCubesActivity(View view){
        Intent intent = new Intent(getBaseContext(), OpenGLCubeActivity.class);
        intent.putExtra("baseCubeCount",2);
        startActivity(intent);
    }

    public void intentToTSevenCubesActivity(View view){
        Intent intent = new Intent(getBaseContext(), OpenGLCubeActivity.class);
        intent.putExtra("baseCubeCount",3);
        startActivity(intent);
    }

    // Menu inflation and management
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_JNI_get_string) {
            Toast.makeText(getApplicationContext(), "Returned String: "+getMsgFromJNI(), Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (id == R.id.action_call_nonstatic) {
            callJavaNonStaticMethodFromJNI();
            return true;
        }
        else if (id == R.id.action_call_opengl) {
            Intent intent = new Intent(getBaseContext(), JniOpenGLActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // RecyclerView Adapter for Action data
    public class ActionAdapter extends RecyclerView.Adapter<ActionAdapter.ActionViewHolder> {

        private ArrayList<ActionData> actionDataList;
        private Double distance;
        private String resName;
        private int resId;
        private Drawable drawable;

        public ActionAdapter(ArrayList<ActionData> actionDataList) {
            this.actionDataList = actionDataList;
        }

        @Override
        public int getItemCount() {
            return actionDataList.size();
        }

        @Override
        public void onBindViewHolder(ActionViewHolder actionViewHolder, int i) {
            ActionData aData = actionDataList.get(i);
            actionViewHolder.vTitle.setText(aData.getTitle());
            actionViewHolder.vSubTitle.setText(aData.getSubTitle());
            actionViewHolder.vDesc.setText(aData.getDescription());
            actionViewHolder.vTotal.setText("Total: "+ Integer.toString(aData.getCubeCount()));
            resName = aData.getImgName();
            resId = res.getIdentifier(resName,"drawable",getPackageName());
            drawable = getDrawable(resId);
            actionViewHolder.vImage.setImageDrawable(drawable);
        }

        @Override
        public ActionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View itemView = LayoutInflater.
                    from(viewGroup.getContext()).
                    inflate(R.layout.card_item, viewGroup, false);

            return new ActionViewHolder(itemView);
        }

        public class ActionViewHolder extends RecyclerView.ViewHolder {
            protected TextView vTitle;
            protected TextView vSubTitle;
            protected TextView vDesc;
            protected TextView vTotal;
            protected ImageView vImage;

            public ActionViewHolder(View v) {
                super(v);
                vTitle =  (TextView) v.findViewById(R.id.title);
                vSubTitle = (TextView)  v.findViewById(R.id.subtitle);
                vDesc = (TextView) v.findViewById(R.id.desc);
                vTotal = (TextView)  v.findViewById(R.id.total);
                vImage = (ImageView)  v.findViewById(R.id.imageView);

            }
        }
    }

    // Java non static method to be called from JNI
    private String javaInstanceMethod(String jniStr){
        Toast.makeText(getApplicationContext(), "Non static method was called by JNI. "+ jniStr, Toast.LENGTH_SHORT).show();
        return "This string was returned from Java";
    }

    // For JNI. Load library and declare native methods
    static {
        System.loadLibrary("library_native");
    }

    public native String getMsgFromJNI();

    public native void callJavaNonStaticMethodFromJNI();

    public static native void on_surface_created();

    public static native void on_surface_changed(int width, int height);

    public static native void on_draw_frame();

    public static native void init(int width, int height);

    public static native void step();
}

