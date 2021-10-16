package com.e2group.farhang.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;

//класс PagedListAdapter для загрузки элементов в RecyclerView
public class FavoriteAdapter extends PagedListAdapter<Word, FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private WordDatabase wordDatabase;


    public FavoriteAdapter(Context context) {
        super(callback);
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favoirte_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        wordDatabase = App.getInstance().getWordDatabase();
        Word w = getItem(position);
        if (getItem(position) != null) {
            holder.onBind(w);
        }
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        public TextView tvWordDelete;
        public CardView cardView;
        public ImageView ivDeleteItem;
        public View bottomSheetView;
        public BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.BottomSheetDialogTheme);
        public TextView tvDescription;
        public Button btnClose;

        public FavoriteViewHolder(View itemView) {
            super(itemView);
            bottomSheetView = LayoutInflater.from(context).inflate(
                    R.layout.layout_bottom_sheet_favorite, (LinearLayout) itemView.findViewById(R.id.bottom_sheet_container_fav)
            );
            ivDeleteItem = itemView.findViewById(R.id.ivDeleteItem);
            tvWordDelete = itemView.findViewById(R.id.tvFavoriteWord);
            cardView = itemView.findViewById(R.id.cardView);
            btnClose = bottomSheetView.findViewById(R.id.btnCloseBotFav);
            tvDescription = bottomSheetView.findViewById(R.id.tvDescriptionDialogFav);
        }

        public void onBind(Word word) {
            tvWordDelete.setText(getCurrentList().get(getLayoutPosition()).getWords().toLowerCase());
            cardView.setOnClickListener(view -> {
                tvDescription.setText(getCurrentList().get(getLayoutPosition()).getValue());
                bottomSheetDialog.show();
            });

            ivDeleteItem.setOnClickListener(view -> {

                Word wd = getCurrentList().get(getLayoutPosition());
                wd.setFavorite(0);
                wordDatabase.wordDao().updateFavList(wd);
                notifyDataSetChanged();
            });

            btnClose.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
            });
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

    public void shareData(String data) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, data + R.string.url);
        sendIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(sendIntent, "Поделиться"));
    }
}
