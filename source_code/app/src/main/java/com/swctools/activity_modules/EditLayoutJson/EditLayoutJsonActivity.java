package com.swctools.activity_modules.EditLayoutJson;

import android.content.Context;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import com.swctools.activity_modules.EditLayoutJson.models.EditBuilding;
import com.swctools.activity_modules.EditLayoutJson.models.EditLayoutJsonListProvider;
import com.swctools.activity_modules.EditLayoutJson.view_adaptors.RecyclerAdaptor_LayoutEditJson;
import com.swctools.R;
import com.swctools.common.enums.BundleKeys;
import com.swctools.layouts.LayoutHelper;
import com.swctools.common.models.player_models.Building;
import com.swctools.common.models.player_models.MapBuildings;
import com.swctools.util.MethodResult;
import com.swctools.util.Utils;

import java.util.ArrayList;

public class EditLayoutJsonActivity extends AppCompatActivity implements EditJsonRecyclerInterface {
    private static final String TAG = "EditLayoutJsonAc";
    private int layoutId, layoutVersionId;
    private String layoutJson;
    private RecyclerView layoutJson_RecyclerView;
    private Button layoutJsonbtnScrollToBottom, addNew;
    private RecyclerAdaptor_LayoutEditJson recyclerAdaptorLayoutEditJson;
    private String searchString;
    private Context context;
    private FloatingActionButton fabSaveLayoutJson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_edit_layout_json);
        layoutJson_RecyclerView = findViewById(R.id.layoutJson_RecyclerView);
        layoutJsonbtnScrollToBottom = findViewById(R.id.layoutJsonbtnScrollToBottom);
        fabSaveLayoutJson = findViewById(R.id.fabSaveLayoutJson);
        addNew = findViewById(R.id.addNew);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabSaveLayoutJson.setOnClickListener(new SaveClicked());


        layoutId = getIntent().getIntExtra(BundleKeys.LAYOUT_ID.toString(), 0);
        layoutVersionId = getIntent().getIntExtra(BundleKeys.LAYOUT_VERSION_ID.toString(), 0);
        layoutJson = LayoutHelper.getLayoutJson(layoutId, layoutVersionId, this);
        String layoutName = LayoutHelper.getLayoutName(layoutId, this);
        String title = layoutName + " (Version) " + layoutVersionId;
        setTitle(title);
        try {

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            layoutJson_RecyclerView.setLayoutManager(linearLayoutManager);
            layoutJson_RecyclerView.setItemAnimator(new DefaultItemAnimator());
            layoutJson_RecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            layoutJson_RecyclerView.setHasFixedSize(false);
            EditLayoutJsonListProvider.populateLayoutJsonRecords(layoutId, layoutVersionId, this);
            recyclerAdaptorLayoutEditJson = new RecyclerAdaptor_LayoutEditJson(EditLayoutJsonListProvider.getListFromTable(this), this);
            layoutJson_RecyclerView.setAdapter(recyclerAdaptorLayoutEditJson);

            layoutJsonbtnScrollToBottom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutJson_RecyclerView.scrollToPosition(recyclerAdaptorLayoutEditJson.getItemCount() - 1);
                }
            });

            addNew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditLayoutJsonListProvider.addNewRow(context);
                    recyclerAdaptorLayoutEditJson = new RecyclerAdaptor_LayoutEditJson(EditLayoutJsonListProvider.getListFromTable(context), context);
                    layoutJson_RecyclerView.setAdapter(recyclerAdaptorLayoutEditJson);
                    recyclerAdaptorLayoutEditJson.notifyDataSetChanged();
                    layoutJson_RecyclerView.scrollToPosition(recyclerAdaptorLayoutEditJson.getItemCount() - 1);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


    }


    @Override
    public void showMessageFromRecycler(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void scrollToBottom() {
        layoutJson_RecyclerView.scrollToPosition(recyclerAdaptorLayoutEditJson.getItemCount() - 1);
    }

    @Override
    public void saveLayout() {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_layout_json_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.editjsonsearch);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchString = newText;
                recyclerAdaptorLayoutEditJson.getFilter().filter(searchString);
                return false;
            }
        });
        return true;
    }

    class SaveClicked implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            saveData();
        }
    }

    private void saveData() {
        try {
            ArrayList<Building> buildingArrayList = new ArrayList<>();
            for (EditBuilding editBuilding : EditLayoutJsonListProvider.getListFromTable(this)) {
                buildingArrayList.add(editBuilding);
            }
            MapBuildings mapBuildings = new MapBuildings(buildingArrayList);
            MethodResult methodResult = LayoutHelper.updateLayoutVersionJson(layoutId, layoutVersionId, mapBuildings.asOutputJSON(), this);
            Toast.makeText(this, methodResult.getMessage(), Toast.LENGTH_LONG).show();
            if (methodResult.success) {
                finish();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void saveRow(int id, String key, String UID, int x, int y) {
        MethodResult methodResult = EditLayoutJsonListProvider.updateBuilding(id, key, UID, x, y, this);
        if (methodResult.success) {
            ArrayList<EditBuilding> buildingArrayList = EditLayoutJsonListProvider.getListFromTable(this);
            recyclerAdaptorLayoutEditJson.setArrayList(buildingArrayList);
            recyclerAdaptorLayoutEditJson.getFilter().filter(searchString);
        }
    }

    public void removeRow(int id) {
        MethodResult methodResult = EditLayoutJsonListProvider.deleteBuilding(id, this);
        if (methodResult.success) {
            ArrayList<EditBuilding> buildingArrayList = EditLayoutJsonListProvider.getListFromTable(this);
            Utils.Log("No in array now: ", String.valueOf(buildingArrayList.size()));
            recyclerAdaptorLayoutEditJson.setArrayList(buildingArrayList);
            recyclerAdaptorLayoutEditJson.getFilter().filter(searchString);
        }
    }

    public void handleListUpdate(String activity_command, boolean success) {

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;// super.onKeyDown(keyCode, event);
    }
}
