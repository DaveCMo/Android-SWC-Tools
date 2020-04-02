package com.swctools.activity_modules.tags_types.views;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_ListTypeTag extends RecyclerView.ViewHolder{
    public TextView editT_TName;
    public Button editT_EditBtn, edtT_DeleteBtn;
    public ViewHolder_ListTypeTag(View itemView) {
        super(itemView);
        editT_TName = (TextView) itemView.findViewById(R.id.editT_TName);
        editT_EditBtn = (Button) itemView.findViewById(R.id.editT_EditBtn);
        edtT_DeleteBtn = (Button) itemView.findViewById(R.id.edtT_DeleteBtn);
    }
}
