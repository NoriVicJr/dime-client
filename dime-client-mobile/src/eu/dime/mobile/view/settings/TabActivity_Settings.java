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

package eu.dime.mobile.view.settings;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import eu.dime.control.LoadingViewHandler;
import eu.dime.mobile.R;
import eu.dime.mobile.helper.AndroidModelHelper;
import eu.dime.mobile.helper.DimeIntentObjectHelper;
import eu.dime.mobile.helper.UIHelper;
import eu.dime.mobile.helper.handler.LoadingViewHandlerFactory;
import eu.dime.mobile.helper.objects.DimeIntentObject;
import eu.dime.mobile.helper.objects.DimeTabObject;
import eu.dime.mobile.helper.objects.ResultObject;
import eu.dime.mobile.helper.objects.ResultObjectServiceAdapter;
import eu.dime.mobile.helper.objects.IResultOfStandardDialog;
import eu.dime.mobile.view.abstr.TabActivityDime;
import eu.dime.mobile.view.adapter.BaseAdapter_ServiceAdapter;
import eu.dime.mobile.view.dialog.Activity_Account_Configuration_Dialog;
import eu.dime.model.ModelHelper;
import eu.dime.model.TYPES;
import eu.dime.model.displayable.DisplayableItem;
import eu.dime.model.displayable.ServiceAdapterItem;
import java.util.Arrays;
import java.util.List;

public class TabActivity_Settings extends TabActivityDime implements IResultOfStandardDialog {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TAG = TabActivity_Settings.class.getSimpleName();
		tabs.add(new DimeTabObject(getResources().getString(R.string.tab_settingsGeneral), Activity_Settings_General.class, dio));
		tabs.add(new DimeTabObject(getResources().getString(R.string.tab_settingsServices), ListActivity_Settings_Services.class, new DimeIntentObject(TYPES.ACCOUNT)));
//		tabs.add(new DimeTabObject(getResources().getString(R.string.tab_settingsRules), Activity_Settings_Rules.class, dio));
		super.init(true, false, false, true);
	}

	@Override
	protected void onClickActionButton() {
		Resources res = getResources();
		//actions for Activity_Settings_General
		if(currentActivity instanceof Activity_Settings_General) {
//			String[] actionsForGeneralSettings = { "" };
//			actionDialog = UIHelper.createActionDialog(this, Arrays.asList(actionsForGeneralSettings), this, null);
//			actionDialog.show();
		}
		//actions for ListActivity_Settings_Services
		else if (currentActivity instanceof ListActivity_Settings_Services) {
			selectedGUIDs = ((ListActivity_Settings_Services) currentActivity).getSelectionGUIDS();
			String[] actionsForServiceAdapters = { res.getString(R.string.action_connectServiceAdapter), res.getString(R.string.action_disconnectServiceAdapter) };
			actionDialog = UIHelper.createActionDialog(this, Arrays.asList(actionsForServiceAdapters), this, selectedGUIDs);
			actionDialog.show();
		}
		//actions for Activity_Settings_Rules
		else if(currentActivity instanceof Activity_Settings_Rules) {
//			String[] actionsForRulesSettings = { "" };
//			actionDialog = UIHelper.createActionDialog(this, Arrays.asList(actionsForRulesSettings), this, null);
//			actionDialog.show();
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View view) {
		super.onClick(view);
        if (view instanceof Button) {
            Button button = (Button) view;
            Resources res = getResources();
            /**
             * ---------------------------------------------------------------------------------------------------------------------------
             * Actions for ListActivity_Settings_Services
             * ---------------------------------------------------------------------------------------------------------------------------
             */
            if (currentActivity instanceof ListActivity_Settings_Services) {
            	//connect service adapter
				if (button.getText().equals(res.getString(R.string.action_connectServiceAdapter))) {
					actionDialog.dismiss();
					UIHelper.createStandardDialog(TabActivity_Settings.this, mrContext, new BaseAdapter_ServiceAdapter(), (List<DisplayableItem>) (Object) ModelHelper.getAllServiceAdapters(mrContext), ResultObject.RESULT_OBJECT_TYPES.SERVICE_CONNECTION);
				}
				//disconnect selected service adapter
				else if (button.getText().equals(res.getString(R.string.action_disconnectServiceAdapter))) {
					final String actionName = res.getResourceEntryName(R.string.action_disconnectServiceAdapter);
					actionDialog.dismiss();
					AndroidModelHelper.deleteGenItemsAsynchronously(currentActivity, TYPES.ACCOUNT, mrContext, actionName);
				}
            }
        }
	}
	
	@Override
	protected LoadingViewHandler createLoadingViewHandler() {
		return LoadingViewHandlerFactory.<TabActivity_Settings>createLVH(TabActivity_Settings.this);
	}

	@Override
	public void handleResult(ResultObject result) {
		if(result instanceof ResultObjectServiceAdapter) {
			ServiceAdapterItem service = ((ResultObjectServiceAdapter)result).getServiceAdapter();
			if(service.getAuthUrl() != null && service.getAuthUrl().length() > 0) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(service.getAuthUrl()));
    			startActivity(browserIntent);
			} else {
				Intent myIntent = new Intent(TabActivity_Settings.this, Activity_Account_Configuration_Dialog.class);
		        startActivity(DimeIntentObjectHelper.populateIntent(myIntent, new DimeIntentObject(service)));
			}
		}
	}
	
}