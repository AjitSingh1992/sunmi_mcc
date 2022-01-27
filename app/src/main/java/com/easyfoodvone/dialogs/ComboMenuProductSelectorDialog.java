package com.easyfoodvone.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.easyfoodvone.R;
import com.easyfoodvone.adapters.SelectedComboProducts;
import com.easyfoodvone.models.MenuProducts;

import java.util.List;

public class ComboMenuProductSelectorDialog extends Dialog {
    private final Context context;
    private final List<MenuProducts.Data> productList;
    private final OnDialogButtonClickListener onDialogButtonClickListener;
    private final boolean isPhone;

    private RecyclerView product_list;
    private SelectedComboProducts adapter;
    private Button btn_ok, btn_cancel;

    public interface OnDialogButtonClickListener {
        void onOkClick(String ids, List<MenuProducts.Data> selectedItemList);
        void onCancelClick();
    }

    public ComboMenuProductSelectorDialog(
            Context context,
            List<MenuProducts.Data> productList,
            OnDialogButtonClickListener onDialogButtonClickListener,
            boolean isPhone) {
        super(context);

        this.context = context;
        this.productList = productList;
        this.onDialogButtonClickListener = onDialogButtonClickListener;
        this.isPhone = isPhone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        setContentView(isPhone ? R.layout.popup_offers_combo_choose_product_phone : R.layout.popup_offers_combo_choose_product);

        product_list = findViewById(R.id.product_list);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);

        product_list.setLayoutManager(new LinearLayoutManager(context));
        adapter = new SelectedComboProducts(context, productList, isPhone);
        product_list.setAdapter(adapter);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                onDialogButtonClickListener.onCancelClick();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MenuProducts.Data> list = adapter.getSelectedItemList();
                onDialogButtonClickListener.onOkClick("", list);
                dismiss();
            }
        });
    }
}
