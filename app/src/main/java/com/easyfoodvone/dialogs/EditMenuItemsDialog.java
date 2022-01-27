package com.easyfoodvone.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.easyfoodvone.R;
import com.easyfoodvone.api_handler.ApiClient;
import com.easyfoodvone.api_handler.ApiInterface;
import com.easyfoodvone.app_common.ws.CommonResponse;
import com.easyfoodvone.app_common.ws.MenuCategoryList;
import com.easyfoodvone.menu_details.adapter.EditMenuProductModifierAdapter;
import com.easyfoodvone.menu_details.adapter.EditMenuProductSizeAdapter;
import com.easyfoodvone.models.MenuProductDetails;
import com.easyfoodvone.utility.LoadingDialog;
import com.easyfoodvone.utility.PrefManager;
import com.easyfoodvone.utility.UserPreferences;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.easyfoodvone.utility.UserContants.AUTH_TOKEN;


public class EditMenuItemsDialog extends Fragment {

    public interface ParentInterface {
        void close();
        void onItemUpdated();
    }

    private final @NonNull ParentInterface parentInterface;
    private final @NonNull Context context;
    private final @NonNull PrefManager prefManager;
    private final @NonNull MenuProductDetails menuDetail;
    private final @NonNull MenuCategoryList.MenuCategories menuCategories;
    private final boolean isPhone;

    private TextView menuCatName;
    private EditText itemName;
    private TextView price;
    private Button btn_ok, btn_cancel;
    private RecyclerView sizeList, itemModifierList;
    private TextView txt, txt1;

    public EditMenuItemsDialog(
            @NonNull Context context,
            @NonNull PrefManager prefManager,
            @NonNull ParentInterface parentInterface,
            @NonNull MenuProductDetails menuDetail,
            @NonNull MenuCategoryList.MenuCategories menuCategories,
            boolean isPhone) {
        this.context = context;
        this.prefManager = prefManager;
        this.parentInterface = parentInterface;
        this.menuDetail = menuDetail;
        this.menuCategories = menuCategories;
        this.isPhone = isPhone;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        if (isPhone) {
            view = inflater.inflate(R.layout.popup_menu_edit_product_detail_phone, container, false);
        } else {
            view = inflater.inflate(R.layout.popup_menu_edit_product_detail, container, false);
        }

        menuCatName = view.findViewById(R.id.menuCatName);
        txt = view.findViewById(R.id.txt);
        txt1 = view.findViewById(R.id.txt1);
        sizeList = view.findViewById(R.id.sizeList);
        sizeList.setLayoutManager(new LinearLayoutManager(context));
        itemModifierList = view.findViewById(R.id.itemModifireList);
        itemModifierList.setLayoutManager(new LinearLayoutManager(context));

        if (menuDetail.getData().getMenu_product_size() != null
                && menuDetail.getData().getMenu_product_size().size() > 0) {
            sizeList.setAdapter(new EditMenuProductSizeAdapter(context, menuDetail, isPhone));
        } else {
            txt.setVisibility(View.GONE);
        }

        if (menuDetail.getData().getProduct_modifiers() != null
                && menuDetail.getData().getProduct_modifiers().size() > 0) {
            itemModifierList.setAdapter(new EditMenuProductModifierAdapter(context, menuDetail, isPhone));
        } else {
            txt1.setVisibility(View.GONE);
        }

        btn_ok = view.findViewById(R.id.btn_ok);
        btn_cancel = view.findViewById(R.id.btn_cancel);
        menuCatName.setText(menuCategories.getMenu_category_name());
        itemName = view.findViewById(R.id.itemName);
        itemName.setText(menuDetail.getData().getProduct_name());
        price = view.findViewById(R.id.price);
        price.setText(menuDetail.getData().getProduct_price());

        price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                menuDetail.getData().setProduct_price(s.toString());
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentInterface.close();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMenuItemDetail(menuDetail);
            }
        });

        return view;
    }

    private void saveMenuItemDetail(MenuProductDetails menuDetail) {
        Log.e("Updating    ---------- ", menuDetail.toString());

        final LoadingDialog dialog = new LoadingDialog(context, "");
        dialog.setCancelable(false);
        dialog.show();

        try {
            final String restaurantId = UserPreferences.get().getLoggedInResponse(context).getRestaurant_id();
            final String authToken = prefManager.getPreference(AUTH_TOKEN, "");
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

            MenuProductDetails commonRequest = new MenuProductDetails();
            commonRequest.setMenu_product_id(menuDetail.getData().getId());
            commonRequest.setRestaurant_id(restaurantId);
            commonRequest.setData(menuDetail.getData());

            CompositeDisposable disposable = new CompositeDisposable();
            disposable.add(apiService.saveMenuDetails(authToken, commonRequest)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<CommonResponse>() {
                        @Override
                        public void onSuccess(CommonResponse data) {
                            dialog.dismiss();

                            if (data.isSuccess()) {
                                Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show();

                                parentInterface.close();
                                parentInterface.onItemUpdated();

                            } else {
                                Toast.makeText(context, "Unable to save item settings", Toast.LENGTH_SHORT).show();
                                Log.e("Error  ----- ", data.getMessage());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            dialog.dismiss();

                            e.printStackTrace();

                            Toast.makeText(context, "Unable to save item settings", Toast.LENGTH_SHORT).show();
                        }
                    }));

        } catch (Exception e) {
            dialog.dismiss();

            e.printStackTrace();

            Toast.makeText(context, "Cannot save item settings", Toast.LENGTH_SHORT).show();
        }
    }
}
