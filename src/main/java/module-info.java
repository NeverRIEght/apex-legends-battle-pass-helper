module com.apexlegendsbphelper.apexlegendsbattlepasshelper {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.apexlegendsbphelper.apexlegendsbattlepasshelper to javafx.fxml;
    exports com.apexlegendsbphelper.apexlegendsbattlepasshelper;
}