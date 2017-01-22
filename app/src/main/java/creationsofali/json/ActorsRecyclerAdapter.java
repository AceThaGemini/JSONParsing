package creationsofali.json;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by ali on 1/22/17.
 */

public class ActorsRecyclerAdapter extends RecyclerView.Adapter<ActorsRecyclerAdapter.ViewHolder> {

    List<Actors> actorsList;

    public ActorsRecyclerAdapter(List<Actors> actorsList) {
        this.actorsList = actorsList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDOB, tvHeight, tvDesc, tvCountry, tvSpouse, tvChildren;
        ImageView imageDp;

        public ViewHolder(View itemView) {
            super(itemView);

            // reference views
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvDOB = (TextView) itemView.findViewById(R.id.tvDOB);
            tvHeight = (TextView) itemView.findViewById(R.id.tvHeight);
            tvDesc = (TextView) itemView.findViewById(R.id.tvDesc);
            tvCountry = (TextView) itemView.findViewById(R.id.tvCountry);
            tvSpouse = (TextView) itemView.findViewById(R.id.tvSpouse);
            tvChildren = (TextView) itemView.findViewById(R.id.tvChildren);
            imageDp = (ImageView) itemView.findViewById(R.id.imageDp);
        }
    }


    public ActorsRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ActorsRecyclerAdapter.ViewHolder holder, int position) {

        // setting views
        holder.tvName.setText(actorsList.get(position).getName());
        holder.tvDOB.setText(actorsList.get(position).getDob());
        holder.tvHeight.setText(actorsList.get(position).getHeight());
        holder.tvCountry.setText(actorsList.get(position).getCountry());
        holder.tvDesc.setText(actorsList.get(position).getDescription());
        holder.tvSpouse.setText(actorsList.get(position).getSpouse());
        holder.tvChildren.setText(actorsList.get(position).getChildren());
        // pass image url in execute()
        new DownloadImageTask(holder.imageDp).execute(actorsList.get(position).getImage());
    }


    @Override
    public int getItemCount() {
        return actorsList.size();
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                InputStream stream = url.openStream();
                bitmap = BitmapFactory.decodeStream(stream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        // publish results on the UI
        @Override
        protected void onPostExecute(Bitmap bitmapResult) {
            super.onPostExecute(bitmapResult);
            // set image
            if (bitmapResult != null)
                imageView.setImageBitmap(bitmapResult);
            // else: do anything dumb!!
        }
    }
}
