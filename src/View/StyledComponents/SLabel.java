package View.StyledComponents;

import javax.swing.*;
import java.awt.*;

public class SLabel extends JLabel {

    public SLabel(){
        super();
        applyStyling();
    }

    public SLabel(String text){
        super(text);
        applyStyling();
    }

    public SLabel(String text, int horizontalAlignment){
        super(text, horizontalAlignment);
        applyStyling();
    }

    private void applyStyling() {
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(StyleColors.TEXT);
    }

}
