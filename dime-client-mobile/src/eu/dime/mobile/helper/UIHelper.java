package eu.dime.mobile.helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.TextView;
import eu.dime.mobile.DimeClient;
import eu.dime.mobile.R;
import eu.dime.control.DummyLoadingViewHandler;
import eu.dime.mobile.helper.listener.ItemActionListener;
import eu.dime.mobile.helper.objects.DimeIntentObject;
import eu.dime.mobile.helper.objects.ResultObject;
import eu.dime.mobile.helper.objects.ResultObject.RESULT_OBJECT_TYPES;
import eu.dime.mobile.helper.objects.NotificationProperties;
import eu.dime.mobile.helper.objects.ResultObjectDisplayable;
import eu.dime.mobile.helper.objects.ResultObjectProfileSharing;
import eu.dime.mobile.helper.objects.ResultObjectServiceAdapter;
import eu.dime.mobile.helper.objects.IResultOfStandardDialog;
import eu.dime.mobile.view.abstr.BaseAdapterDisplayableItem;
import eu.dime.mobile.view.adapter.BaseAdapter_Dialog_Sharing_Profile;
import eu.dime.mobile.view.adapter.BaseAdapter_ServiceAdapter;
import eu.dime.mobile.view.adapter.BaseAdapter_Standard;
import eu.dime.mobile.view.communication.TabActivity_Livepost_Detail;
import eu.dime.mobile.view.data.TabActivity_Resource_Detail;
import eu.dime.mobile.view.dialog.Activity_Edit_Item_Dialog;
import eu.dime.mobile.view.dialog.Activity_Share_Dialog;
import eu.dime.mobile.view.dialog.ListActivity_Merge_Dialog;
import eu.dime.mobile.view.people.TabActivity_Group_Detail;
import eu.dime.mobile.view.people.TabActivity_Person_Detail;
import eu.dime.mobile.view.situations.TabActivity_Situations;
import eu.dime.model.GenItem;
import eu.dime.model.LoadingAbortedRuntimeException;
import eu.dime.model.Model;
import eu.dime.model.ModelHelper;
import eu.dime.model.ModelRequestContext;
import eu.dime.model.ModelTypeNotFoundException;
import eu.dime.model.TYPES;
import eu.dime.model.specialitem.advisory.AdvisoryItem;
import eu.dime.model.specialitem.advisory.WarningAgentNotValidForSharing;
import eu.dime.model.specialitem.advisory.WarningAttributesDisjunctGroups;
import eu.dime.model.specialitem.advisory.WarningAttributesProfileNotShared;
import eu.dime.model.specialitem.advisory.WarningAttributesUntrusted;
import eu.dime.model.specialitem.advisory.WarningTooManyReceivers;
import eu.dime.model.specialitem.advisory.WarningTooManyResources;
import eu.dime.model.specialitem.usernotification.UNEntryAdhocGroupRecommendation;
import eu.dime.model.specialitem.usernotification.UNEntryMergeRecommendation;
import eu.dime.model.specialitem.usernotification.UNEntryMessage;
import eu.dime.model.specialitem.usernotification.UNEntryRefToItem;
import eu.dime.model.specialitem.usernotification.UNEntrySituationRecommendation;
import eu.dime.model.specialitem.usernotification.UN_TYPE;
import eu.dime.model.specialitem.usernotification.UserNotificationItem;
import eu.dime.model.acl.ACLPackage;
import eu.dime.model.acl.ACLPerson;
import eu.dime.model.displayable.DisplayableItem;
import eu.dime.model.displayable.GroupItem;
import eu.dime.model.displayable.LivePostItem;
import eu.dime.model.displayable.PersonItem;
import eu.dime.model.displayable.ProfileAttributeItem;
import eu.dime.model.displayable.ProfileItem;
import eu.dime.model.displayable.ResourceItem;
import eu.dime.model.displayable.ServiceAdapterItem;
import eu.dime.model.displayable.SituationItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UIHelper {

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** standard ui functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */

	public static void hideView(View v) {
		v.setVisibility(View.GONE);
	}

	public static void showView(View v) {
		v.setVisibility(View.VISIBLE);
	}

	public static TextView createTextView(Context context, int style, int typeface, int textSize, LayoutParams params, boolean isSingleLine) {
		TextView textView = new TextView(context);
		if(isSingleLine) {
			textView.setSingleLine(true);
			textView.setEllipsize(TruncateAt.END);
		}
		if(typeface != -1) textView.setTypeface(null, typeface);
		if(params != null) textView.setLayoutParams(params);
		if(textSize != -1) textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
		if(style != -1) textView.setTextAppearance(context, style);
		return textView;
	}

	public static EditText createEditText(Context context, int input, String hint, int lines, boolean focused) {
		EditText editText = new EditText(context);
		editText.setHint(hint);
		editText.setInputType(input);
		editText.setLines(lines);
		editText.setSingleLine(lines == 1);
		editText.setFocusableInTouchMode(true);
		if (focused)
			editText.requestFocus();
		return editText;
	}

	public static LinearLayout createLinearLayout(Context context, int orientation) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(orientation);
		return linearLayout;
	}

	public static void hideSoftKeyboard(Context context, View view) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	public static void showSoftKeyboard(Context context, View view) {
		((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	public final static boolean isValidEmail(CharSequence target) {
		return (target == null) ? false : (android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches());
	}

	@SuppressLint("DefaultLocale")
	public static int switchInputType(String key) {
		int inputType = InputType.TYPE_CLASS_TEXT;
		if (key.toLowerCase().contains("date")) {
			inputType = InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE;
		} else if (key.toLowerCase().contains("email")) {
			inputType = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS;
		} else if (key.toLowerCase().contains("postal") || key.toLowerCase().contains("pobox") || key.toLowerCase().contains("phone")) {
			inputType = InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED;
		} else if (key.toLowerCase().contains("phone")) {
			inputType = InputType.TYPE_CLASS_PHONE;
		}
		return inputType;
	}

	public static boolean isUIThread() {
		return (Looper.getMainLooper().getThread() == Thread.currentThread());
	}

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** dialog functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */

	public static void createStandardDialog(final Activity activity, ModelRequestContext mrContext, final BaseAdapterDisplayableItem adapter, List<DisplayableItem> items, final ResultObject.RESULT_OBJECT_TYPES type) {
		try {
			Builder builder = new AlertDialog.Builder(activity);
			LinearLayout ll = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_standard, null);
			ListView list = (ListView) ll.findViewById(R.dialog.list);
			TextView emptyText = createEmptyListWidget(activity);
			ll.addView(emptyText);
			list.setEmptyView(emptyText);
			list.setAdapter(adapter);
			list.setTextFilterEnabled(true);
			builder.setView(ll);
			final LinearLayout header = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.dialog_standard_header, null);
			final LinearLayout searchArea = (LinearLayout) header.findViewById(R.dialog.search_area);
			final LinearLayout infoArea = (LinearLayout) header.findViewById(R.dialog.info_area);
			final EditText searchField = (EditText) header.findViewById(R.dialog.searchfield);
			final TextView searchResults = (TextView) header.findViewById(R.dialog.searchresults);
			searchResults.setText(adapter.getCount() + " result(s)");
			searchField.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean hasFocus) {
					if (!hasFocus) hideSoftKeyboard(activity, view);
				}
			});
			searchField.addTextChangedListener(new TextWatcher() {
				@Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
				@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
				@Override
				public void afterTextChanged(Editable searchText) {
					adapter.getFilter().filter(searchText);
					final Handler handler = new Handler(new Handler.Callback() {
						@Override
						public boolean handleMessage(Message msg) {
							searchResults.setText(adapter.getCount() + " result(s)");
							return true;
						}
					});
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							handler.sendEmptyMessage(0);
						}
					}, 200);
				}
			});
			TextView titleTextView = (TextView) header.findViewById(R.dialog.title);
			titleTextView.setText(getLabelForStandardDialog(activity.getResources(), type));
			TextView infoText = (TextView) header.findViewById(R.dialog.info_text);
			infoText.setText(getInfoTextForStandardDialog(activity.getResources(), type));
			Button search = (Button) header.findViewById(R.dialog.button_search);
			search.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (searchArea.getVisibility() == View.GONE) {
						searchArea.setVisibility(View.VISIBLE);
						searchField.requestFocus();
						showSoftKeyboard(activity, searchField);
					} else {
						searchField.setText("");
						searchField.clearFocus();
						searchArea.setVisibility(View.GONE);
					}
				}
			});
			infoArea.setVisibility((DimeClient.getSettings().isDialogInfoAreaDisplayed()) ? View.VISIBLE : View.GONE);
			Button infoButton = (Button) header.findViewById(R.dialog.button_info);
			infoButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (infoArea.getVisibility() == View.GONE) {
						infoArea.setVisibility(View.VISIBLE);
						DimeClient.getSettings().setDialogInfoAreaDisplayed(true);
					} else {
						infoArea.setVisibility(View.GONE);
						DimeClient.getSettings().setDialogInfoAreaDisplayed(false);
					}
				}
			});
			builder.setCustomTitle(header);
			builder.setCancelable(true);
			builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if (activity instanceof IResultOfStandardDialog) {
						IResultOfStandardDialog rosdi = (IResultOfStandardDialog) activity;
						if (adapter instanceof BaseAdapter_Dialog_Sharing_Profile) {
							List<DisplayableItem> items = ((BaseAdapter_Dialog_Sharing_Profile) adapter).getSelectionItems();
 							if(items.size() == 1) {
								ProfileItem item = (ProfileItem) items.get(0);
								rosdi.handleResult(new ResultObjectProfileSharing(item));
							}
						} else if (adapter instanceof BaseAdapter_Standard) {
							List<DisplayableItem> displayables = adapter.getSelectionItems();
							rosdi.handleResult(new ResultObjectDisplayable(displayables, type));
						} else if (adapter instanceof BaseAdapter_ServiceAdapter) {
							List<DisplayableItem> items = ((BaseAdapter_ServiceAdapter) adapter).getSelectionItems();
 							if(items.size() == 1) {
								ServiceAdapterItem item = (ServiceAdapterItem) items.get(0);
								rosdi.handleResult(new ResultObjectServiceAdapter(item));
 							}
						}
					}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
			alert.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
			adapter.init(activity, mrContext, items);
		} catch (Exception e) {
			Toast.makeText(activity, "Error loading items!", Toast.LENGTH_LONG).show();
		}
	}

	public static Builder createAlertDialogBuilder(Context context, String title, boolean isCancelable) {
		Builder builder = new Builder(context);
		builder.setTitle(title);
		builder.setCancelable(isCancelable);
		if (isCancelable) {
			builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			});
		}
		return builder;
	}

	public static Builder createCustomAlertDialogBuilder(Context context, String title, boolean isCancelable, View view) {
		Builder builder = createAlertDialogBuilder(context, title, isCancelable);
		builder.setView(view);
		return builder;
	}

	public static AlertDialog createInfoDialog(Context context, String infotext, String buttonlabel) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(infotext).setCancelable(false).setPositiveButton(buttonlabel, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
			}
		});
		AlertDialog alert = builder.create();
		return alert;
	}
	
	public static ProgressDialog createCustonProgressDialog(Context context, String dialogText) {
		ProgressDialog dialog = ProgressDialog.show(context, null, dialogText, true, true, new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.dismiss();
			}
		});
    	((TextView) dialog.findViewById(android.R.id.message)).setTextColor(Color.WHITE);
    	return dialog;
	}

	public static void displayAlertDialog(Builder builder, boolean showSoftInput) {
		AlertDialog alert = builder.create();
		if(showSoftInput) alert.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		alert.show();
		if (alert.getButton(AlertDialog.BUTTON_NEGATIVE) == null) {
			alert.getButton(AlertDialog.BUTTON_POSITIVE).setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		}
	}
	
	public static Dialog createActionDialog(Context context, List<String> names, OnClickListener listener, List<String> selectedGUIDs) {
		Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_LEFT_ICON);
		dialog.setTitle("Select Action");
		((TextView) dialog.findViewById(android.R.id.title)).setTextColor(Color.WHITE);
		dialog.setContentView(R.layout.dialog_action);
		dialog.setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, R.drawable.icon_white_action);
		TextView selection = (TextView) dialog.findViewById(R.action_dialog.selection_text);
		if (selectedGUIDs != null) {
			String text = selectedGUIDs.size() + " selected item(s)";
			selection.setText(text);
		} else {
			selection.setVisibility(View.GONE);
		}
		LinearLayout tl = (LinearLayout) dialog.findViewById(R.action_dialog.baselayout);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.setMargins(9, 0, 9, 0);
		tl.setLayoutParams(params);
		for (String name : names) {
			Button tmp = new Button(context);
			tmp.setText(name);
			tmp.setTextSize(12);
			tmp.setGravity(Gravity.CENTER_VERTICAL);
			StateListDrawable sld = (StateListDrawable) context.getResources().getDrawable(UIHelper.getResourceId(context.getResources(), name));
			tmp.setCompoundDrawablesWithIntrinsicBounds(sld, null, null, null);
			tmp.setCompoundDrawablePadding(10);
			LinearLayout.LayoutParams lpms = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			lpms.setMargins(0, 0, 0, 5);
			tmp.setLayoutParams(lpms);
			tmp.setOnClickListener(listener);
			if (selectedGUIDs != null && ((selectedGUIDs.isEmpty() && name.contains("select")) || (selectedGUIDs.size() < 2 && name.contains("Merge")))) {
				tmp.setEnabled(false);
			} else if (name.equals(context.getResources().getString(R.string.action_editItem))) {
				tmp.setId(R.string.action_editItem);
			}
			tl.addView(tmp);
		}
		return dialog;
	}

	public static void showItemActionDialog(Context context, GenItem item) {
		Resources res = context.getResources();
		String[] actions = { res.getString(R.string.action_editItem), res.getString(R.string.action_share), res.getString(R.string.action_unshare) };
		ItemActionListener listener = new ItemActionListener(context, new DimeIntentObject(item));
		Dialog dialog = createActionDialog(context, Arrays.asList(actions), listener, Arrays.asList(item.getGuid()));
		listener.setDialog(dialog);
		dialog.show();
		if(ModelHelper.isDisplayableItem(item.getMType())) {
			DisplayableItem di = (DisplayableItem) item;
			if(!di.getUserId().equals(Model.ME_OWNER)) {
				Button button = (Button) dialog.findViewById(R.string.action_editItem);
				if(button != null) button.setEnabled(false);
			}
			if(item.getMType().equals(TYPES.PERSON)) {
				PersonItem person = (PersonItem) di;
				if(person.getDefaultProfileGuid().length() == 0) {
					Button button = (Button) dialog.findViewById(R.string.action_share);
					if(button != null) button.setEnabled(false);
				}
			}
		}
	}

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** header functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */

	public static void inflateStandardHeader(final Activity activity, final DisplayableItem di, ModelRequestContext mrContext) {
		if (di != null) {
			LinearLayout header = (LinearLayout) activity.findViewById(R.id.header);
            if(header != null && di.getUserId().equals(Model.ME_OWNER)) header.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, Activity_Edit_Item_Dialog.class);
					activity.startActivity(DimeIntentObjectHelper.populateIntent(intent, new DimeIntentObject(di)));
				}
			});
			ImageView image = (ImageView) activity.findViewById(R.header.image);
			TextView nameText = (TextView) activity.findViewById(R.header.name_text);
			TextView changedText = (TextView) activity.findViewById(R.header.changed_text);
			TextView childrenText = (TextView) activity.findViewById(R.header.children_text);
			TextView barText = (TextView) activity.findViewById(R.header.bar_text);
			TextView levelText = (TextView) activity.findViewById(R.header.level_text);
			ProgressBar progressBar = (ProgressBar) activity.findViewById(R.header.bar);
			ImageView lock = (ImageView) activity.findViewById(R.id.locked);
			String name = (ModelHelper.isParentable(di)) ? di.getName() + " (" + di.getItems().size() + ")" : di.getName();
			nameText.setText(name);
			changedText.setText(formatDateByMillis(di.getLastUpdated()));
			ImageHelper.loadImageAsynchronously(image, di, activity);
			lock.setVisibility(di.getUserId().equals(Model.ME_OWNER) ? View.GONE : View.VISIBLE);
			AndroidModelHelper.loadGroupsOfChildAsynchronously(activity, mrContext.owner, di, childrenText);
			if(di.getMType().equals(TYPES.GROUP)) {
				barText.setVisibility(View.GONE);
				levelText.setVisibility(View.GONE);
				progressBar.setVisibility(View.GONE);
			} else {
				barText.setText(getTrustOrPrivacyLabelOfProgressBar(di));
				paintDimeProgressBar(activity, progressBar, di);
				updateTrustOrPrivacyLevelTextView(activity, levelText, di);
			}
		} else {
			Toast.makeText(activity, "Error loading item!", Toast.LENGTH_LONG).show();
			activity.getParent().finish();
		}
	}
	
	private static LinearLayout createNewExpandedViewRow(Context context) {
		LinearLayout.LayoutParams lpmsLayout = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		LinearLayout ll = new LinearLayout(context);
		lpmsLayout.setMargins(0, 5, 0, 5);
		ll.setLayoutParams(lpmsLayout);
		ll.setPadding(10, 0, 10, 0);
		ll.setBackgroundColor(context.getResources().getColor(R.color.metabar_grey));
		return ll;
	}

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** adapter functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */
	
	public static void updateInfoContainer(Context context, LinearLayout infoContainer, DisplayableItem di) {
		LinearLayout llchanged = createNewExpandedViewRow(context);
		LinearLayout.LayoutParams lpmsProgressBar = new LinearLayout.LayoutParams(0, 5, 1f);
		LinearLayout.LayoutParams lpmsLabel = new LinearLayout.LayoutParams(100, LinearLayout.LayoutParams.WRAP_CONTENT);
		lpmsProgressBar.gravity = Gravity.CENTER_VERTICAL;
		TextView labelTV = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, lpmsLabel, true);
		labelTV.setText("changed:");
		TextView valueTV = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, null, true);
		valueTV.setText(UIHelper.formatDateByMillis(di.getLastUpdated()));
		llchanged.addView(labelTV);
		llchanged.addView(valueTV);
		infoContainer.addView(llchanged);
		if(!di.getMType().equals(TYPES.GROUP)) {
			LinearLayout llbar = createNewExpandedViewRow(context);
			TextView labelTVbar = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, lpmsLabel, true);
			TextView barText = new TextView(context);
			barText.setTextSize(11);
			barText.setPadding(15, 0, 0, 0);
			ProgressBar progressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
			progressBar.setLayoutParams(lpmsProgressBar);
			progressBar.setMax(2);
			progressBar.setProgress(1);
			llbar.addView(labelTVbar);
			llbar.addView(progressBar);
			llbar.addView(barText);
			infoContainer.addView(llbar);
			updateTrustOrPrivacyLevelTextView(context, barText, di);
			labelTVbar.setText(getTrustOrPrivacyLabelOfProgressBar(di));
			paintDimeProgressBar(context, progressBar, di);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void updatePreviewContainer(Context context, LinearLayout previewContainer, List<DisplayableItem> displayables, DisplayableItem item, TextView emptyContainer) {
		String empty = "";
		int counter = 0;
		switch (item.getMType()) {
		case DATABOX:
			for (DisplayableItem di : displayables) {
				LinearLayout ll = createNewExpandedViewRow(context);
				TextView resourceName = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f), true);
				if (counter > 4) {
					resourceName.setText("...");
					ll.addView(resourceName);
					previewContainer.addView(ll);
					break;
				} else {
					resourceName.setText(di.getName());
					ll.addView(resourceName);
					TextView resourceFileSize = new TextView(context);
					resourceFileSize.setTextSize(11);
					resourceFileSize.setText(String.valueOf(((ResourceItem) di).getFileSize()) + " bytes");
					resourceFileSize.setGravity(Gravity.RIGHT);
					ll.addView(resourceFileSize);
					previewContainer.addView(ll);
				}
				counter++;
			}
			empty = "no resources assigned to databox";
			break;
		case GROUP:
			LinearLayout llGroup = new LinearLayout(context);
			for (DisplayableItem pi : displayables) {
				ImageView persimage = new ImageView(context);
				ImageHelper.loadImageAsynchronously(persimage, pi, context);
				LinearLayout.LayoutParams lpms = new LinearLayout.LayoutParams(50, 50);
				lpms.setMargins(6, 0, 0, 0);
				persimage.setLayoutParams(lpms);
				if (counter == 0 || counter % 6 == 0) {
					llGroup = new LinearLayout(context);
					llGroup.setPadding(0, 0, 0, 10);
					llGroup.setGravity(Gravity.LEFT);
					previewContainer.addView(llGroup);
				}
				llGroup.addView(persimage);
				counter++;
			}
			empty = "no persons assigned to group";
			break;
		case PROFILE:
			for (ProfileAttributeItem pai : (List<ProfileAttributeItem>) (Object) displayables) {
				if (pai != null) {
					TextView labelCat = createTextView(context, -1, Typeface.BOLD, 11, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT), false);
					labelCat.setText(pai.getCategory() + " (" + pai.getName() + ")");
					labelCat.setPadding(10, 0, 0, 0);
					boolean hasChild = false;
					int index = 0;
					for (String key : pai.getValue().keySet()) {
						String label = pai.getLabelForValueKey(key);
						String value = pai.getValue().get(key);
						if (value.length() > 0) {
							if (!hasChild) {
								hasChild = true;
								index = previewContainer.getChildCount();
							}
							LinearLayout ll = createNewExpandedViewRow(context);
							TextView labelTV = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, -1, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f), true);
							labelTV.setText(label);
							TextView valueTV = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, -1, null, true);
							valueTV.setFilters(new InputFilter[] { new InputFilter.LengthFilter(17) });
							valueTV.setText(value);
							ll.addView(labelTV);
							ll.addView(valueTV);
							previewContainer.addView(ll);
						}
					}
					if (hasChild) {
						previewContainer.addView(labelCat, index);
					}
				}
			}
			empty = "no attributes assigned to profile";
			break;
		case LIVEPOST:
			TextView text = new TextView(context);
			text.setText(((LivePostItem) item).getText());
			previewContainer.addView(text);
			empty = "<empty>";
			break;
		default:
			break;
		}
		if((displayables != null && displayables.size() == 0) || (item.getMType().equals(TYPES.LIVEPOST) && ((LivePostItem) item).getText().length() == 0)) {
			emptyContainer.setVisibility(View.VISIBLE);
			emptyContainer.setText(empty);
		} else {
			emptyContainer.setVisibility(View.GONE);
		}
	}

	public static void updateSharedContainerShareables(Context context, LinearLayout previewContainer, List<DisplayableItem> displayables, TextView emptyContainer) {
		int counter = 0;
		if(displayables == null) {
			emptyContainer.setVisibility(View.VISIBLE);
			emptyContainer.setText("error occurred loading shared items");
		} else if(displayables.size() > 0) {
			emptyContainer.setVisibility(View.GONE);
			for (DisplayableItem di : displayables) {
				LinearLayout ll = createNewExpandedViewRow(context);
				TextView resourceName = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f), true);
				if (counter > 4) {
					resourceName.setText("...");
					ll.addView(resourceName);
					previewContainer.addView(ll);
					break;
				} else {
					ImageView image = new ImageView(context);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(24, 24);
					params.setMargins(0, 0, 10, 0);
					image.setLayoutParams(params);
					ImageHelper.loadImageAsynchronously(image, di, context);
					resourceName.setText((ModelHelper.isParentable(di)) ? di.getName() + " ("+di.getItems().size()+")": di.getName());
					ll.addView(image);
					ll.addView(resourceName);
					previewContainer.addView(ll);
				}
				counter++;
			}
		} else {
			emptyContainer.setVisibility(View.VISIBLE);
			emptyContainer.setText("nothing shared");
		}
	}

	public static void updateSharedContainerAgents(Context context, LinearLayout previewContainer, Iterable<ACLPackage> aclPackage, TextView emptyContainer) {
		try {
			if(aclPackage.iterator().hasNext()) {
				emptyContainer.setVisibility(View.GONE);
				for (ACLPackage acl : aclPackage) {
					ProfileItem profile = ModelHelper.getProfileWithSaid(DimeClient.getMRC(new DummyLoadingViewHandler()), acl.getSaidSender()); //FIXME Load async??
					TextView profileHeadline = createTextView(context, 0, Typeface.BOLD, 11, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT), false);
					profileHeadline.setText("@" + profile.getName());
					profileHeadline.setPadding(10, 0, 0, 0);
					previewContainer.addView(profileHeadline);
					int counter = 0;
					for (ACLPerson persons : acl.getPersons()) {
						LinearLayout ll = createNewExpandedViewRow(context);
						TextView personName = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f), true);
						if (counter > 4) {
							personName.setText("...");
							ll.addView(personName);
							previewContainer.addView(ll);
							break;
						} else {
							PersonItem pi = (PersonItem) AndroidModelHelper.getGenItemSynchronously(context, new DimeIntentObject(persons.getPersonId(), TYPES.PERSON));
							personName.setText(pi.getName());
							ll.addView(personName);
							previewContainer.addView(ll);
						}
						counter++;
					}
					counter = 0;
					for (String group : acl.getGroups()) {
						LinearLayout ll = createNewExpandedViewRow(context);
						TextView groupName = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 11, new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f), true);
						if (counter > 4) {
							groupName.setText("...");
							ll.addView(groupName);
							previewContainer.addView(ll);
							break;
						} else {
							GroupItem gi = (GroupItem) AndroidModelHelper.getGenItemSynchronously(context, new DimeIntentObject(group, TYPES.GROUP));
							groupName.setText(gi.getName() + "(group)");
							ll.addView(groupName);
							previewContainer.addView(ll);
						}
						counter++;
					}
				}
			} else {
				emptyContainer.setVisibility(View.VISIBLE);
				emptyContainer.setText("nobody has access");
			}
		} catch (Exception e) { }
	}
	
	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** widget functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */
	
	public static TextView createEmptyListWidget(Context context) {
		TextView emptyText = new TextView(context);
        emptyText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        emptyText.setText("No items in list");
        emptyText.setVisibility(View.GONE);
        emptyText.setId(android.R.id.empty);
        emptyText.setGravity(Gravity.CENTER_HORIZONTAL);
        emptyText.setPadding(0, 20, 0, 20);
        return emptyText;
	}

	public static LinearLayout createSharingWidget(final Context context, final DisplayableItem item, final List<DisplayableItem> selectedItems) {
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setPadding(0, 5, 0, 5);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ImageView image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(40, 40));
		image.setPadding(0, 0, 10, 0);
		image.setImageResource(getDrawableId(item));
		Button recycleButton = new Button(context);
		recycleButton.setBackgroundResource(R.drawable.icon_black_recycle);
		recycleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				selectedItems.remove(item);
				((Activity_Share_Dialog) context).updateViewOnSelectionChanged();
			}
		});
		recycleButton.setLayoutParams(new LayoutParams(40, 40));
		recycleButton.setGravity(Gravity.RIGHT);
		TextView text = createTextView(context, -1, -1, -1, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f), true);
		text.setText(item.getName());
		text.setPadding(0, 0, 0, 5);
		text.setGravity(Gravity.CENTER_VERTICAL);
		ll.addView(image);
		ll.addView(text);
		ll.addView(recycleButton);
		return ll;
	}

	@SuppressWarnings("deprecation")
	public static View createWarningWidget(final Context context, AdvisoryItem advisoryItem) {
		LinearLayout llparent = createLinearLayout(context, LinearLayout.VERTICAL);
		LinearLayout llchild = new LinearLayout(context);
		ImageView image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(40, 40));
		image.setPadding(0, 0, 10, 0);
		final Button expand = new Button(context);
		TextView headline = createTextView(context, -1, -1, 14, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f), true);
		headline.setText(getHeadlineForWarning(context, advisoryItem.getWarningType()));
		headline.setGravity(Gravity.CENTER_VERTICAL);
		final TextView text = createTextView(context, R.style.dimeTheme, Typeface.NORMAL, 12, null, false);
		text.setText(UIHelper.getTextForWarning(context, advisoryItem));
		text.setVisibility(View.GONE);
		text.setPadding(40, -5, 40, 5);
		image.setImageResource(UIHelper.getImageIdForWarning(advisoryItem.getWarningLevel()));
		expand.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_expand_bar));
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(30, 30);
		lp.setMargins(0, 5, 0, 5);
		expand.setLayoutParams(lp);
		expand.setGravity(Gravity.CENTER_VERTICAL);
		llchild.setTag(true);
		llchild.setPadding(0, 5, 0, 0);
		llchild.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(v.getTag().equals(true)){
					v.setTag(false);
					expand.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_collapse_bar));
					text.setVisibility(View.VISIBLE);
				} else {
					v.setTag(true);
					expand.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.button_expand_bar));
					text.setVisibility(View.GONE);
				}
			}
		});
		llchild.addView(image);
		llchild.addView(headline);
		llchild.addView(expand);
		llparent.addView(llchild);
		llparent.addView(text);
		return llparent;
	}

	public static LinearLayout createUnsahreWidget(final Context context, final DisplayableItem item, final List<DisplayableItem> items) {
		final LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.HORIZONTAL);
		ll.setPadding(0, 5, 0, 5);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ImageView image = new ImageView(context);
		image.setLayoutParams(new LayoutParams(40, 40));
		image.setPadding(0, 0, 10, 0);
		image.setImageResource(getDrawableId(item));
		Button recycleButton = new Button(context);
		recycleButton.setBackgroundResource(R.drawable.icon_black_recycle);
		recycleButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((LinearLayout) ll.getParent()).removeView(ll);
				items.add(item);
			}
		});
		recycleButton.setLayoutParams(new LayoutParams(40, 40));
		recycleButton.setGravity(Gravity.RIGHT);
		TextView text = createTextView(context, -1, -1, -1, new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1f), true);
		text.setText(item.getName());
		text.setGravity(Gravity.CENTER_VERTICAL);
		ll.addView(image);
		ll.addView(text);
		ll.addView(recycleButton);
		return ll;
	}
	
	@SuppressWarnings("deprecation")
	public static void paintDimeProgressBar(Context context, ProgressBar pb, DisplayableItem di) {
		int level = AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(di);
		pb.setProgress((level + 1) % 2);
		Rect bounds = pb.getProgressDrawable().getBounds();
		final float[] roundedCorners = new float[] { 5, 5, 5, 5, 5, 5, 5, 5 };
		ShapeDrawable pgDrawable = new ShapeDrawable(new RoundRectShape(roundedCorners, null, null));
		pgDrawable.getPaint().setColor(getWarningColor(context, di));
		ClipDrawable progress = new ClipDrawable(pgDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
		pb.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.progress_horizontal));
		pb.setProgressDrawable(progress);
		pb.getProgressDrawable().setBounds(bounds);
		pb.setProgress(level);
	}
	
	public static String getTrustOrPrivacyLabelOfProgressBar(DisplayableItem di) {
		String s = "";
		if(ModelHelper.isAgent(di.getMType())) {
			s = "Trust:";
		} else if(ModelHelper.isShareable(di.getMType())) {
			s = "Privacy:";
		}
		return s;
	}

	public static void updateTrustOrPrivacyLevelTextView(Context context, TextView tv, DisplayableItem di) {
		tv.setText(getWarningText(di));
		tv.setTextColor(getWarningColor(context, di));
	}
	
	public static String updateEditTrustOrPrivacyLevelHint(Context context, DisplayableItem di) {
		String hint = "";
		if(ModelHelper.isAgent(di.getMType())) {
			hint = context.getResources().getString(R.string.edit_trust_value_hint);
		} else if(ModelHelper.isShareable(di.getMType())) {
			hint = context.getResources().getString(R.string.edit_privacy_value_hint);
		}
		return hint;
	}
	
	public static NotificationProperties getNotificationProperties(Context context, UserNotificationItem userNotification) {
		DimeIntentObject dio = new DimeIntentObject();
        String notificationText = "Error occurred trying to get notification text!";
        String sender = "Personal Server";
        Intent intent = new Intent();           
        int drawableId = R.drawable.icon_black_notification;
        if(userNotification.getUnType().equals(UN_TYPE.REF_TO_ITEM)) {
        	try {
	            UNEntryRefToItem entry = (UNEntryRefToItem) userNotification.getUnEntry();
	        	dio = new DimeIntentObject(entry.getGuid(), entry.getType(), entry.getUserId());
				if (entry.getType().equals(TYPES.GROUP)) {
				    intent = new Intent(context, TabActivity_Group_Detail.class);
				    drawableId = R.drawable.icon_black_group;
				} else if (entry.getType().equals(TYPES.PERSON)) {
				    intent = new Intent(context, TabActivity_Person_Detail.class);
				    drawableId = R.drawable.icon_black_person;
				} else if (entry.getType().equals(TYPES.DATABOX)) {
				    intent = new Intent(context, TabActivity_Resource_Detail.class);
				    drawableId = R.drawable.icon_black_databox;
				} else if (entry.getType().equals(TYPES.RESOURCE)) {
				    intent = new Intent(context, TabActivity_Resource_Detail.class);
				    drawableId = R.drawable.icon_black_data_doc;
				} else if(entry.getType().equals(TYPES.LIVEPOST)){
				    intent = new Intent(context, TabActivity_Livepost_Detail.class);
				    drawableId = R.drawable.icon_black_communication;
				} else {
					throw new Exception("type not supported yet");
				}
				if(entry.getOperation().equals(UNEntryRefToItem.OPERATION_SHARED)) {
					try {
		        		PersonItem pi = (PersonItem) AndroidModelHelper.getGenItemSynchronously(context, new DimeIntentObject(entry.getUserId(), TYPES.PERSON));
		        		if(pi != null) sender = pi.getName();
			        	DisplayableItem di = (DisplayableItem) AndroidModelHelper.getGenItemSynchronously(context, new DimeIntentObject(entry.getGuid(), entry.getType(), entry.getUserId()));
				        if(di != null) notificationText = UIHelper.formatStringOnlyFirstCharUpperCase(entry.getType().toString()) + " \'" + di.getName() + "\' was shared to you!";
		        	} catch (LoadingAbortedRuntimeException e) {
	    				notificationText = "\'unknown item\' was shared to you!";
	    				intent = new Intent();
	    			}
				} else {
					String test = (ModelHelper.isShareable(entry.getType())) ? "privay " : "trust ";
					String operation = (entry.getOperation().equals(UNEntryRefToItem.OPERATION_DEC_PRIV) || entry.getOperation().equals(UNEntryRefToItem.OPERATION_DEC_TRUST)) ? "decrease " : "increase ";
					notificationText = "The system suggests to " + operation + test +  "value of the " + entry.getType() + "!";
					intent = new Intent(context, Activity_Edit_Item_Dialog.class);
				}
			} catch (ModelTypeNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
	    } else if(userNotification.getUnType().equals(UN_TYPE.ADHOC_GROUP_RECOMMENDATION)){
	    	UNEntryAdhocGroupRecommendation entry = (UNEntryAdhocGroupRecommendation) userNotification.getUnEntry();
	    	notificationText = "The system recognized the new adhoc group "+ entry.getName() + "!";
	    	drawableId = R.drawable.icon_black_group;
	    	dio = new DimeIntentObject(entry.getGuid(), TYPES.GROUP);
	    	intent = new Intent(context, TabActivity_Group_Detail.class);
	    } else if(userNotification.getUnType().equals(UN_TYPE.MERGE_RECOMMENDATION)){
	    	UNEntryMergeRecommendation entry = (UNEntryMergeRecommendation) userNotification.getUnEntry();
	    	notificationText = "The system suggests to merge " + entry.getSourceName() + " with " + entry.getTargetName() + "!";
	    	dio = new DimeIntentObject(userNotification.getGuid(), TYPES.USERNOTIFICATION);
	    	intent = new Intent(context, ListActivity_Merge_Dialog.class);
	    	drawableId = R.drawable.icon_black_people;
	    } else if(userNotification.getUnType().equals(UN_TYPE.MESSAGE)){
	    	UNEntryMessage entry = (UNEntryMessage) userNotification.getUnEntry();
	    	notificationText = entry.getMessage() + " Link: " + entry.getLink();
	    	drawableId = R.drawable.icon_black_info;
	    } else if(userNotification.getUnType().equals(UN_TYPE.SITUATION_RECOMMENDATION)){
	    	UNEntrySituationRecommendation entry = (UNEntrySituationRecommendation) userNotification.getUnEntry();
	    	SituationItem si = (SituationItem) AndroidModelHelper.getGenItemSynchronously(context, new DimeIntentObject(entry.getGuid(), TYPES.SITUATION));
	    	notificationText = "The system recognized " + si.getName() + " with a prohability of " + UIHelper.formatDoubleToPercentage(entry.getScore()) + " as current situation! Do you confirm to activate it?";
	    	drawableId = R.drawable.icon_black_situation;
	    	dio = new DimeIntentObject(TYPES.SITUATION);
	    	intent = new Intent(context, TabActivity_Situations.class);
	    } else {
	    	return null;
	    }
	    return new NotificationProperties(notificationText, DimeIntentObjectHelper.populateIntent(intent, dio), drawableId, sender);
	}

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** format functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */

	@SuppressLint("SimpleDateFormat")
	public static String formatDateByMillis(long timeStamp) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeStamp);
		DateFormat df = new SimpleDateFormat("E', 'dd MMM yyyy"); // DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.SHORT)
		return df.format(cal.getTime());
	}
	
	public static String formatStringList(List<String> strings) {
		StringBuilder sb = new StringBuilder();
		String delim = "";
		if(strings.size() == 0) {
			//should not occur
			sb.append(delim).append("\'" + "empty" + "\'");
		} else {
			for (int i = 0; i < strings.size(); i++) {
				sb.append(delim).append("\'" + strings.get(i) + "\'");
				delim = (i != strings.size()-2) ? ", " : " and ";
			}
		}
		return sb.toString();
	}
	
	public static String concatNamesLists(String s1, String s2) {
		String result = "";
		if (s1.length() > 0 && s2.length() > 0) {
			result = s1 + ", " + s2;
		} else if (s1.length() > 0) {
			result = s1;
		} else if (s2.length() > 0) {
			result = s2;
		}
		return result;
	}
	
	@SuppressLint("DefaultLocale") 
    public static String formatStringOnlyFirstCharUpperCase(String string) {
    	string = string.toLowerCase();
    	String firstChar = "" + string.charAt(0);
    	return firstChar.toUpperCase() + string.substring(1);
    }
	
	public static String formatDoubleToPercentage(double score) {
		return ((int) ((score/1.0d) * 100)) + "%";
	}

	/** ------------------------------------------------------------------------------------------------------------------------------------------
	 ** resources functions
	 ** ------------------------------------------------------------------------------------------------------------------------------------------ */
	
	public static int getImageIdForWarning(double warningLevel) {
		int imageId;
		if (warningLevel <= 0.5) {
			imageId = R.drawable.share_state_severe;
		} else {
			imageId = R.drawable.share_state_critical;
		}
		return imageId;
	}
	
	private static CharSequence getHeadlineForWarning(Context context, String warningType) {
		String headline = "";
		if(warningType.equals(AdvisoryItem.WARNING_TYPES[0])) {
			headline = context.getResources().getString(R.string.sharing_warning_untrusted);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[1])) {
			headline = context.getResources().getString(R.string.sharing_warning_disjunct_groups);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[2])) {
			headline = context.getResources().getString(R.string.sharing_warning_unshared_profile);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[3])) {
			headline = context.getResources().getString(R.string.sharing_warning_too_many_resources);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[4])) {
			headline = context.getResources().getString(R.string.sharing_warning_too_many_receivers);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[5])) {
			headline = context.getResources().getString(R.string.sharing_warning_agent_not_valid_for_sharing);
		} else if(warningType.equals(AdvisoryItem.WARNING_TYPES[6])) {
			headline = context.getResources().getString(R.string.sharing_warning_not_possible);
		}
		return headline;
	}
	
	public static String getTextForWarning(Context context, AdvisoryItem advisoryItem) {
//		Resources res = context.getResources();
		String message = "";
		// untrusted
		if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[0])) {
			WarningAttributesUntrusted attributes = (WarningAttributesUntrusted) advisoryItem.getAttributes();
			message += getWarningText(AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(attributes.getPrivacyValue())) + " privacy: " 
					+ formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getPrivateResources(), true))
					+ System.getProperty("line.separator")
					+ getWarningText(AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(attributes.getTrustValue())) + " trust:"
					+ formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getUntrustedAgents(), false));
		}
		// disjunct_groups
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[1])) {
			WarningAttributesDisjunctGroups attributes = (WarningAttributesDisjunctGroups) advisoryItem.getAttributes();
			message += formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getConcernedPersons(), false))
					+ System.getProperty("line.separator")
					+ "previous recepients:"
					+ System.getProperty("line.separator")
					+ formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getPreviousSharedGroups(), false));
		}
		// profile_not_shared
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[2])) {
			WarningAttributesProfileNotShared attributes = (WarningAttributesProfileNotShared) advisoryItem.getAttributes();
			message += "the selected profile was never shared with:"
					+ formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getPersonGuids(), false));
		}
		// too_many_resources
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[3])) {
			WarningTooManyResources attributes = (WarningTooManyResources) advisoryItem.getAttributes();
			message += "More than " 
					+ attributes.getNumberOfResources() 
					+ " items selected!";
		}
		// too_many_receivers
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[4])) {
			WarningTooManyReceivers attributes = (WarningTooManyReceivers) advisoryItem.getAttributes();
			message += "More than " 
					+ attributes.getNumberOfReceivers() +
					" receivers selected!";
		}
		// agent_not_valid_for_sharing
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[5])) {
			WarningAgentNotValidForSharing attributes = (WarningAgentNotValidForSharing) advisoryItem.getAttributes();
			message += "Cannot share to persons "
					+ formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, attributes.getAgentsNotValidForSharing(), false))
					+ ((attributes.getParentGroup().length() > 0) ? " of group " + formatStringList(AndroidModelHelper.getListOfNamesOfGuidList(context, Arrays.asList(attributes.getParentGroup()), false)) : "")
					+ ". Each requires to have a service account which supports sharing!";
		}
		// sharing_not_possible
		else if (advisoryItem.getWarningType().equals(AdvisoryItem.WARNING_TYPES[6])) {
			message = context.getResources().getString(R.string.sharing_warning_not_possible_detailed);
		}
		return message;
	}
	
	private static String getLabelForStandardDialog(Resources res, RESULT_OBJECT_TYPES type) {
		String label = "";
		switch (type) {
		case SERVICE_CONNECTION:
			label = res.getString(R.string.service_connection_dialog_label);;
			break;
		case SHARING_DATABOXES:
			label = "Select databoxes";
			break;
		case SHARING_GROUPS:
			label = "Select groups";
			break;
		case SHARING_LIVEPOSTS:
			label = "Select liveposts";
			break;
		case SHARING_PERSONS:
			label = "Select persons";
			break;
		case SHARING_PROFILE:
			label = "Select profile";
			break;
		case SHARING_RESOURCES:
			label = "Select resources";
			break;
		case ADD_RESOURCES_TO_DATABOX:
			label = "Select resource(s)";
			break;
		case ASSIGN_RESOURCES_TO_DATABOX:
			label = "Select databox(es)";
			break;
		case ADD_PEOPLE_TO_GROUP:
			label = "Select people";
			break;
		case ASSIGN_PEOPLE_TO_GROUP:
			label = "Select group(s)";
			break;
		default:
			break;
		}
		return label;
	}
	
	private static String getInfoTextForStandardDialog(Resources res, RESULT_OBJECT_TYPES type) {
		String infoText = "";
		switch (type) {
		case SERVICE_CONNECTION:
			infoText = res.getString(R.string.service_connection_dialog_info);
			break;
		case SHARING_DATABOXES:
			infoText = res.getString(R.string.sharing_dialog_select_databoxes_info);
			break;
		case SHARING_GROUPS:
			infoText = res.getString(R.string.sharing_dialog_select_group_info);
			break;
		case SHARING_LIVEPOSTS:
			infoText = res.getString(R.string.sharing_dialog_select_liveposts_info);
			break;
		case SHARING_PERSONS:
			infoText = res.getString(R.string.sharing_dialog_select_persons_info);
			break;
		case SHARING_PROFILE:
			infoText = res.getString(R.string.sharing_dialog_select_profile_info);
			break;
		case SHARING_RESOURCES:
			infoText = res.getString(R.string.sharing_dialog_select_resources_info);
			break;
		case ADD_RESOURCES_TO_DATABOX : case ASSIGN_RESOURCES_TO_DATABOX:
			infoText = res.getString(R.string.dialog_add_resources_to_databox_info);
			break;
		case ADD_PEOPLE_TO_GROUP : case ASSIGN_PEOPLE_TO_GROUP:
			infoText = res.getString(R.string.dialog_add_people_to_group_info);
			break;
		default:
			break;
		}
		return infoText;
	}
	
	private static int getDrawableId(DisplayableItem di) {
		int id = 0;
		double value = AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(di);
		switch (di.getMType()) {
		case GROUP:
			if (value < 1) {
				id = R.drawable.icon_color_group_trust_low;
			} else if (value < 2) {
				id = R.drawable.icon_color_group_trust_medium;
			} else {
				id = R.drawable.icon_color_group_trust_high;
			}
			break;
		case RESOURCE:
			if (value < 1) {
				id = R.drawable.icon_color_data_resource_privacy_low;
			} else if (value < 2) {
				id = R.drawable.icon_color_data_resource_privacy_medium;
			} else {
				id = R.drawable.icon_color_data_resource_privacy_high;
			}
			break;
		case DATABOX:
			if (value < 1) {
				id = R.drawable.icon_color_databox_privacy_low;
			} else if (value < 2) {
				id = R.drawable.icon_color_databox_privacy_medium;
			} else {
				id = R.drawable.icon_color_databox_privacy_high;
			}
			break;
		case PERSON:
			if (value < 1) {
				id = R.drawable.icon_color_person_trust_low;
			} else if (value < 2) {
				id = R.drawable.icon_color_person_trust_medium;
			} else {
				id = R.drawable.icon_color_person_trust_high;
			}
			break;
		case LIVEPOST:
			id = R.drawable.icon_black_communication;
			break;
		default:
			break;
		}
		return id;
	}
	
	private static int getWarningColor(Context context, DisplayableItem di) {
		int resId = 0;
		int level = AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(di);
		if (level <= 0) {
			if(ModelHelper.isAgent(di.getMType())) resId = context.getResources().getColor(R.color.warning_level_low);
			else resId = context.getResources().getColor(R.color.warning_level_high);
		} else if (level <= 1) {
			resId = context.getResources().getColor(R.color.warning_level_neutral);
		} else if (level <= 2) {
			if(ModelHelper.isAgent(di.getMType())) resId = context.getResources().getColor(R.color.warning_level_high);
			else resId = context.getResources().getColor(R.color.warning_level_low);
		}
		return resId;
	}
	
	public static String getWarningText(DisplayableItem di) {
		return getWarningText(AndroidModelHelper.getTrustOrPrivacyLevelForDisplayableItem(di));
	}
	
	private static String getWarningText(int level) {
		String s = "";
		if (level <= 0) {
			s = "low";
		} else if (level <= 1) {
			s = "medium";
		} else if (level <= 2) {
			s ="high";
		}
		return s;
	}

	public static int getResourceId(Resources res, String name) {
		int resId = R.drawable.action_place;
		// Action icons
		if (name.equals(res.getString(R.string.action_addPeopleToGroup)) || name.equals(res.getString(R.string.action_addPeopleToGroupDetail))) {
			resId = R.drawable.action_assign_to_group;
		} else if (name.equals(res.getString(R.string.action_removeSelectedPeopleFromGroup))) {
			resId = R.drawable.action_remove_person;
		} else if (name.equals(res.getString(R.string.action_renameGroup))) {
			resId = R.drawable.action_rename_group;
		} else if (name.equals(res.getString(R.string.action_mergeSelection))) {
			resId = R.drawable.action_merge;
		} else if (name.equals(res.getString(R.string.action_share)) || name.equals(res.getString(R.string.action_shareSelectedItems)) || name.equals(res.getString(R.string.action_unshare))) {
			resId = R.drawable.action_share;
		} else if (name.equals(res.getString(R.string.action_saveAsNewGroup))) {
			resId = R.drawable.action_save_as_new_group;
		} else if (name.equals(res.getString(R.string.action_addNewGroup))) {
			resId = R.drawable.action_add_group;
		} else if (name.equals(res.getString(R.string.action_deleteSelectedGroups))) {
			resId = R.drawable.action_delete_group;
		} else if (name.equals(res.getString(R.string.action_searchPublicResolverService))) {
			resId = R.drawable.action_search_public;
		} else if (name.equals(res.getString(R.string.action_importSpecificContactFromPhoneBook))) {
			resId = R.drawable.action_import_person;
		} else if (name.equals(res.getString(R.string.action_importAllContactsFromPhoneBook))) {
			resId = R.drawable.action_import_all;
		} else if (name.equals(res.getString(R.string.action_addToNewGroup))) {
			resId = R.drawable.action_add_group;
		} else if (name.equals(res.getString(R.string.action_uploadImage))) {
			resId = R.drawable.action_upload_picture;
		} else if (name.equals(res.getString(R.string.action_assignToNewDatabox))) {
			resId = R.drawable.action_add_new_databox;
		} else if (name.equals(res.getString(R.string.action_assignToDatabox))) {
			resId = R.drawable.action_assign_to_databox;
		} else if (name.equals(res.getString(R.string.action_addNewDatabox))) {
			resId = R.drawable.action_add_new_databox;
		} else if (name.equals(res.getString(R.string.action_deleteSelectedDataboxes))) {
			resId = R.drawable.action_delete_databox;
		} else if (name.equals(res.getString(R.string.action_addResourcesToDatabox))) {
			resId = R.drawable.action_assign_to_databox;
		} else if (name.equals(res.getString(R.string.action_removeResourcesFromDatabox))) {
			resId = R.drawable.action_delete_resource;
		} else if (name.equals(res.getString(R.string.action_newMessage))) {
			resId = R.drawable.action_add_new_message;
		} else if (name.equals(res.getString(R.string.action_newProfile))) {
			resId = R.drawable.action_add_profilecard;
		} else if (name.equals(res.getString(R.string.action_deleteSelectedProfiles))) {
			resId = R.drawable.action_delete_profilecard;
		} else if (name.equals(res.getString(R.string.action_addExistingAttribute))) {
			resId = R.drawable.action_add_existing_attribute;
		} else if (name.equals(res.getString(R.string.action_addNewAttribute))) {
			resId = R.drawable.action_add_new_attribute;
		} else if (name.equals(res.getString(R.string.action_deleteSelectedAttributeCategories))) {
			resId = R.drawable.action_delete_attribute;
		} else if (name.equals(res.getString(R.string.action_exit))) {
			resId = R.drawable.action_exit;
		} else if (name.equals(res.getString(R.string.action_logout))) {
			resId = R.drawable.action_logout;
		} else if (name.equals(res.getString(R.string.action_deleteSituation))) {
			resId = R.drawable.action_delete_situation;
		} else if (name.equals(res.getString(R.string.action_newSituation))) {
			resId = R.drawable.action_add_situation;
		} else if (name.equals(res.getString(R.string.action_setCurrentPositionTag))) {
			resId = R.drawable.action_set_current_position;
		} else if (name.equals(res.getString(R.string.action_removeCurrentPositionTag))) {
			resId = R.drawable.action_remove_current_position;
		} else if (name.equals(res.getString(R.string.action_setFavouriteTag))) {
			resId = R.drawable.action_set_favorite;
		} else if (name.equals(res.getString(R.string.action_removeFavouriteTag))) {
			resId = R.drawable.action_remove_favorite;
		} else if (name.equals(res.getString(R.string.action_removeAllNotifications))) {
			resId = R.drawable.action_delete_all_notifications;
		} else if (name.equals(res.getString(R.string.action_removeSelectedNotifications))) {
			resId = R.drawable.action_delete_notification;
		} else if (name.equals(res.getString(R.string.action_connectServiceAdapter))) {
			resId = R.drawable.action_add_situation;
		} else if (name.equals(res.getString(R.string.action_disconnectServiceAdapter))) {
			resId = R.drawable.action_delete_situation;
		} else if (name.equals(res.getString(R.string.action_editItem))) {
			resId = R.drawable.action_edit_item;
		} else if (name.equals(res.getString(R.string.action_removeSelectedResources))) {
			resId = R.drawable.action_delete_resource;
		} else if (name.equals(res.getString(R.string.action_uploadFile))) {
			resId = R.drawable.action_add_resources;
		}
		// Tab icons
		else if (name.equals("All people")) {
			resId = R.drawable.icon_black_person;
		} else if (name.equals("Groups")) {
			resId = R.drawable.icon_black_group;
		}
		return resId;
	}

}
