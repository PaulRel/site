package Interface;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class OrderView {
	private AnchorPane rootPane;
	
	public OrderView(MainView mainView) {
		rootPane = new AnchorPane();
        createZone();
        Scene orderScene = new Scene(rootPane, 1350, 670);
        
        HeaderView v = new HeaderView(mainView);      
        rootPane.getChildren().addAll(v.getHeader());
        String css = this.getClass().getResource("/style.css").toExternalForm();        
        orderScene.getStylesheets().add(css);
        mainView.getPrimaryStage().setScene(orderScene);
	}
	
	public void createZone() {
		
	}
	
}
