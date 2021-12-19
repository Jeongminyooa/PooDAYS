package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PooAdapter extends CursorAdapter {
    private int layout;
    private LayoutInflater inflater;

    public PooAdapter(Context context, int layout, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        this.layout = layout;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = inflater.inflate(layout, parent, false);

        ViewHolder holder = new ViewHolder();
        view.setTag(holder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();

        if(holder.tvDate == null) {
            holder.tvDate = view.findViewById(R.id.tvDate);
            holder.tvIsPoo = view.findViewById(R.id.tvIsPoo);
            holder.ivBM = view.findViewById(R.id.ivBM);
        }
        holder.tvDate.setText(cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_DATE)));

        int isPoo = cursor.getInt(cursor.getColumnIndexOrThrow(PooDBHelper.COL_ISPOO));
        if(isPoo == 1) {
            holder.tvIsPoo.setText("배변 기록이 있습니다.");
            String bm = cursor.getString(cursor.getColumnIndexOrThrow(PooDBHelper.COL_BM));

            if(bm.equals(context.getResources().getString(R.string.bm_poo1))) {
                holder.ivBM.setImageResource(R.mipmap.poo1);
            } else if(bm.equals(context.getResources().getString(R.string.bm_poo2))) {
                holder.ivBM.setImageResource(R.mipmap.poo2);
            }else if (bm.equals(context.getResources().getString(R.string.bm_poo3))) {
                holder.ivBM.setImageResource(R.mipmap.poo3);
            } else {
                holder.ivBM.setImageResource(R.mipmap.poo4);
            }
        }
        else {
            holder.tvIsPoo.setText("배변 기록이 없습니다.");
            holder.ivBM.setImageResource(R.mipmap.no_poo);
        }
    }

    class ViewHolder {
        TextView tvDate;
        TextView tvIsPoo;
        ImageView ivBM;
        public ViewHolder() {
            tvDate = null;
            tvIsPoo = null;
            ivBM = null;
        }

    }
}
