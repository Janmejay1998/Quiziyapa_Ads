package lets.play.quiziyapa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import java.util.List;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private List<String> listdata;
    private String total;
    // RecyclerView recyclerView;
    public MyListAdapter(List <String> listdata, String total) {
        this.listdata = listdata;
        this.total = total;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(listdata.get(position) +"/"+ total);
        holder.lottieAnimationView.playAnimation();
        float m = (Float.parseFloat(listdata.get(position))/Float.parseFloat(total))*100;
        {
            if (m == 100) {
                holder.textView2.setText("EXCELLENT");
                holder.lottieAnimationView.setMinAndMaxFrame(11,46);
            }
            else if (m >= 90) {
                holder.textView2.setText("GOOD");
                holder.lottieAnimationView.setMinAndMaxFrame(11,37);
            }
            else if (m >= 50) {
                holder.textView2.setText("NOT BAD");
                holder.lottieAnimationView.setMinAndMaxFrame(11,28);
            }
            else {
                holder.textView2.setText("WORST");
                holder.lottieAnimationView.setMinAndMaxFrame(11,19);
            }

        }

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView,textView2;
        public RelativeLayout relativeLayout;
        public LottieAnimationView lottieAnimationView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView);
            this.textView2 = itemView.findViewById(R.id.textView2);
            lottieAnimationView = itemView.findViewById(R.id.ratingStar);
            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}