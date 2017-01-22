package creationsofali.json;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


/**
 * Created by ali on 11/13/16.
 */

public class ActorsListAdapter extends BaseAdapter {


    Context context;
    private ArrayList<Actors> actorsList;
    private LayoutInflater inflater;
    private int resource;

    // ctor
    public ActorsListAdapter(Context context, int resource, ArrayList<Actors> actors) {
        this.context = context;
        actorsList = actors;
        this.resource = resource;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return actorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return actorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // for the 1st time view created
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
            holder = new ViewHolder();

            // referencing view
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvDOB = (TextView) convertView.findViewById(R.id.tvDOB);
            holder.tvHeight = (TextView) convertView.findViewById(R.id.tvHeight);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);
            holder.tvCountry = (TextView) convertView.findViewById(R.id.tvCountry);
            holder.tvSpouse = (TextView) convertView.findViewById(R.id.tvSpouse);
            holder.tvChildren = (TextView) convertView.findViewById(R.id.tvChildren);
            holder.imageDp = (ImageView) convertView.findViewById(R.id.imageDp);

            convertView.setTag(holder);

        } else {
            // if there'is data already in holder
            holder = (ViewHolder) convertView.getTag();
        }

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


        return convertView;
    }

    static class ViewHolder {
        public ImageView imageDp;
        public TextView tvName;
        public TextView tvDesc;
        public TextView tvDOB;
        public TextView tvCountry;
        public TextView tvHeight;
        public TextView tvSpouse;
        public TextView tvChildren;
    }

    // image downloading task
    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        DownloadImageTask(ImageView image) {
            super();
            this.image = image;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bm = null;

            try {
                URL url = new URL(params[0]);
                InputStream stream = url.openStream();
                bm = BitmapFactory.decodeStream(stream);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            // set image parsed on doInBackground
            if (bitmap != null)
                image.setImageBitmap(bitmap);
            // else: set a default image if you want
        }
    }
}
