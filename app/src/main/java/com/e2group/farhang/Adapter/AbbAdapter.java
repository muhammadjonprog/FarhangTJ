package com.e2group.farhang.Adapter;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.e2group.farhang.Model.Abbreavetura;
import com.e2group.farhang.Model.Word;
import com.e2group.farhang.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.snackbar.Snackbar;

//класс PagedListAdapter для загрузки элементов в RecyclerView
public class AbbAdapter extends PagedListAdapter<Abbreavetura, RecyclerView.ViewHolder> {
    private Context context;
    private ClipboardManager clipboardManager ;
    private View view;
    private static final int DEFAULT_VIEW_TYPE = 0;
    private static final int NATIVE_AD_VIEW_TYPE = 1;
    public static final int ITEM_PER_AD = 10;
    public AbbAdapter(Context context) {
        super(callback);
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        if (position>1 && position %ITEM_PER_AD ==0 && connectionDetector.isConnected()){
            return NATIVE_AD_VIEW_TYPE;
        }
        return DEFAULT_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            default:
                view = layoutInflater.inflate(R.layout.abbr_item, parent, false);
                return new AbbViewHolder(view);
            case NATIVE_AD_VIEW_TYPE:
                view = layoutInflater.inflate(R.layout.banner_item, parent,false);
                return new NativeAdViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Abbreavetura w = getItem(position);
        if (w != null && (holder.getItemViewType() ==DEFAULT_VIEW_TYPE)){
            ((AbbViewHolder) holder).onBind();
        }
       
}

    class AbbViewHolder extends RecyclerView.ViewHolder {
            public TextView tvWord;
            public ImageView ivCopy;
            public AbbViewHolder(View itemView) {
                super(itemView);
                ivCopy = itemView.findViewById(R.id.ivCopyRowAbb);
                tvWord = itemView.findViewById(R.id.tvAbbv);
            }

            public void onBind(){
                tvWord.setText(getCurrentList().get(getLayoutPosition()).getWord());
                ivCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String data = getCurrentList().get(getLayoutPosition()).getWord();
                        if (!data.isEmpty()) {
                            ClipData clipData = ClipData.newPlainText("text", data);
                            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                            clipboardManager.setPrimaryClip(clipData);
                            Snackbar.make(view, "Скопирован в буфер обмена.", Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    }
                });
            }
        }

    public class NativeAdViewHolder extends RecyclerView.ViewHolder {
        private AdView adView;
        private CardView cardView;
        public NativeAdViewHolder(View view) {
            super(view);
            MobileAds.initialize(context, "ca-app-pub-4838759025427461~5486751859");
            adView = (AdView) view.findViewById(R.id.adViewItemBanner);
            cardView = (CardView) view.findViewById(R.id.cardViewAdd);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }

    static DiffUtil.ItemCallback<Abbreavetura> callback = new DiffUtil.ItemCallback<Abbreavetura>() {
        @Override
        public boolean areItemsTheSame(@NonNull Abbreavetura oldItem, @NonNull Abbreavetura newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Abbreavetura oldItem, @NonNull Abbreavetura newItem) {
            return oldItem.getWord().equals(newItem.getWord());

        }
    };

    private  class  ConnectionDetector {
        Context context;
        ConnectionDetector(Context context) {
            this.context = context;
        }

        boolean isConnected(){
            ConnectivityManager connectivity = (ConnectivityManager)
                    context.getSystemService(Service.CONNECTIVITY_SERVICE);
            if (connectivity != null){
                NetworkInfo info = connectivity.getActiveNetworkInfo();
                if (info != null){
                    if (info.getState() == NetworkInfo.State.CONNECTED){
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
