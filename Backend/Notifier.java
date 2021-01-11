package org.firstinspires.ftc.teamcode.Backend;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Notifier {

    private Telemetry tele;

    public Notifier(Telemetry tele) {
        this.tele = tele;
    }

    public void message(String text) {
        tele.addData("Status", text);
        tele.update();
    }

    public void cMessage(String text) {
        tele.clear();
        tele.addData("Status", text);
        tele.update();
    }

    public void clear() {
        tele.clear();
        tele.update();
    }
}
