package com.e2group.farhang.Adapter;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.e2group.farhang.Model.App;
import com.e2group.farhang.Model.Word;
import com.e2group.farhang.Model.WordDatabase;
import com.e2group.farhang.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

//класс PagedListAdapter для загрузки элементов в RecyclerView
public class WordAdapter extends PagedListAdapter<Word, RecyclerView.ViewHolder> {
    private Context context;
    private ClipboardManager clipboardManager;
    private WordDatabase wordDatabase;
    private View view;
    private static final int DEFAULT_VIEW_TYPE = 0;
    private static final int NATIVE_AD_VIEW_TYPE = 1;
    public static final int ITEM_PER_AD = 10;

    public WordAdapter(Context context) {
        super(callback);
        this.context = context;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        ConnectionDetector connectionDetector = new ConnectionDetector(context);
        if (position>1 && position %ITEM_PER_AD ==0 && connectionDetector.isConnected()){
            return NATIVE_AD_VIEW_TYPE;
        }else
        return DEFAULT_VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        switch (viewType) {
            default:
                view = layoutInflater.inflate(R.layout.word_item, parent, false);
                return new SearchViewHolder(view);
            case NATIVE_AD_VIEW_TYPE:
                view = layoutInflater.inflate(R.layout.banner_item, parent,false);
                return new BannerAddViewHolder(view);

        }
        }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       wordDatabase = App.getInstance().getWordDatabase();
        Word w = getItem(position);
        if (w != null && (holder.getItemViewType() ==DEFAULT_VIEW_TYPE)){
            ((SearchViewHolder) holder).onBindSearch();
        }
    }

     class SearchViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWord, tvTextDefinitionWord;
        public CardView cardView;
        public ImageView ivCopyBottom, ivShareBottom, ivLikeBottom, ivCopy;
        public View bottomSheetView;
        public BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context,R.style.BottomSheetDialogTheme);
        public TextView tvDescriptionDialog, tvWordDialog;
        public Button btnClose;

        public SearchViewHolder(View itemView) {
            super(itemView);
            bottomSheetView = LayoutInflater.from(context).inflate(
                    R.layout.layout_bottom_sheet,
                    (LinearLayout)itemView.
                            findViewById(R.id.bottom_sheet_container)
            );
            ivCopy = itemView.findViewById(R.id.btn_copy);
            tvWord = itemView.findViewById(R.id.tvWord);
            tvTextDefinitionWord = itemView.findViewById(R.id.text_definition_word);
            cardView = itemView.findViewById(R.id.cardViewWord);
            ivCopyBottom = bottomSheetView.findViewById(R.id.ivCopyDialogBot);
            ivShareBottom = bottomSheetView.findViewById(R.id.ivShareDialogBot);
            ivLikeBottom = bottomSheetView.findViewById(R.id.ivLikeDialogBot);
            btnClose = bottomSheetView.findViewById(R.id.btnCloseBot);
            tvWordDialog = bottomSheetView.findViewById(R.id.tvWordDialog);
            tvDescriptionDialog = bottomSheetView.findViewById(R.id.tvDescriptionDialog);

        }

        public void onBindSearch(){
            tvWord.setText(getCurrentList().get(getLayoutPosition()).getWords().toLowerCase());
            tvTextDefinitionWord.setText(getCurrentList().get(getLayoutPosition()).getValue().toLowerCase());
            cardView.setOnClickListener(view -> {

                bottomSheetDialog.setContentView(bottomSheetView);
                tvWordDialog.setText(getCurrentList().get(getLayoutPosition()).getWords().toLowerCase());
                tvDescriptionDialog.setText(getCurrentList().get(getLayoutPosition()).getValue());

                bottomSheetDialog.show();
            });

            btnClose.setOnClickListener(view1 -> {

                bottomSheetDialog.dismiss();
            });
            ivCopyBottom.setOnClickListener(view -> {
                copyT(getCurrentList().get(getLayoutPosition()).getValue());
            });

            ivCopy.setOnClickListener(view -> {
                copyText(getCurrentList().get(getLayoutPosition()).getValue(),view);
            });

            ivShareBottom.setOnClickListener(view -> {
                shareData(getCurrentList().get(getLayoutPosition()).getValue());
            });

            ivLikeBottom.setOnClickListener(view -> {
                Word wordT = getCurrentList().get(getLayoutPosition());
                if (wordT.getFavorite()==0){
                    wordT.setFavorite(1);
                } else {
                    wordT.setFavorite(0);

                }

                if (getItem(getLayoutPosition()).getFavorite()==0){
                    ivLikeBottom.setImageResource(R.drawable.no_star);
                }else {
                    ivLikeBottom.setImageResource(R.drawable.star);
                }
                notifyDataSetChanged();
                wordDatabase.wordDao().updateFavList(wordT);
            });

            if (getItem(getLayoutPosition()).getFavorite()==0){
                ivLikeBottom.setImageResource(R.drawable.no_star);
            }else {
                ivLikeBottom.setImageResource(R.drawable.star);
            }
        }
    }

    public  class BannerAddViewHolder extends RecyclerView.ViewHolder {
        private AdView adView;
        private CardView addCard;
        public BannerAddViewHolder(@NonNull View itemView) {
            super(itemView);
            MobileAds.initialize(context, "ca-app-pub-4838759025427461~5486751859");
            adView = (AdView) itemView.findViewById(R.id.adViewItemBanner);
            addCard = (CardView) itemView.findViewById(R.id.cardViewAdd);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        }
    }


    static DiffUtil.ItemCallback<Word> callback = new DiffUtil.ItemCallback<Word>() {
        @Override
        public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
            return oldItem.getWords().equals(newItem.getWords()) &&
                    oldItem.getValue().equals(newItem.getValue());
        }
    };

    public void shareData(String data){
            final String appPackageName = context.getPackageName();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, data+"Farhang TJ" +"\n"+"https://play.google.com/store/apps/details?id=" + appPackageName);
            sendIntent.setType("text/plain");
            context.startActivity(Intent.createChooser(sendIntent,"Поделиться"));
    }

    public void copyText (String data, View view){
        String text = data;
        if (! text.isEmpty()){
            ClipData clipData = ClipData.newPlainText("text",text);
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);
            Snackbar.make(view, "Скопирован в буфер обмена.", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void copyT(String text){
        if (! text.isEmpty()){
            ClipData clipData = ClipData.newPlainText("text",text);
            clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Скопирован в буфер обмена.", Toast.LENGTH_SHORT).show();
        }
    }

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
