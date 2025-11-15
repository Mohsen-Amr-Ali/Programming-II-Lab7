package View.StyledComponents;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class STField extends JTextField {

    public STField(){
        super();
        applyStyling();
    }

    public STField(int columns){
        super(columns);
        applyStyling();
    }

    public STField(String text){
        super(text);
        applyStyling();
    }

    public STField(String text, int columns){
        super(text, columns);
        applyStyling();
    }

    private void applyStyling(){
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setForeground(StyleColors.TEXT);
        setBackground(StyleColors.CARD);

        Border flatBorder = BorderFactory.createLineBorder(StyleColors.ACCENT_DARK, 1 );
        Border padding = BorderFactory.createEmptyBorder(5, 8, 5, 8);

        setBorder(BorderFactory.createCompoundBorder(flatBorder, padding));

    }
}
