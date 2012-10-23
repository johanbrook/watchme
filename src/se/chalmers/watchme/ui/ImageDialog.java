/**
*	ImageDialog.java
*
*	<p>A dialog class for viewing an image full screen.</p>
*
*	@author Johan Brook
*	@copyright (c) 2012 Johan Brook, Robin Andersson, Lisa Stenberg, Mattias Henriksson
*	@license MIT
*/

package se.chalmers.watchme.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;
import se.chalmers.watchme.R;

public class ImageDialog extends Dialog {

	private ImageView image;
	
	/**
	 * Create a new full screen dialog with a context.
	 * 
	 * <p>An ClickListener is automatically set on the view, and listens
	 * to click in order to dismiss the dialog.</p>
	 * 
	 * @param context The context.
	 */
	public ImageDialog(Context context) {
		super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
		
		this.image = new ImageView(context);
		addContentView(image, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		
		this.image.setOnClickListener(new CancelListener());
	}
	
	/**
	 * Create a new full screen dialog with a context and a
	 * bitmap image.
	 * 
	 * @param context The context
	 * @param bm The bitmap image to initialize with
	 */
	public ImageDialog(Context context, Bitmap bm) {
		this(context);
		setImage(bm);
	}
	
	/**
	 * Set the dialog's bitmap image.
	 * 
	 * @param bm The bitmap image
	 */
	public void setImage(Bitmap bm) {
		this.image.setImageBitmap(bm);
	}

	@Override
	public void show() {
		super.show();
		
		// Show a quick info message about how to dimiss the dialog
		Toast.makeText(this.getContext(), getContext().getString(R.string.poster_dialog_dismiss_text), Toast.LENGTH_LONG).show();
	}
	
	/**
	 * The listener for when the view is tapped. It should
	 * then be dismissed.
	 * 
	 * @author Johan
	 */
	private class CancelListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			dismiss();
		}
	}

}
