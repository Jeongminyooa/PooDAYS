package ddwucom.mobile.finalproject.ma02_20190980;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class InfoAdapter extends BaseAdapter {
    private int layout;
    private Context context;
    private ArrayList<BlogDTO> blogList;
    private LayoutInflater inflater;

    public InfoAdapter(Context context, int layout, ArrayList<BlogDTO> blogList) {
        this.layout = layout;
        this.blogList = blogList;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return blogList.size();
    }

    @Override
    public Object getItem(int i) {
        return blogList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        ViewHolder viewHolder;

        if(view == null) {
            view = inflater.inflate(layout, viewGroup, false);

            viewHolder = new ViewHolder();
            viewHolder.tvPostTitle = view.findViewById(R.id.tvPostTitle);
            viewHolder.tvPostDescr = view.findViewById(R.id.tvPostDescr);
            viewHolder.tvDate = view.findViewById(R.id.tvPostDate);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 키워드가 <b> 태그가 씌워져 있으므로 삭제해줌
        String title_without_keywordTag = blogList.get(pos).getTitle().replace("<b>", "");
        title_without_keywordTag = title_without_keywordTag.replace("</b>", "");

        String descr_without_keywordTag = blogList.get(pos).getDescription().replace("<b>", "");
        descr_without_keywordTag = descr_without_keywordTag.replace("</b>", "");

        viewHolder.tvPostTitle.setText(title_without_keywordTag);
        viewHolder.tvPostDescr.setText(descr_without_keywordTag);
        viewHolder.tvDate.setText(blogList.get(pos).getPostdate());

        return view;
    }


    class ViewHolder {
        TextView tvPostTitle;
        TextView tvPostDescr;
        TextView tvDate;
        public ViewHolder() {
            tvDate = null;
            tvPostTitle = null;
            tvPostDescr = null;
        }

    }
}
