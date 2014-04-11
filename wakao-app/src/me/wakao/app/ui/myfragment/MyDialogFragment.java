package me.wakao.app.ui.myfragment;

import me.wakao.app.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 
 * @author <a href="mailto:kris@krislq.com">Kris.lee</a>
 * @since Mar 12, 2013
 * @version 1.0.0
 */
public class MyDialogFragment extends DialogFragment {
	
	private OnClickListener okbtnClickListener;
	private String tipMsg;
	
	public MyDialogFragment(OnClickListener onClickListener,String tipMsg) {
		this.okbtnClickListener = onClickListener;
		this.tipMsg = tipMsg;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout message_dialog = (LinearLayout) inflater.inflate(
				R.layout.message_dialog, container, false);
		
		TextView msg = (TextView) message_dialog.findViewById(R.id.message);
		Button okBtn = (Button)message_dialog.findViewById(R.id.ok);
		Button cancelBtn = (Button)message_dialog.findViewById(R.id.cancel);
		okBtn.setOnClickListener(okbtnClickListener);
		
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getDialog().dismiss();
			}
		});
		
		msg.setText(tipMsg);
		Log.e("TAG", "DialogFragment create");
		return message_dialog;
	}

}
