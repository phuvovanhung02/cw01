package vn.edu.greenwich.cw_1_sample.ui.resident;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.Navigation;

import com.google.android.material.bottomappbar.BottomAppBar;

import vn.edu.greenwich.cw_1_sample.database.ResimaDAO;
import vn.edu.greenwich.cw_1_sample.models.Request;
import vn.edu.greenwich.cw_1_sample.models.Resident;
import vn.edu.greenwich.cw_1_sample.ui.dialog.DeleteConfirmFragment;
import vn.edu.greenwich.cw_1_sample.ui.request.RequestCreateFragment;
import vn.edu.greenwich.cw_1_sample.ui.request.list.RequestListFragment;
import vn.edu.greenwich.cw_1_sample.R;

public class ResidentDetailFragment extends Fragment
        implements DeleteConfirmFragment.FragmentListener, RequestCreateFragment.FragmentListener {
    public static final String ARG_PARAM_RESIDENT = "resident";

    protected ResimaDAO _db;
    protected Resident _resident;
    protected Button fmResidentDetailRequestButton;
    protected BottomAppBar fmResidentDetailBottomAppBar;
    protected FragmentContainerView fmResidentDetailRequestList;
    protected TextView fmResidentDetailName,fmResidentDetailDestination,fmResidentDetailDescription,
            fmResidentDetailStartDate, fmResidentDetailRisk,fmResidentDetailUsername,fmResidentDetailNumber;

    public ResidentDetailFragment() {}

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        _db = new ResimaDAO(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resident_detail, container, false);

        fmResidentDetailName = view.findViewById(R.id.fmResidentDetailName);
        fmResidentDetailDestination = view.findViewById(R.id.fmResidentDetailDestination);
        fmResidentDetailDescription = view.findViewById(R.id.fmResidentDetailDescription);
        fmResidentDetailStartDate = view.findViewById(R.id.fmResidentDetailStartDate);
        fmResidentDetailNumber = view.findViewById(R.id.fmResidentDetailNumber);
        fmResidentDetailUsername = view.findViewById(R.id.fmResidentDetailUsername);
        fmResidentDetailRisk = view.findViewById(R.id.fmResidentDetailRisk);
        fmResidentDetailBottomAppBar = view.findViewById(R.id.fmResidentDetailBottomAppBar);
        fmResidentDetailRequestButton = view.findViewById(R.id.fmResidentDetailRequestButton);
        fmResidentDetailRequestList = view.findViewById(R.id.fmResidentDetailRequestList);

        fmResidentDetailBottomAppBar.setOnMenuItemClickListener(item -> menuItemSelected(item));
        fmResidentDetailRequestButton.setOnClickListener(v -> showAddRequestFragment());

        showDetails();
        showRequestList();

        return view;
    }

    protected void showDetails() {
        String name = getString(R.string.error_not_found);
        String destination = getString(R.string.error_no_info);
        String description = getString(R.string.error_no_info);
        String username = getString(R.string.error_no_info);
        String number = getString(R.string.error_no_info);
        String startDate = getString(R.string.error_not_found);
        String risk = getString(R.string.error_not_found);

        if (getArguments() != null) {
            _resident = (Resident) getArguments().getSerializable(ARG_PARAM_RESIDENT);
            _resident = _db.getResidentById(_resident.getId()); // Retrieve data from Database.

            name = _resident.getName_of_trip();
            destination = _resident.getDestination();
            description = _resident.getDescription();
            username = _resident.getUsername();
            number = _resident.getNumber();
            startDate = _resident.getStartDate();
            risk = _resident.getRequiresRisk() == 1 ? getString(R.string.label_risk) : getString(R.string.label_tenant);
        }

        fmResidentDetailName.setText(name);
        fmResidentDetailNumber.setText(number);
        fmResidentDetailUsername.setText(username);
        fmResidentDetailDestination.setText(destination);
        fmResidentDetailDescription.setText(description);
        fmResidentDetailStartDate.setText(startDate);
        fmResidentDetailRisk.setText(risk);
    }

    protected void showRequestList() {
        if (getArguments() != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(RequestListFragment.ARG_PARAM_RESIDENT_ID, _resident.getId());

            // Send arguments (resident id) to RequestListFragment.
            getChildFragmentManager().getFragments().get(0).setArguments(bundle);
        }
    }

    protected boolean menuItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.residentUpdateFragment:
                showUpdateFragment();
                return true;

            case R.id.residentDeleteFragment:
                showDeleteConfirmFragment();
                return true;
        }

        return true;
    }

    protected void showUpdateFragment() {
        Bundle bundle = null;

        if (_resident != null) {
            bundle = new Bundle();
            bundle.putSerializable(ResidentUpdateFragment.ARG_PARAM_RESIDENT, _resident);
        }

        Navigation.findNavController(getView()).navigate(R.id.residentUpdateFragment, bundle);
    }

    protected void showDeleteConfirmFragment() {
        new DeleteConfirmFragment(getString(R.string.notification_delete_confirm)).show(getChildFragmentManager(), null);
    }

    protected void showAddRequestFragment() {
        new RequestCreateFragment(_resident.getId()).show(getChildFragmentManager(), null);
    }

    @Override
    public void sendFromDeleteConfirmFragment(int status) {
        if (status == 1 && _resident != null) {
            long numOfDeletedRows = _db.deleteResident(_resident.getId());

            if (numOfDeletedRows > 0) {
                Toast.makeText(getContext(), R.string.notification_delete_success, Toast.LENGTH_SHORT).show();
                Navigation.findNavController(getView()).navigateUp();

                return;
            }
        }

        Toast.makeText(getContext(), R.string.notification_delete_fail, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendFromRequestCreateFragment(Request request) {
        if (request != null) {
            request.setResidentId(_resident.getId());

            long id = _db.insertRequest(request);

            Toast.makeText(getContext(), id == -1 ? R.string.notification_create_fail : R.string.notification_create_success, Toast.LENGTH_SHORT).show();

            reloadRequestList();
        }
    }

    protected void reloadRequestList() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(RequestListFragment.ARG_PARAM_RESIDENT_ID, _resident.getId());

        getChildFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fmResidentDetailRequestList, RequestListFragment.class, bundle)
                .commit();
    }
}