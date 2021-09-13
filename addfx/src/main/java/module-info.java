module org.openjfx.addfx {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;
	
    opens org.openjfx.addfx to javafx.fxml;
    exports org.openjfx.addfx;
}
