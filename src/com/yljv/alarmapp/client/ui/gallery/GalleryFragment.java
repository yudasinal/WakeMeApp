package com.yljv.alarmapp.client.ui.gallery;

import java.io.File;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.ApplicationSettings;

/*
 * shows you all your saved pictures
 */
public class GalleryFragment extends SherlockFragment{
	
	private GalleryUtils utils;
    private ArrayList<String> imagePaths = new ArrayList<String>();
    private GridViewImageAdapter adapter;
    private GridView gridView;
    private int columnWidth;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar myBar = getActivity().getActionBar();
		myBar.setTitle(R.string.gallery);
		setHasOptionsMenu(true);
	}
 
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gallery_layout, container, false);
 
        gridView = (GridView) view.findViewById(R.id.grid_view);
 
        utils = new GalleryUtils(getActivity());
 
        // Initilizing Grid View
        InitilizeGridLayout();
 
        // loading all image paths from SD card
        File path = new File(Environment
				.getExternalStorageDirectory().getAbsolutePath()
				+ ApplicationSettings.directory);
        String[] fileNames;
        
        if(path.exists())
        {
            fileNames = path.list();
            for(String s : fileNames){
            	imagePaths.add(path.getAbsolutePath() + "/" + s);
            }
        }
        
 
        // Gridview adapter
        adapter = new GridViewImageAdapter(getActivity(), imagePaths,
                columnWidth);
        
        gridView.setAdapter(adapter);
 
        return view;
    }
 
    private void InitilizeGridLayout() {
        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                GalleryConstants.GRID_PADDING, r.getDisplayMetrics());
 
        columnWidth = (int) ((utils.getScreenWidth() - ((GalleryConstants.NUM_OF_COLUMNS + 1) * padding)) / GalleryConstants.NUM_OF_COLUMNS);
 
        gridView.setNumColumns(GalleryConstants.NUM_OF_COLUMNS);
        gridView.setColumnWidth(columnWidth);
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setPadding((int) padding, (int) padding, (int) padding,
                (int) padding);
        gridView.setHorizontalSpacing((int) padding);
        gridView.setVerticalSpacing((int) padding);
    }
 
	/*
	private GridView gridView;
	private PictureAdapter myPictureAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gallery_layout, container, false);
		gridView = (GridView) view.findViewById(R.id.gridView);
		myPictureAdapter = new PictureAdapter(getActivity(), R.layout.gallery_row, getData());
		getActivity().getActionBar().setTitle("Gallery");
		gridView.setAdapter(myPictureAdapter);
		return view;
	}

	private ArrayList getData() {
		final ArrayList pictureItems = new ArrayList();
		TypedArray pics = getResources().obtainTypedArray(R.array.photo_options);
		for (int i = 0; i < pics.length(); i++) {
			Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), 
					pics.getResourceId(i, -1));
			pictureItems.add(new ImageItem(bitmap));
			
			// TODO Auto-generated method stub
		}
		return pictureItems;
	}
	*/
	
	
	
	
	
	
}
