/*
* Copyright 2013 by the digital.me project (http:\\www.dime-project.eu).
*
* Licensed under the EUPL, Version 1.1 only (the "Licence");
* You may not use this work except in compliance with the Licence.
* You may obtain a copy of the Licence at:
*
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl
*
* Unless required by applicable law or agreed to in writing, software distributed under the Licence is distributed on an "AS IS" basis,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the Licence for the specific language governing permissions and limitations under the Licence.
*/

package eu.dime.mobile.view.data;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import eu.dime.control.LoadingViewHandler;
import eu.dime.mobile.R;
import eu.dime.mobile.helper.AndroidModelHelper;
import eu.dime.mobile.helper.UIHelper;
import eu.dime.mobile.helper.handler.LoadingViewHandlerFactory;
import eu.dime.mobile.helper.objects.DimeTabObject;
import eu.dime.mobile.helper.objects.ResultObject;
import eu.dime.mobile.helper.objects.ResultObjectDisplayable;
import eu.dime.mobile.helper.objects.IResultOfStandardDialog;
import eu.dime.mobile.view.abstr.TabActivityDisplayableItemDetail;
import eu.dime.mobile.view.adapter.BaseAdapter_Standard;
import eu.dime.model.GenItem;
import eu.dime.model.ModelHelper;
import eu.dime.model.displayable.DataboxItem;
import eu.dime.model.displayable.DisplayableItem;

import java.util.ArrayList;
import java.util.List;

public class TabActivity_Databox_Detail extends TabActivityDisplayableItemDetail implements IResultOfStandardDialog {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = TabActivity_Databox_Detail.class.getSimpleName();
	}

	@Override
	protected void initializeTabs() {
    	tabs.add(new DimeTabObject(getResources().getString(R.string.tab_databoxDetail) + di.getName(), ListActivity_Databox_Detail.class, dio));
    	TabActivity_Databox_Detail.this.init(true, true, isOwnItem(), isOwnItem());
	}
	
	@Override
	protected List<String> getActionsForDetailView() {
		List<String> actions = new ArrayList<String>();
		actions.add(getString(R.string.action_addResourcesToDatabox));
		actions.add(getString(R.string.action_removeResourcesFromDatabox));
		actions.add(getString(R.string.action_shareSelectedItems));
		return actions;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		super.onClick(v);
		if (v instanceof Button) {
			Button button = (Button) v;
			Resources res = getResources();
			//add resources to databox
			if (button.getText().equals(res.getString(R.string.action_addResourcesToDatabox))) {
				actionDialog.dismiss();
				UIHelper.createStandardDialog(TabActivity_Databox_Detail.this, mrContext, new BaseAdapter_Standard(), (List<DisplayableItem>) (Object) AndroidModelHelper.removeListItemsByMeansOfItems((List<GenItem>) (Object) ModelHelper.getAllResources(mrContext), (List<GenItem>) (Object) ModelHelper.getResourcesOfDatabox(mrContext, (DataboxItem) di)), ResultObject.RESULT_OBJECT_TYPES.ADD_RESOURCES_TO_DATABOX);
			}
			//remove resources from databox
			else if (button.getText().equals(res.getString(R.string.action_removeResourcesFromDatabox))) {
				final String actionName = res.getResourceEntryName(R.string.action_removeResourcesFromDatabox);
				actionDialog.dismiss();
				di.removeItems(selectedGUIDs);
				AndroidModelHelper.updateGenItemAsynchronously(di, null, currentActivity, mrContext, actionName);
			}
			//share
			else if (button.getText().equals(res.getString(R.string.action_shareSelectedItems))) {
				actionDialog.dismiss();
				AndroidModelHelper.shareResources(this, new ArrayList<String>(selectedGUIDs), ModelHelper.getChildType(dio.getItemType()));
			}
		}
	}

	@Override
	protected LoadingViewHandler createLoadingViewHandler() {
		return LoadingViewHandlerFactory.<TabActivity_Databox_Detail> createLVH(TabActivity_Databox_Detail.this);
	}

	@Override
	public void handleResult(ResultObject result) {
		final String actionName = getResources().getResourceEntryName(R.string.action_addResourcesToDatabox);
		di.addItems(AndroidModelHelper.getListOfGuidsOfDisplayableList(((ResultObjectDisplayable)result).getDisplayables()));
		AndroidModelHelper.updateGenItemAsynchronously(di, null, currentActivity, mrContext, actionName);
	}

	@Override
	protected void initializeHeader() {
		ViewGroup headerContainer = (ViewGroup) findViewById(R.tabframe.header);
		LinearLayout header = null;
		if(headerContainer.getChildCount() > 0) {
			header = (LinearLayout) headerContainer.getChildAt(0);
		} else {
			header = (LinearLayout) getLayoutInflater().inflate(R.layout.header_standard, headerContainer);
		}
		UIHelper.inflateStandardHeader(this, di, mrContext, header);
	}

}