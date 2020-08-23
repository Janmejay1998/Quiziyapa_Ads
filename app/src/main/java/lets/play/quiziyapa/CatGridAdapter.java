package lets.play.quiziyapa;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CatGridAdapter extends BaseAdapter {
    private List<String> catList;

    public CatGridAdapter(List<String> catList) {
        this.catList = catList;
    }

    View view;
    @Override
    public int getCount() {
        return catList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        if(convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item,parent,false);
        }
        else {
            view = convertView;
        }

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(parent.getContext(), SetsActivity.class);
                        intent.putExtra("CATEGORY", catList.get(position));
                        intent.putExtra("CATEGORY_ID", position + 1);
                        parent.getContext().startActivity(intent);
                    }
                });


        ((TextView) view.findViewById(R.id.catName)).setText(catList.get(position));

        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#64379f"));
        colors.add(Color.parseColor("#95adbe"));
        colors.add(Color.parseColor("#574f7d"));
        colors.add(Color.parseColor("#503a65"));
        colors.add(Color.parseColor("#3c2a4d"));


        Random rnd = new Random();
        int color = colors.get(rnd.nextInt(colors.size()));
        ((CardView) view).setCardBackgroundColor(color);

        return view;
    }


}
