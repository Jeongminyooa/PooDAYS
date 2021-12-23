package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ToiletAdapter extends CursorAdapter {
    private int layout;
    private LayoutInflater inflater;

    public ToiletAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.layout = layout;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = inflater.inflate(layout, viewGroup, false);

        ViewHolder holder = new ViewHolder();
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if(holder.tvBookMarkName == null) {
            holder.tvBookMarkName = view.findViewById(R.id.tvBookMarkName1);
            holder.tvBookMarkAddress = view.findViewById(R.id.tvBookMarkAddress1);
               holder.ivBookMarkImage = view.findViewById(R.id.ivBookMarkImage);
        }
        holder.tvBookMarkName.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_NAME)));
        holder.tvBookMarkAddress.setText(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_ADDRESS)));
       // 사진 저장 부분
        holder.ivBookMarkImage.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndexOrThrow(ToiletDBHelper.COL_IMAGE))));

    }
    class ViewHolder {
        TextView tvBookMarkName;
        TextView tvBookMarkAddress;
        ImageView ivBookMarkImage;
        public ViewHolder() {
            tvBookMarkName = null;
            tvBookMarkAddress = null;

            ivBookMarkImage = null;
        }

    }
}
