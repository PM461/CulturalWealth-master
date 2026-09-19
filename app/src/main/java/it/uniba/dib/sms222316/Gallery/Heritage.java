package it.uniba.dib.sms222316.Gallery;



public class Heritage {

    private String Title, Description, Type, Pic;
    private boolean Own;

    public Heritage() {
    }

    public Heritage(String title, String description, String type, String pic, boolean own) {
        Title = title;
        Description = description;
        Type = type;
        Pic = pic;
        Own = own;
    }

    public String getPic() {
        return Pic;
    }

    public String getTitle() {
        return Title;
    }

    public String getDescription() {
        return Description;
    }

    public String getType() {
        return Type;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setType(String type) {
        Type = type;
    }

    public void setPic(String pic) {
        Pic = pic;
    }

    public boolean isOwn() {
        return Own;
    }

    public void setOwn(boolean own) {
        Own = own;
    }
}
