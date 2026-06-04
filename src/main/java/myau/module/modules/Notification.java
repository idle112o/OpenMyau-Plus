package myau.module.modules;

import myau.module.Module;
import myau.property.properties.BooleanProperty;
import myau.property.properties.ModeProperty;

public class Notification extends Module {
    public final ModeProperty mode = new ModeProperty("mode", 0, new String[]{"DEFAULT"});
    public final ModeProperty fontMode = new ModeProperty("font", 0, new String[]{"SANS", "MINECRAFT", "NUNITO"});
    public final BooleanProperty chat = new BooleanProperty("chat", false);
    public final BooleanProperty moduleToggled = new BooleanProperty("module-toggled", true);

    public Notification() {
        super("Notification", true, false, "Controls in-game notifications");
    }
}
