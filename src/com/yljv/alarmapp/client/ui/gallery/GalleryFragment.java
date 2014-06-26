package com.yljv.alarmapp.client.ui.gallery;

import java.io.File;
import java.util.ArrayList;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.yljv.alarmapp.R;
import com.yljv.alarmapp.client.helper.ApplicationSettings;

public class GalleryFragment extends SherlockFragment {
	
	GridView gridView;
	GridViewAdapter gridAdapter;
	ProgressBar progressBar;
	
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
		
		//Load the pictures from the memory
		new LoadFilesTask().execute();
		
		//Make the progress bar visible until all the items are loaded
		progressBar = (ProgressBar) view.findViewById(R.id.progress);
		progressBar.setVisibility(View.VISIBLE);

		return view;
	}
	
	public ArrayList getData() {
		final ArrayList imageItems = new ArrayList();
		
		//get the images from the directory and put them in the list
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() 
				+ ApplicationSettings.directory;
		
		File f = new File(path);
		File[] listFiles = f.listFiles();
		for(int i = 0; i< listFiles.length; i++) {
			Bitmap bitmap = BitmapFactory.decodeFile(listFiles[i].toString());
			imageItems.add(new ImageItem(bitmap));
		}
		return imageItems;
	}
	
	private class LoadFilesTask extends AsyncTask<Void, Void, ArrayList> {
		
		@Override
		protected ArrayList doInBackground(Void... arg0) {
			return getData();
		}

		@Override
		protected void onPostExecute(ArrayList result) {
			super.onPostExecute(result);
			gridAdapter = new GridViewAdapter(getActivity(), R.layout.row_grid,result);
			gridView.setAdapter(gridAdapter);
			progressBar.setVisibility(View.GONE);
			
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int position,
						long id) {
					
					Intent i = new Intent(getActivity(), FullScreenViewImage.class);
		            i.putExtra("position", position);
		            getActivity().startActivity(i);
		        }
			});
			
		}
	}
	
	
}
