package client.browse;

public class WorkoutTableRow {
    private String uploader;
    private String name;

    public WorkoutTableRow(String uploader, String name) {
        this.uploader = uploader;
        this.name = name;
    }

    public String getUploader() {
        return uploader;
    }

    public void setUploader(String uploader) {
        this.uploader = uploader;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
