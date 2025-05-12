package controller;

public class ControllerFactory {
    public static BaseController getController(String action) {
        if (action.startsWith("lead/")) {
            return new LeadController();
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }
}
