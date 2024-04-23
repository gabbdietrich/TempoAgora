import javax.swing.*;

public class AppLauncher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new tempoGui().setVisible(true);

//                System.out.println(PrevisaoApp.getLocationData("Brasil"));
//
//                System.out.println(PrevisaoApp.getCurrentTime());
            }
        });

    }
}
