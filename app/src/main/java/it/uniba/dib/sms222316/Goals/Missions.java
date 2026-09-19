package it.uniba.dib.sms222316.Goals;

public class Missions {

    private String Title, Type;
    private int ProgressNumber, Rewards, Target;

    public Missions(String title, int progressNumber, int rewards, int target, String type) {
        Title = title;
        ProgressNumber = progressNumber;
        Rewards = rewards;
        Target = target;
        Type = type;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getProgressNumber() {
        return ProgressNumber;
    }

    public void setProgressNumber(int progressNumber) {
        ProgressNumber = progressNumber;
    }

    public int getRewards() {
        return Rewards;
    }

    public int getTarget() {
        return Target;
    }

    public void setTarget(int target) {
        Target = target;
    }

    public void setRewards(int rewards) {
        Rewards = rewards;
    }

}
