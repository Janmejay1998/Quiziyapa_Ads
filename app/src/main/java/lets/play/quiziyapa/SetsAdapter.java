package lets.play.quiziyapa;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class SetsAdapter extends BaseAdapter {

    private int noOfSets ;
    LottieAnimationView star;

    public SetsAdapter(int noOfSets) {
        this.noOfSets = noOfSets;
    }

    @Override
    public int getCount() {
        return noOfSets;
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

        View view;
        if(convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sets_item,parent,false);
        }
        else {
            view = convertView;
        }

        star = view.findViewById(R.id.star);
        star.playAnimation();

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(parent.getContext(), QuestionActivity.class);
                intent.putExtra("SETNO",position + 1);
                parent.getContext().startActivity(intent);
            }
        });

        view.animate().rotationBy(360).setDuration(3000).start();
        view.setScaleX((float) 1.5);
        view.setScaleY((float) 1.5);

        ((TextView) view.findViewById(R.id.sets_no)).setText(String.valueOf(position + 1));

        return view;
    }
}
