package Authority;

public class ViewEmergency {

    private int emergencyId;
    private int userId;
    private String type;
    private String location;
    private String status;
    private String time;

    public ViewEmergency(int emergencyId, int userId,
                         String type, String location,
                         String status, String time) {

        this.emergencyId = emergencyId;
        this.userId = userId;
        this.type = type;
        this.location = location;
        this.status = status;
        this.time = time;
    }

    public int getEmergencyId() { return emergencyId; }
    public int getUserId() { return userId; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getTime() { return time; }
}