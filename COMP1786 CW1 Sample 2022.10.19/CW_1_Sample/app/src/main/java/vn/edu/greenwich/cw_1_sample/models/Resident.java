package vn.edu.greenwich.cw_1_sample.models;

import java.io.Serializable;

public class Resident implements Serializable {
    protected long _id;
    protected String _Name_of_trip;
    protected String _Destination;
    protected String _Description;
    protected String _Username;
    protected String _Number;
    protected String _start_date_of_trip;
    protected int _Requires_risk_assessment;

    public Resident() {
        _id = -1;
        _Username = null;
        _Number = null ;
        _Name_of_trip= null;
        _Destination = null;
        _Description = null;
        _start_date_of_trip= null;
        _Requires_risk_assessment= -1;
    }

    public Resident(long id, String Name_of_trip, String startDate, int risk,String Description,String Destination,String Username, String number) {
        _id = id;
        _Name_of_trip= Name_of_trip;
        _Destination= Destination;
        _Description= Description;
        _Username = Username;
        _Number= number;
        _start_date_of_trip= startDate;
        _Requires_risk_assessment= risk;
    }

    public long getId() { return _id; }
    public void setId(long id) {
        _id = id;
    }

    public String getName_of_trip() {
        return _Name_of_trip;
    }
    public void setName_of_trip(String Name_of_trip) {
        _Name_of_trip= Name_of_trip;
    }
    public String getDestination() {
        return _Destination;
    }
    public void setDestination(String Destination) {
        _Destination= Destination;
    }
    public String getDescription() {
        return _Description;
    }
    public void setDescription(String Description) {
        _Description= Description;
    }

    public String getStartDate() {
        return _start_date_of_trip;
    }
    public void setStartDate(String startDate) {
        _start_date_of_trip= startDate;
    }
    public String getNumber() {
        return _Number;
    }
    public void setNumber(String number) {
        _Number= number;
    }

    public String getUsername() {
        return _Username;
    }
    public void setUsername(String Username) {
        _Username= Username;
    }

    public int getRequiresRisk() {
        return _Requires_risk_assessment;
    }
    public void setRequiresRisk(int risk) {
        _Requires_risk_assessment= risk;
    }

    public boolean isEmpty() {
        if (-1 == _id && null == _Name_of_trip&& null==_Description && null ==_Destination && null == _Username && null == _Number && null == _start_date_of_trip&& -1 == _Requires_risk_assessment)
            return true;

        return false;
    }

    @Override
    public String toString() {
        return "[" + _start_date_of_trip+ "] " + _Name_of_trip + _Destination + _Description + _Username + _Number;
    }
}