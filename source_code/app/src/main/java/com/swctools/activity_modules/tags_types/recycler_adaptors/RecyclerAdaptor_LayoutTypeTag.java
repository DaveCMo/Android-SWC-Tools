package com.swctools.activity_modules.tags_types.recycler_adaptors;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swctools.R;
import com.swctools.activity_modules.tags_types.models.LayoutTypeTagContainer;
import com.swctools.activity_modules.tags_types.views.ViewHolder_ListTypeTag;

import java.util.List;

public class RecyclerAdaptor_LayoutTypeTag extends RecyclerView.Adapter<ViewHolder_ListTypeTag> {
    private Context context;
    private List<LayoutTypeTagContainer> layoutTypeTagAdaptorList;
    private LayoutTagTypeRowInterface layoutTagTypeRowInterface;
    private String typeTag;

    public RecyclerAdaptor_LayoutTypeTag(List<LayoutTypeTagContainer> layoutTypeTagAdaptors, String typetag, Context context) {
        this.typeTag = typetag;
        this.context = context;
        this.layoutTypeTagAdaptorList = layoutTypeTagAdaptors;
        layoutTagTypeRowInterface = (LayoutTagTypeRowInterface)context;
    }

    @NonNull
    @Override
    public ViewHolder_ListTypeTag onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_t_rowitem, parent, false);


        return new ViewHolder_ListTypeTag(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder_ListTypeTag holder, int position) {
        final LayoutTypeTagContainer layoutTypeTagContainer = layoutTypeTagAdaptorList.get(position);
        holder.editT_TName.setText(layoutTypeTagContainer.getTName());

        holder.editT_EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutTagTypeRowInterface.editT(layoutTypeTagContainer.getTagId(), typeTag, layoutTypeTagContainer.getTName());
            }
        });

        holder.edtT_DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutTagTypeRowInterface.deleteT(layoutTypeTagContainer.getTagId(), typeTag);
            }
        });
    }

    @Override
    public int getItemCount() {
        return layoutTypeTagAdaptorList.size();
    }


    public interface LayoutTagTypeRowInterface{
        public void editT(int tId, String typeTag, String currentValue);
        public void deleteT(int tId, String typeTag);
        public String TAG = "TAG";
        public String TYPE = "TYPE";
    }
}
