package vn.edu.greenwich.cw_1_sample.ui.resident;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.ui.dialog.CalendarFragment;
import vn.edu.greenwich.cw_1_sample.R;

public class ResidentRegisterFragment extends Fragment
        implements ResidentRegisterConfirmFragment.FragmentListener, CalendarFragment.FragmentListener {
    public static final String ARG_PARAM_RESIDENT = "Detail_trip";

    protected EditText fmResidentRegisterName,fmResidentRegisterDestination,fmResidentRegisterDescription
            , fmResidentRegisterStartDate,fmResidentRegisterNumber,fmResidentRegisterUsername ;
    protected LinearLayout fmResidentRegisterLinearLayout;
    protected SwitchMaterial fmResidentRegisterRisk;
    protected TextView fmResidentRegisterError;
    protected Button fmResidentRegisterButton;

    protected ResimaDAO _db;

    public ResidentRegisterFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_register, container, false);

        fmResidentRegisterError = view.findViewById(R.id.fmResidentRegisterError);
        fmResidentRegisterName = view.findViewById(R.id.fmResidentRegisterName);
        fmResidentRegisterDestination = view.findViewById(R.id.fmResidentRegisterDestination);
        fmResidentRegisterDescription = view.findViewById(R.id.fmResidentRegisterDescription);
        fmResidentRegisterUsername = view.findViewById(R.id.fmResidentRegisterUsername);
        fmResidentRegisterNumber = view.findViewById(R.id.fmResidentRegisterNumber);
        fmResidentRegisterStartDate = view.findViewById(R.id.fmResidentRegisterStartDate);
        fmResidentRegisterRisk = view.findViewById(R.id.fmResidentRegisterRisk);
        fmResidentRegisterButton = view.findViewById(R.id.fmResidentRegisterButton);
        fmResidentRegisterLinearLayout = view.findViewById(R.id.fmResidentRegisterLinearLayout);

        // Show Calendar for choosing a date.
        fmResidentRegisterStartDate.setOnTouchListener((v, motionEvent) -> showCalendar(motionEvent));

        // Update current resident.
        if (getArguments() != null) {
            Resident resident = (Resident) getArguments().getSerializable(ARG_PARAM_RESIDENT);

            fmResidentRegisterName.setText(resident.getName_of_trip());
            fmResidentRegisterDestination.setText(resident.getDestination());
            fmResidentRegisterDescription.setText(resident.getDescription());
            fmResidentRegisterUsername.setText(resident.getUsername());
            fmResidentRegisterNumber.setText(resident.getNumber());
            fmResidentRegisterStartDate.setText(resident.getStartDate());
            fmResidentRegisterRisk.setChecked(resident.getRequiresRisk() == 1 ? true : false);

            fmResidentRegisterButton.setText(R.string.label_update);
            fmResidentRegisterButton.setOnClickListener(v -> update(resident.getId()));

            return view;
        }

        // Create new resident.
        fmResidentRegisterButton.setOnClickListener(v -> register());

        return view;
    }

    protected void register() {
        if (isValidForm()) {
            Resident resident = getResidentFromInput(-1);

            new ResidentRegisterConfirmFragment(resident).show(getChildFragmentManager(), null);

            return;
        }

        moveButton();
    }

    protected void update(long id) {
        if (isValidForm()) {
            Resident resident = getResidentFromInput(id);

            long status = _db.updateResident(resident);

            FragmentListener listener = (FragmentListener) getParentFragment();
            listener.sendFromResidentRegisterFragment(status);

            return;
        }

        moveButton();
    }

    protected boolean showCalendar(MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            new CalendarFragment().show(getChildFragmentManager(), null);
        }

        return false;
    }

    protected Resident getResidentFromInput(long id) {
        String name = fmResidentRegisterName.getText().toString();
        String destination = fmResidentRegisterDestination.getText().toString();
        String description = fmResidentRegisterDescription.getText().toString();
        String username = fmResidentRegisterUsername.getText().toString();
        String number = fmResidentRegisterNumber.getText().toString();
        String startDate = fmResidentRegisterStartDate.getText().toString();
        int risk = fmResidentRegisterRisk.isChecked() ? 1 : 0;

        return new Resident(id, name, startDate, risk,destination,description,username,number);
    }

    protected boolean isValidForm() {
        boolean isValid = true;

        String error = "";
        String name = fmResidentRegisterName.getText().toString();
        String username = fmResidentRegisterUsername.getText().toString();
        String number = fmResidentRegisterNumber.getText().toString();
        String destination = fmResidentRegisterDestination.getText().toString();
        String description = fmResidentRegisterDescription.getText().toString();
        String startDate = fmResidentRegisterStartDate.getText().toString();

        if (name == null || name.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }
        if (username == null || username.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }

        if (destination == null || destination.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_destination) + "\n";

            isValid = false;
        }
        if (startDate == null   || startDate.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_start_date) + "\n";
            isValid = false;
        }
        if (description == null || description.trim().isEmpty()) {
            error += "* " + getString(R.string.error_blank_name) + "\n";
            isValid = false;
        }
        if (number == null   || number.trim().isEmpty()) {
            error += "* " + "Number of passenger can't blank." + "\n";
            isValid = false;
        }

        fmResidentRegisterError.setText(error);

        return isValid;
    }

    protected void moveButton() {
        LinearLayout.LayoutParams btnParams = (LinearLayout.LayoutParams) fmResidentRegisterButton.getLayoutParams();

        int linearLayoutPaddingLeft = fmResidentRegisterLinearLayout.getPaddingLeft();
        int linearLayoutPaddingRight = fmResidentRegisterLinearLayout.getPaddingRight();
        int linearLayoutWidth = fmResidentRegisterLinearLayout.getWidth() - linearLayoutPaddingLeft - linearLayoutPaddingRight;

        btnParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        btnParams.topMargin += fmResidentRegisterButton.getHeight();
        btnParams.leftMargin = btnParams.leftMargin == 0 ? linearLayoutWidth - fmResidentRegisterButton.getWidth() : 0;

        fmResidentRegisterButton.setLayoutParams(btnParams);
    }

    @Override
    public void sendFromResidentRegisterConfirmFragment(long status) {
        switch ((int) status) {
            case -1:
                Toast.makeText(getContext(), R.string.notification_create_fail, Toast.LENGTH_SHORT).show();
                return;

            default:
                Toast.makeText(getContext(), R.string.notification_create_success, Toast.LENGTH_SHORT).show();

                fmResidentRegisterName.setText("");
                fmResidentRegisterUsername.setText("");
                fmResidentRegisterNumber.setText("");
                fmResidentRegisterDestination.setText("");
                fmResidentRegisterDescription.setText("");
                fmResidentRegisterStartDate.setText("");

                fmResidentRegisterName.requestFocus();
        }
    }

    @Override
    public void sendFromCalendarFragment(String date) {
        fmResidentRegisterStartDate.setText(date);
    }

    public interface FragmentListener {
        void sendFromResidentRegisterFragment(long status);
    }
}