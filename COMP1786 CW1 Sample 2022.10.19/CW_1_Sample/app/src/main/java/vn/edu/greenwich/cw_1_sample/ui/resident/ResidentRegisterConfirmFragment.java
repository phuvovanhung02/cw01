package vn.edu.greenwich.cw_1_sample.ui.resident;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.R;

public class ResidentRegisterConfirmFragment extends DialogFragment {
    protected ResimaDAO _db;
    protected Resident _resident;
    protected Button fmResidentRegisterConfirmButtonConfirm, fmResidentRegisterConfirmButtonCancel;
    protected TextView fmResidentRegisterConfirmName,fmResidentRegisterConfirmDestination
            ,fmResidentRegisterConfirmDescription, fmResidentRegisterConfirmStartDate,
            fmResidentRegisterConfirmRisk, fmResidentRegisterConfirmUsername,fmResidentRegisterConfirmNumber;

    public ResidentRegisterConfirmFragment() {
        _resident = new Resident();
    }

    public ResidentRegisterConfirmFragment(Resident resident) {
        _resident = resident;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

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
        View view = inflater.inflate(R.layout.fragment_resident_register_confirm, container, false);

        String name = getString(R.string.error_no_info);
        String destination = getString(R.string.error_no_info);
        String description = getString(R.string.error_no_info);
        String username = getString(R.string.error_no_info);
        String number = getString(R.string.error_no_info);
        String startDate = getString(R.string.error_no_info);
        String risk = getString(R.string.error_no_info);

        fmResidentRegisterConfirmName = view.findViewById(R.id.fmResidentRegisterConfirmName);
        fmResidentRegisterConfirmDestination = view.findViewById(R.id.fmResidentRegisterConfirmDestination);
        fmResidentRegisterConfirmDescription = view.findViewById(R.id.fmResidentRegisterConfirmDescription);
        fmResidentRegisterConfirmNumber = view.findViewById(R.id.fmResidentRegisterConfirmNumber);
        fmResidentRegisterConfirmUsername = view.findViewById(R.id.fmResidentRegisterConfirmUsername);
        fmResidentRegisterConfirmStartDate = view.findViewById(R.id.fmResidentRegisterConfirmStartDate);
        fmResidentRegisterConfirmRisk = view.findViewById(R.id.fmResidentRegisterConfirmRisk);
        fmResidentRegisterConfirmButtonCancel = view.findViewById(R.id.fmResidentRegisterConfirmButtonCancel);
        fmResidentRegisterConfirmButtonConfirm = view.findViewById(R.id.fmResidentRegisterConfirmButtonConfirm);

        if (_resident.getRequiresRisk() != -1) {
            risk = _resident.getRequiresRisk() == 1 ? getString(R.string.label_risk) : getString(R.string.label_tenant);
        }

        if (_resident.getName_of_trip() != null && !_resident.getName_of_trip().trim().isEmpty()) {
            name = _resident.getName_of_trip();
        }
        if (_resident.getDestination() != null && !_resident.getDestination().trim().isEmpty()) {
            destination = _resident.getDestination();
        }
        if (_resident.getDescription() != null && !_resident.getDescription().trim().isEmpty()) {
            description = _resident.getDescription();
        }
        if (_resident.getUsername() != null && !_resident.getUsername().trim().isEmpty()) {
             username = _resident.getUsername();
        }
        if (_resident.getNumber() != null && !_resident.getNumber().trim().isEmpty()) {
             number= _resident.getNumber();
        }

        if (_resident.getStartDate() != null && !_resident.getStartDate().trim().isEmpty()) {
            startDate = _resident.getStartDate();
        }


        fmResidentRegisterConfirmName.setText(name);
        fmResidentRegisterConfirmDestination.setText(destination);
        fmResidentRegisterConfirmDescription.setText(description);
        fmResidentRegisterConfirmNumber.setText(number);
        fmResidentRegisterConfirmUsername.setText(username);
        fmResidentRegisterConfirmStartDate.setText(startDate);
        fmResidentRegisterConfirmRisk.setText(risk);

        fmResidentRegisterConfirmButtonCancel.setOnClickListener(v -> dismiss());
        fmResidentRegisterConfirmButtonConfirm.setOnClickListener(v -> confirm());

        return view;
    }

    protected void confirm() {
        long status = _db.insertResident(_resident);

        FragmentListener listener = (FragmentListener) getParentFragment();
        listener.sendFromResidentRegisterConfirmFragment(status);

        dismiss();
    }

    public interface FragmentListener {
        void sendFromResidentRegisterConfirmFragment(long status);
    }
}