package io.github.memfis19.annca.internal.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import io.github.memfis19.annca.R;
import io.github.memfis19.annca.internal.utils.Utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by kubaspatny on 9/20/17.
 */

public class PreviewSwitchView extends AppCompatImageButton {

	public static final int PREVIEW_ON = 0;
	public static final int PREVIEW_OFF = 1;

	@IntDef({ PREVIEW_ON, PREVIEW_OFF})
	@Retention(RetentionPolicy.SOURCE)
	public @interface PreviewSettings {
	}

	private OnPreviewSettingsChangeListener mOnPreviewSettingsChangeListener;

	public interface OnPreviewSettingsChangeListener {
		void onPreviewSettingsChanged(@PreviewSettings int previewSettings);
	}

	private Context  mContext;
	private Drawable mPreviewOnDrawable;
	private Drawable mPreviewOffDrawable;
	private int mPadding = 5;

	private
	@PreviewSettings
	int mCurrentPreviewSettings = PREVIEW_ON;

	public PreviewSwitchView(Context context) {
		this(context, null);
	}

	public PreviewSwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initializeView();
	}

	public PreviewSwitchView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs);
	}

	private void initializeView() {
		mPreviewOnDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_visibility_white_24dp);
		mPreviewOnDrawable = DrawableCompat.wrap(mPreviewOnDrawable);
		DrawableCompat.setTintList(mPreviewOnDrawable.mutate(), ContextCompat.getColorStateList(mContext, R.drawable.switch_camera_mode_selector));

		mPreviewOffDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_visibility_off_white_24dp);
		mPreviewOffDrawable = DrawableCompat.wrap(mPreviewOffDrawable);
		DrawableCompat.setTintList(mPreviewOffDrawable.mutate(), ContextCompat.getColorStateList(mContext, R.drawable.switch_camera_mode_selector));

		setBackgroundResource(R.drawable.circle_frame_background_dark);
		setOnClickListener(new PreviewSettingsClickListener());
		setIcons();
		mPadding = Utils.convertDipToPixels(mContext, mPadding);
		setPadding(mPadding, mPadding, mPadding, mPadding);
	}

	private void setIcons() {
		if (mCurrentPreviewSettings == PREVIEW_ON) {
			setImageDrawable(mPreviewOnDrawable);
		} else {
			setImageDrawable(mPreviewOffDrawable);
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (Build.VERSION.SDK_INT > 10) {
			if (enabled) {
				setAlpha(1f);
			} else {
				setAlpha(0.5f);
			}
		}
	}

	public void setPreviewSettings(@PreviewSettings int previewSettings) {
		mCurrentPreviewSettings = previewSettings;
		setIcons();
	}

	public void setOnPreviewSettingsChangeListener(OnPreviewSettingsChangeListener onPreviewSettingsChangeListener) {
		mOnPreviewSettingsChangeListener = onPreviewSettingsChangeListener;
	}

	private class PreviewSettingsClickListener implements OnClickListener {

		@Override
		public void onClick(View view) {
			if (mCurrentPreviewSettings == PREVIEW_ON) {
				mCurrentPreviewSettings = PREVIEW_OFF;
			} else {
				mCurrentPreviewSettings = PREVIEW_ON;
			}

			setIcons();

			if (mOnPreviewSettingsChangeListener != null){
				mOnPreviewSettingsChangeListener.onPreviewSettingsChanged(mCurrentPreviewSettings);
			}
		}
	}
}