package vn.edu.greenwich.cw_1_sample.ui.resident;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.ui.dialog.CalendarFragment;
import vn.edu.greenwich.cw_1_sample.R;

public class ResidentSearchFragment extends DialogFragment implements CalendarFragment.FragmentListener {
    protected EditText fmResidentSearchDate, fmResidentSearchName,fmResidentSearchDestination,
            fmResidentSearchDescription,fmResidentSearchNumber,fmResidentSearchUsername;
    protected Button fmResidentSearchButtonCancel, fmResidentSearchButtonSearch;

    public ResidentSearchFragment() {}

    @Override
    public void onResume() {
        super.onResume();

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_search, container, false);

        fmResidentSearchDate = view.findViewById(R.id.fmResidentSearchDate);
        fmResidentSearchName = view.findViewById(R.id.fmResidentSearchName);
        fmResidentSearchDestination = view.findViewById(R.id.fmResidentSearchDestination);
        fmResidentSearchDescription = view.findViewById(R.id.fmResidentSearchDescription);
        fmResidentSearchUsername = view.findViewById(R.id.fmResidentSearchUsername);
        fmResidentSearchNumber = view.findViewById(R.id.fmResidentSearchNumber);
        fmResidentSearchButtonCancel = view.findViewById(R.id.fmResidentSearchButtonCancel);
        fmResidentSearchButtonSearch = view.findViewById(R.id.fmResidentSearchButtonSearch);
        fmResidentSearchButtonSearch.setOnClickListener(v -> search());
        fmResidentSearchButtonCancel.setOnClickListener(v -> dismiss());
        fmResidentSearchDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        return view;
    }

    protected void search() {
        Resident _resident = new Resident();

        String date = fmResidentSearchDate.getText().toString();
        String destination = fmResidentSearchDestination.getText().toString();
        String description = fmResidentSearchDescription.getText().toString();
        String username = fmResidentSearchUsername.getText().toString();
        String number = fmResidentSearchNumber.getText().toString();
        String name = fmResidentSearchName.getText().toString();

        if (date != null  && !date.trim().isEmpty())
            _resident.setStartDate(date);

        if (name != null &&number!=null && destination != null && description != null && username != null &&!name.trim().isEmpty())
            _resident.setName_of_trip(name);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromResidentSearchFragment(_resident);

        dismiss();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmResidentSearchDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromResidentSearchFragment(Resident resident);
    }
}