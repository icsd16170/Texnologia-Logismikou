package project.dto;

public class UserAction {

    private String lastApiCalled;

    private int tries = 0;


    public UserAction() {
    }

    public UserAction(String lastApiCalled, int tries) {
        this.lastApiCalled = lastApiCalled;
        this.tries = tries;
    }

    public String getLastApiCalled() {
        return lastApiCalled;
    }

    public void setLastApiCalled(String lastApiCalled) {
        this.lastApiCalled = lastApiCalled;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
