package com.avhar.launchtracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class LaunchAdapter extends RecyclerView.Adapter<LaunchAdapter.ViewHolder> {
  private List<Launch> launches;

  public LaunchAdapter(List<Launch> launches) {
    this.launches = launches;
  }

  @Override
  public LaunchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    Context context = parent.getContext();
    LayoutInflater inflater = LayoutInflater.from(context);

    // Inflate the custom layout
    View launchView = inflater.inflate(R.layout.launch_card, parent, false);

    // Return a new holder instance
    return new ViewHolder(launchView);
  }

  @Override
  public void onBindViewHolder(LaunchAdapter.ViewHolder holder, int position) {
    // Get the data model based on position
    Launch launch = launches.get(position);

    // Set item views based on your views and data model
    TextView nameView = holder.name;
    nameView.setText(launch.getName());

    TextView providerView = holder.provider;
    providerView.setText(launch.getProvider());
  }

  @Override
  public int getItemCount() {
    return launches.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    // Your holder should contain a member variable
    // for any view that will be set as you render a row
    public TextView name;
    public TextView provider;

    // We also create a constructor that accepts the entire item row
    // and does the view lookups to find each subview
    public ViewHolder(View itemView) {
      // Stores the itemView in a public final member variable that can be used
      // to access the context from any ViewHolder instance.
      super(itemView);

      name = (TextView) itemView.findViewById(R.id.launchName);
      provider = (TextView) itemView.findViewById(R.id.launchProvider);
    }
  }
}
