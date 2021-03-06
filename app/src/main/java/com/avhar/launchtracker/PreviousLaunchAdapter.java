package com.avhar.launchtracker;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.avhar.launchtracker.data.Launch;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class PreviousLaunchAdapter extends RecyclerView.Adapter<PreviousLaunchAdapter.ViewHolder> {
  private final List<Launch> launches;
  private final Context context;

  public PreviousLaunchAdapter(List<Launch> launches, Context context) {
    this.launches = launches;
    this.context = context;
  }

  @NonNull
  @Override
  public PreviousLaunchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    // Inflate the custom layout
    View launchView = inflater.inflate(R.layout.previous_launch_card, parent, false);

    // Return a new holder instance
    return new ViewHolder(launchView);
  }

  @Override
  public void onBindViewHolder(PreviousLaunchAdapter.ViewHolder holder, int position) {
    Launch launch = launches.get(position);

    CardView cardView = holder.cardView;
    cardView.setOnClickListener(view -> {
      Intent i = new Intent(context, DetailsActivity.class);
      i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      i.putExtra("launch", launch);
      i.putExtra("displayCountdown", false);
      context.startActivity(i);
    });

    View statusBar = holder.statusBar;

    switch (launch.getStatus()) {
      case 1:
      case 3:
        statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.go_for_launch));
        break;
      case 2:
      case 5:
      case 8:
        statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.tbd));
        break;
      case 4:
        statusBar.setBackgroundColor(ContextCompat.getColor(context, R.color.fail));
        break;
      default:
    }


    TextView nameView = holder.name;
    nameView.setText(launch.getName());

    TextView providerView = holder.provider;
    providerView.setText(String.format("%s - %s", launch.getProvider(), launch.getLaunchType()));

    TextView netView = holder.net;
    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy 'at' HH:mm:ss", Locale.getDefault());
    format.setTimeZone(TimeZone.getDefault());

    netView.setText(format.format(launch.getNet()));
  }

  @Override
  public int getItemCount() {
    if (launches == null) {
      return 0;
    }
    return launches.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public CardView cardView;
    public TextView name;
    public TextView provider;
    public TextView net;
    public View statusBar;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolder(View itemView) {
      // Stores the itemView in a public final member variable that can be used
      // to access the context from any ViewHolder instance.
      super(itemView);

      name = itemView.findViewById(R.id.launchName);
      provider = itemView.findViewById(R.id.launchProviderAndType);
      net = itemView.findViewById(R.id.launchNet);
      cardView = itemView.findViewById(R.id.cardView);
      statusBar = itemView.findViewById(R.id.statusBar);
    }
  }
}
