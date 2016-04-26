package com.itmindco.dlnaplayervr.UpnpService;

import com.itmindco.dlnaplayervr.Models.ItemModel;

import org.fourthline.cling.model.action.ActionException;
import org.fourthline.cling.model.action.ActionInvocation;
import org.fourthline.cling.model.message.UpnpResponse;
import org.fourthline.cling.model.meta.Service;
import org.fourthline.cling.model.types.ErrorCode;
import org.fourthline.cling.support.contentdirectory.callback.Browse;
import org.fourthline.cling.support.model.BrowseFlag;
import org.fourthline.cling.support.model.DIDLContent;
import org.fourthline.cling.support.model.DIDLObject;
import org.fourthline.cling.support.model.SortCriterion;
import org.fourthline.cling.support.model.container.Container;
import org.fourthline.cling.support.model.item.Item;

import java.util.ArrayList;

public class CustomContentBrowseActionCallback extends Browse {
    private Service service;

    public CustomContentBrowseActionCallback(Service service, String id) {
        super(service, id, BrowseFlag.DIRECT_CHILDREN, "*", 0, null,
                new SortCriterion(true, "dc:title"));

        this.service = service;

        //if (mCallbacks != null)
        //    mCallbacks.onDisplayDirectories();
    }

    private ItemModel createItemModel(DIDLObject item) {

//        ItemModel itemModel = new ItemModel(getResources(),
//                R.drawable.ic_folder, service, item);
//
//        URI usableIcon = item.getFirstPropertyValue(DIDLObject.Property.UPNP.ICON.class);
//        if (usableIcon != null)
//            itemModel.setIconUrl(usableIcon.toString());
//
//        if (item instanceof Item) {
//            itemModel.setIcon(R.drawable.ic_file);
//
//            SharedPreferences prefs =
//                    PreferenceManager.getDefaultSharedPreferences(mActivity);
//
//            if (prefs.getBoolean("settings_hide_file_icons", false))
//                itemModel.setHideIcon(true);
//
//            if (prefs.getBoolean("settings_show_extensions", false))
//                itemModel.setShowExtension(true);
//        }

        return null;
    }

    @Override
    public void received(final ActionInvocation actionInvocation, final DIDLContent didl) {

        ArrayList<ItemModel> items = new ArrayList<ItemModel>();

        try {
            for (Container childContainer : didl.getContainers())
                items.add(createItemModel(childContainer));

            for (Item childItem : didl.getItems())
                items.add(createItemModel(childItem));

            //if (mCallbacks != null)
            //    mCallbacks.onDisplayItems(items);

        } catch (Exception ex) {
            actionInvocation.setFailure(new ActionException(
                    ErrorCode.ACTION_FAILED,
                    "Can't create list childs: " + ex, ex));
            failure(actionInvocation, null, ex.getMessage());
        }
    }

    @Override
    public void updateStatus(Status status) {

    }

    @Override
    public void failure(ActionInvocation invocation, UpnpResponse response, String s) {
        //if (mCallbacks != null)
        //    mCallbacks.onDisplayItemsError(createDefaultFailureMessage(invocation, response));
    }
}
