package com.martabak.kamar.activity.staff;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martabak.kamar.R;
import com.martabak.kamar.domain.Staff;
import com.martabak.kamar.service.StaffServer;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;

/**
 * Lists all registered staff and their most recent working hours.
 * Allows updating of their start and end time.
 * Use the {@link StaffHoursFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StaffHoursFragment extends Fragment {

    public StaffHoursFragment() {
    }

    /**
     * @return A new instance of fragment CheckGuestInFragment.
     */
    public static StaffHoursFragment newInstance() {
        return new StaffHoursFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_hours, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.staff_list);
        final List<Staff> staff = new ArrayList<>();
        final StaffRecyclerViewAdapter recyclerViewAdapter = new StaffRecyclerViewAdapter(staff);
        recyclerView.setAdapter(recyclerViewAdapter);

        StaffServer.getInstance(this.getActivity()).getStaffOfDivision(
                Staff.RESP_ENGINEERING, Staff.RESP_HOUSEKEEPING, Staff.RESP_MASSAGE
        ).subscribe(new Observer<Staff>() {
            @Override public void onCompleted() {
                Log.d(StaffHoursFragment.class.getCanonicalName(), "onCompleted");
                recyclerViewAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable e) {
                Log.d(StaffHoursFragment.class.getCanonicalName(), "onError", e);
                e.printStackTrace();
            }
            @Override public void onNext(final Staff s) {
                Log.d(StaffHoursFragment.class.getCanonicalName(), "Staff found " + s.firstName + " " + s.lastName);
                staff.add(s);
            }
        });

        return view;
    }

    public class StaffRecyclerViewAdapter
            extends RecyclerView.Adapter<StaffRecyclerViewAdapter.ViewHolder> {

        protected int selectedPos = -1;

        private final List<Staff> mValues;

        public StaffRecyclerViewAdapter(List<Staff> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.staff_list_row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.itemView.setSelected(selectedPos == position);
            holder.item = mValues.get(position);
            // TODO image view
            holder.firstNameView.setText(holder.item.firstName);
            holder.lastNameView.setText(holder.item.lastName);
            if (holder.item.startTime != null) {
                holder.startTimeView.setText(holder.item.startTime.toString()); // TODO date formatter
            }
            if (holder.item.endTime != null) {
                holder.endTimeView.setText(holder.item.endTime.toString()); // TODO date formatter
            }
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public Staff item;
            public final View rootView;
            public final ImageView imageView;
            public final TextView firstNameView;
            public final TextView lastNameView;
            public final TextView startTimeView;
            public final TextView endTimeView;

            public ViewHolder(View view) {
                super(view);
                rootView = view;
                imageView = (ImageView) view.findViewById(R.id.staff_image);
                firstNameView = (TextView) view.findViewById(R.id.staff_first_name);
                lastNameView = (TextView) view.findViewById(R.id.staff_last_name);
                startTimeView = (TextView) view.findViewById(R.id.staff_start_time);
                endTimeView = (TextView) view.findViewById(R.id.staff_end_time);
            }

            @Override
            public String toString() {
                return super.toString() + " " + firstNameView.getText() + " " + lastNameView.getText();
            }
        }
    }

}
