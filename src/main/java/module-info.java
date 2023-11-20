module com.apexlegendsbphelper.apexlegendsbattlepasshelper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens org.apexlegendsbphelper to javafx.fxml;
    exports org.apexlegendsbphelper;
    exports org.apexlegendsbphelper.Controller;
    opens org.apexlegendsbphelper.Controller to javafx.fxml;
}