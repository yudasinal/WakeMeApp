package com.yljv.alarmapp.ui;

import com.yljv.alarmapp.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

public class ChooseOrTakePhotoDialog extends DialogFragment {
	
	private static final int CAMERA_REQUEST = 1888;
	protected static final int RESULT_OK = 0; 
    private ImageView imageView;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setItems(R.array.photo_options, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch(which) {
				case 0:
					Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); //CAMERA_REQUEST = 1888???
				case 1:
					chooseFromGallery();
				}
				
			}

		});
		return builder.create();
	}
	

	private void chooseFromGallery() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            Bitmap photo = (Bitmap) data.getExtras().get("data"); 
            imageView.setImageBitmap(photo);
        }  
    } 
	
}

