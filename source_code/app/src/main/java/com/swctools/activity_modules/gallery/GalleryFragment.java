package com.swctools.activity_modules.gallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.Matrix;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.swctools.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static com.swctools.R.drawable.layout_editor;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    private Matrix matrix;
    private ScaleGestureDetector scaleGestureDetector = null;
    private Context context;
    private static final String SECTION_IMAGE = "SECTION_IMAGE";
    private static final String SECTION_LABEL = "SECTION_LABEL";
    private static final String SECTION_ID = "SECTION_ID";


    private String label;


    protected PinchZoomPan gallery_Image;
    protected TextView image_label;
//    private ImageView image_delete, imageLabelEdit;


    private GalleryFragementInterface galleryFragementInterface;

    public GalleryFragment() {
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        galleryFragementInterface = (GalleryFragementInterface) context;
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GalleryFragment newInstance(String fileLoc, long id, String label) {
        GalleryFragment fragment = new GalleryFragment();
        Bundle args = new Bundle();
        args.putString(SECTION_IMAGE, fileLoc);
        args.putString(SECTION_LABEL, label);
        args.putLong(SECTION_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        gallery_Image = rootView.findViewById(R.id.gallery_Image);
        image_label = rootView.findViewById(R.id.image_label);

        //Get values



        String filePath = getArguments().getString(SECTION_IMAGE);
        final long id = getArguments().getLong(SECTION_ID);
        label = getArguments().getString(SECTION_LABEL);
        try {
            File file = new File(filePath);
            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(layout_editor);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);


            gallery_Image.loadImageOnCanvas(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        image_label.setText(label);


//        image_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                galleryFragementInterface.deleteImage(id);
//            }
//        });
        return rootView;
    }
}

