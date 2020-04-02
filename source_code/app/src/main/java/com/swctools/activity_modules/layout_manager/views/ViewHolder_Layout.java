package com.swctools.activity_modules.layout_manager.views;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.swctools.R;

public class ViewHolder_Layout extends RecyclerView.ViewHolder {
    public ImageView layoutImage, layoutCard_factionImage, layoutFavourite;
    public TextView
            layoutCard_LayoutName,
            layoutCard_Player,
            noVersionsTxt;
    public Button
            layoutCard_UpdateWAR,
            layoutCard_selectAndAppl,
            btnViewMore;
    public Toolbar layoutCard_Toolbar;

    public ConstraintLayout layoutCardButtonRow;
    public RecyclerView layoutTagRecycler;
    public ConstraintLayout layoutCard_Layout;


    public ViewHolder_Layout(View v) {
        super(v);
        layoutImage = (ImageView) v.findViewById(R.id.layoutFavImg);
        layoutCard_factionImage = (ImageView) v.findViewById(R.id.layoutCard_factionImage);
        layoutCard_LayoutName = (TextView) v.findViewById(R.id.layoutCard_LayoutName);
        layoutCard_Player = (TextView) v.findViewById(R.id.layoutCard_Player);
        noVersionsTxt = v.findViewById(R.id.noVersionsTxt);
        layoutTagRecycler = v.findViewById(R.id.layoutTagRecycler);
        layoutCard_UpdateWAR = (Button) v.findViewById(R.id.layoutCard_UpdateWAR);
        layoutCard_selectAndAppl = (Button) v.findViewById(R.id.layoutCard_selectAndAppl);
        layoutCard_Toolbar = (Toolbar) v.findViewById(R.id.layoutCard_Toolbar);
        layoutCardButtonRow = v.findViewById(R.id.layoutCardButtonRow);
//            layoutVersionsContainer = (ConstraintLayout) v.findViewById(R.id.layoutVersionsContainer);
        layoutFavourite = (ImageView) v.findViewById(R.id.layoutFavourite);
        layoutCard_Layout = v.findViewById(R.id.layoutCard_Layout);
        btnViewMore = v.findViewById(R.id.btnViewMore);
//        layoutCard_Layout.setOnClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.LayoutClickListener());
        //setOnClickListener(s)
//        layoutCard_UpdateWAR.setOnClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.UpdateWarClicked());
//        layoutCard_selectAndAppl.setOnClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.UpdatePVPClicked());
//        layoutFavourite.setOnClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.FavClicked());
//        layoutCard_Toolbar.setOnMenuItemClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.LayoutToolbarListener());
//        layoutCardExpandRow.setOnClickListener(new RecyclerAdaptor_LayoutList.ViewHolder_Layout.ExpandClicked());

    }

////        public void versionSubListController(int position) {
//////
//////            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//////            if (layoutRecord.isOpen) {
//////                layoutCardExpand.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_arrow_down_black_24dp));
//////                layoutVersionsContainer.setVisibility(View.GONE);
//////                layoutCardButtonRow.setVisibility(View.VISIBLE);
//////
//////                ((LayoutRecord) mLayouts.get(position)).isOpen = false;
//////            } else {
//////                layoutCardExpand.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_keyboard_arrow_up_black_24dp));
//////                layoutCardButtonRow.setVisibility(View.GONE);
//////                layoutVersionsContainer.setVisibility(View.VISIBLE);
//////                ((LayoutRecord) mLayouts.get(position)).isOpen = true;
//////            }
//////        }
//
//    class LayoutClickListener implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int position = getLayoutPosition();
//            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//            mInterface.loadLayout(layoutRecord);
//        }
//    }
//
//    class LayoutToolbarListener implements Toolbar.OnMenuItemClickListener {
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            int position = getLayoutPosition();
//            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//            int myLayoutId = layoutRecord.getLayoutId();
//            int itemId = menuItem.getItemId();
//            if (itemId == R.id.layout_card_delete) {
//                mInterface.deleteSelectedLayout(myLayoutId);
//            } else if (itemId == R.id.layout_card_edit) {
//                mInterface.editSelectedLayout(myLayoutId);
//            } else if (itemId == R.id.layout_card_moveFolder) {
//                mInterface.moveLayoutToNewFolder(myLayoutId);
//            }
//
//
//            return true;
//        }
//    }
//
//    class UpdatePVPClicked implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int position = getLayoutPosition();
//            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//            int myLayoutId = layoutRecord.getLayoutId();
//            int versionID = LayoutHelper.getMaxVersion(myLayoutId, mContext);
//            mInterface.applySelectedLayout(myLayoutId, versionID);
//        }
//    }
//
//    class ExpandClicked implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int position = getLayoutPosition();
////                versionSubListController(position);
//
//        }
//    }
//
//    class UpdateWarClicked implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int position = getLayoutPosition();
//            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//            int myLayoutId = layoutRecord.getLayoutId();
//            int versionID = LayoutHelper.getMaxVersion(myLayoutId, mContext);
//            mInterface.applySelectedLayoutWar(myLayoutId, versionID);
//        }
//    }
//
//    class FavClicked implements View.OnClickListener {
//        @Override
//        public void onClick(View view) {
//            int position = getLayoutPosition();
//            LayoutRecord layoutRecord = (LayoutRecord) mLayouts.get(position);
//            String currentValue = layoutRecord.getLayoutIsFavourite();
//            LayoutHelper.LayoutHelperEnums newFavValue = LayoutHelper.LayoutHelperEnums.NO;
//            if (StringUtil.isStringNotNull(currentValue)) {
//                if (currentValue.equalsIgnoreCase(LayoutHelper.LayoutHelperEnums.NO.toString())) {
//                    newFavValue = LayoutHelper.LayoutHelperEnums.YES;
//                }
//            } else {
//                newFavValue = LayoutHelper.LayoutHelperEnums.YES;
//            }
//            mInterface.markFavourite(layoutRecord.getLayoutId(), newFavValue);
//        }
//    }

}
