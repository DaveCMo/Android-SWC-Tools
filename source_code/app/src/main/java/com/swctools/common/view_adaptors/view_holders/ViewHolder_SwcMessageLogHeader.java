package com.swctools.common.view_adaptors.view_holders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.swctools.R;

public class ViewHolder_SwcMessageLogHeader extends RecyclerView.ViewHolder {

    public TextView swcmessagelog_function, swcmessagelog_message, swcmessagelog_timeStamp;

    public ViewHolder_SwcMessageLogHeader(View itemView) {
        super(itemView);
        swcmessagelog_function = itemView.findViewById(R.id.swcmessagelog_function);
        swcmessagelog_message = itemView.findViewById(R.id.swcmessagelog_message);
        swcmessagelog_timeStamp = itemView.findViewById(R.id.swcmessagelog_timeStamp);
    }


}
